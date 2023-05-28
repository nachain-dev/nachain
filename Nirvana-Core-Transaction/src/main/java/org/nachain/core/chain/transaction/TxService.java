package org.nachain.core.chain.transaction;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.base.Amount;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.instance.npp.InstanceNppService;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxContextService;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMark;
import org.nachain.core.config.ChainConfig;
import org.nachain.core.crypto.Key;
import org.nachain.core.intermediate.AccountTxHeightService;
import org.nachain.core.intermediate.feedback.Feedback;
import org.nachain.core.intermediate.feedback.FeedbackService;
import org.nachain.core.persistence.rocksdb.page.Page;
import org.nachain.core.persistence.rocksdb.page.PageCallback;
import org.nachain.core.persistence.rocksdb.page.PageService;
import org.nachain.core.persistence.rocksdb.page.SortEnum;
import org.nachain.core.signverify.SignVerify;
import org.nachain.core.token.CoreTokenEnum;
import org.nachain.core.token.Token;
import org.nachain.core.token.TokenService;
import org.nachain.core.token.TokenSingleton;
import org.nachain.core.token.nft.dto.NftMintedDTO;
import org.nachain.core.util.CommUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class TxService {


    public static List<String> excludeAddress;

    static {
        excludeAddress = Lists.newArrayList();

        TxReservedWord[] txReservedWords = TxReservedWord.values();
        for (TxReservedWord txReservedWord : txReservedWords) {

            if (txReservedWord.name.equals(TxReservedWord.INSTANCE.name)) {
                continue;
            }

            excludeAddress.add(txReservedWord.name);
        }
    }


    public static Tx newEmptyTx() {
        return new Tx();
    }


    public static Tx newNftMintedTx(long instance, long token, long nftTokenId, String owner, String mintTx, Key minerKey) throws Exception {

        String from = TxReservedWord.MINT.name;


        NftMintedDTO nftMintedDTO = new NftMintedDTO();
        nftMintedDTO.setInstance(instance);
        nftMintedDTO.setToken(token);
        nftMintedDTO.setNftItemId(nftTokenId);
        nftMintedDTO.setOwner(owner);
        nftMintedDTO.setFromTx(mintTx);


        TxContext txContext = TxContextService.newNftMintedContext(nftMintedDTO);


        long txHeight = AccountTxHeightService.nextTxHeight(from, instance, token);


        String remarks = String.format("MINT #%d", nftTokenId);


        BigInteger value = Amount.toToken(1);
        return newTx(TxType.MINT, instance, token, from, owner, value, BigInteger.ZERO, TxGasType.NAC.value, txHeight, TxInputData.newEmptyInputData(), TxOutputService.newOutput(), TxInputData.newEmptyInputData(), txContext, remarks, 0, minerKey);
    }


    public static Tx newNftAuthorTx(long instance, long token, long nftItemId, String author, BigInteger value, Tx mintTx, Key minerKey) throws Exception {

        String from = TxReservedWord.MINT.name;


        TxInputData inputData = TxInputData.newInputData(TxInputService.newTxInput(mintTx.getInstance(), mintTx.getHash(), mintTx.getValue()));


        long txHeight = AccountTxHeightService.nextTxHeight(from, instance, token);


        String remarks = String.format("MINT AUTHOR #%d", nftItemId);


        return newTx(TxType.MINT, instance, token, from, author, value, BigInteger.ZERO, TxGasType.NAC.value, txHeight, inputData, TxOutputService.newOutput(), TxInputData.newEmptyInputData(), TxContextService.newTransferContext(instance), remarks, 0, minerKey);
    }


    public static Tx newMinedTx(long instance, String miner, BigInteger blockReward, TxContext txContext, String remarks, Key minerKey) throws Exception {

        String from = TxReservedWord.COINBASE.name;


        long token = CoreTokenEnum.NAC.id;


        long txHeight = AccountTxHeightService.nextTxHeight(from, instance, token);


        if (remarks == null) {
            remarks = TxReservedWord.COINBASE.name;
        }


        return newTx(TxType.COINBASE, instance, token, from, miner, blockReward, BigInteger.ZERO, TxGasType.NAC.value, txHeight, TxInputData.newEmptyInputData(), TxOutputService.newOutput(), TxInputData.newEmptyInputData(), txContext, remarks, 0, minerKey);
    }


    public static Tx newMinedGasAwardTx(long instance, String miner, BigInteger blockReward, TxContext txContext, String remarks, Key minerKey) throws Exception {

        String from = TxReservedWord.COINBASE.name;


        long token = CoreTokenEnum.NAC.id;


        long txHeight = AccountTxHeightService.nextTxHeight(from, instance, token);


        if (remarks == null) {
            remarks = TxReservedWord.COINBASE.name + " GAS AWARD";
        }


        return newTx(TxType.COINBASE, instance, token, from, miner, blockReward, BigInteger.ZERO, TxGasType.NAC.value, txHeight, TxInputData.newEmptyInputData(), TxOutputService.newOutput(), TxInputData.newEmptyInputData(), txContext, remarks, 0, minerKey);
    }


    public static Tx newDeductMinedTx(long instance, String miner, BigInteger blockReward, TxContext txContext, String remarks, Key minerKey) throws Exception {

        String from = TxReservedWord.COINBASE.name;


        long token = getTxToken(instance);


        long txHeight = AccountTxHeightService.nextTxHeight(from, instance, token);


        if (remarks == null) {
            remarks = TxReservedWord.COINBASE.name;
        }


        return newTx(TxType.COINBASE, instance, token, miner, TxReservedWord.COINBASE.name, blockReward, BigInteger.ZERO, TxGasType.NAC.value, txHeight, TxInputData.newEmptyInputData(), TxOutputService.newOutput(), TxInputData.newEmptyInputData(), txContext, remarks, 0, minerKey);
    }


    public static Tx newEventRefundTx(Tx failedTx, Key minerKey) throws Exception {

        TxInputData inputData = TxInputData.newInputData(TxInputService.newTxInput(failedTx.getInstance(), failedTx.getHash(), failedTx.getValue()));

        long txHeight = AccountTxHeightService.nextTxHeight(TxReservedWord.TRANSFER_EVENT_REFUND.name, failedTx.getInstance(), failedTx.getToken());

        Tx eventRefundTx = TxService.newTx(TxType.TRANSFER_EVENT_REFUND, failedTx.getInstance(), failedTx.getToken(), failedTx.getTo(), failedTx.getFrom(), failedTx.getValue(), BigInteger.ZERO, failedTx.getGasType(), txHeight, inputData, TxOutputService.newOutput(), TxInputData.newEmptyInputData(), TxContextService.newTransferContext(failedTx.getInstance()), TxReservedWord.TRANSFER_EVENT_REFUND.name, 0, minerKey);

        return eventRefundTx;
    }


    public static Tx newEventWithdrawTx(long instance, long token, String from, String to, BigInteger sendAmount, long txHeight, TxContext context, Key minerKey) throws Exception {

        Tx eventWithdrawTx = TxService.newTx(TxType.TRANSFER_EVENT_WITHDRAW, instance, token, from, to, sendAmount, BigInteger.ZERO, TxGasType.NAC.value, txHeight, TxInputData.newEmptyInputData(), TxOutputService.newOutput(), TxInputData.newEmptyInputData(), context, TxReservedWord.TRANSFER_EVENT_WITHDRAW.name, 0, minerKey);

        return eventWithdrawTx;
    }


    public static Tx newGasTx(long instance, String from, String fromTxHash, long gasType, BigInteger gasAmount, long txHeight, Key minerKey) throws Exception {

        TxOutput gasTxOutput = TxOutputService.newTxOutput(instance, fromTxHash, gasAmount);


        String gasRemarks = String.format(TxReservedWord.TRANSFER_GAS.name + " from %s", fromTxHash);


        Tx gasTx = TxService.newTx(TxType.TRANSFER_GAS, instance, gasType, from, TxReservedWord.TRANSFER_GAS.name, gasAmount, BigInteger.ZERO, gasType, txHeight, TxInputData.newEmptyInputData(), gasTxOutput, TxInputData.newEmptyInputData(), TxContextService.newTransferContext(instance), gasRemarks, 0, minerKey);

        return gasTx;
    }


    public static Tx newChangeTx(long instanceId, long token, String from, String fromTxHash, BigInteger value, long gasType, long txHeight, TxInputData inputData, Key minerKey) throws Exception {

        String remarks = String.format(TxReservedWord.TRANSFER_CHANGE.name + " from %s", fromTxHash);


        Tx changeTx = TxService.newTx(TxType.TRANSFER_CHANGE, instanceId, token, from, from, value, BigInteger.ZERO, gasType, txHeight, inputData, TxOutputService.newOutput(), TxInputData.newEmptyInputData(), TxContextService.newTransferContext(instanceId), remarks, 0, minerKey);

        return changeTx;
    }


    public static Tx newTx(TxType txType, long instanceID, long token, String from, String to, BigInteger value, BigInteger gas, long gasType, long txHeight, TxContext txContext, String remarks, long blockCondition, Key senderKey) throws Exception {
        return newTx(txType, instanceID, token, from, to, value, gas, gasType, txHeight, TxInputData.newEmptyInputData(), TxOutputService.newOutput(), TxInputData.newEmptyInputData(), txContext, remarks, blockCondition, senderKey);
    }


    public static Tx newTx(TxType txType, long instanceID, long token, String from, String to, BigInteger value, BigInteger gas, long gasType, long txHeight, TxInputData inputData, TxOutput outputData, TxInputData gasInputData, TxContext txContext, String remarks, long blockCondition, Key senderKey) throws Exception {
        Tx tx = new Tx();


        if (senderKey == null) {
            throw new ChainException("Tx.setSenderSign() -> SenderKey is null.");
        }


        tx.setInstance(instanceID);

        tx.setVersion(ChainConfig.TX_VERSION);

        tx.setStatus(TxStatus.PENDING.value);

        tx.setBlockHeight(0);

        tx.setEventInfo("");

        tx.setTimestamp(CommUtils.currentTimeMillis());

        tx.setToken(token);

        tx.setFrom(from);

        tx.setTo(to);

        tx.setValue(value);

        tx.setGas(gas);

        tx.setGasType(gasType);

        tx.setGasLimit(0);

        tx.setGasInputData(gasInputData);

        tx.setTxHeight(txHeight);

        tx.setTxType(txType.value);

        tx.setContext(txContext.toJson());

        tx.setRemarks(remarks);

        tx.setBlockCondition(blockCondition);

        tx.setHash(tx.encodeHashString());


        tx.setSenderSign(SignVerify.signHexString(tx.toSenderSignString(), senderKey));


        tx.setInputData(inputData);

        tx.setOutput(outputData);

        tx.setChangeTx("");

        tx.setBleedValue(BigInteger.ZERO);

        return tx;
    }


    public static Tx getTx(long instance, String txHash) {
        Tx tx;
        try {
            TxDAO txDAO = new TxDAO(instance);
            tx = txDAO.find(txHash);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return tx;
    }


    public static long getTxToken(long instanceId) {

        Token token = InstanceNppService.getToken(instanceId);

        return token.getId();
    }


    public static void sortByTxHeight(List<Tx> txList) {
        Collections.sort(txList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Tx t1 = (Tx) o1;
                Tx t2 = (Tx) o2;
                int i = t1.getFrom().compareTo(t2.getFrom());
                if (i == 0) {
                    return Long.valueOf(t1.getTxHeight()).compareTo(t2.getTxHeight());
                }
                return i;
            }
        });
    }


    public static void sortByBlockHeight(List<Tx> txList) {
        Collections.sort(txList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Tx t1 = (Tx) o1;
                Tx t2 = (Tx) o2;
                int i = t1.getFrom().compareTo(t2.getFrom());
                if (i == 0) {
                    return Long.valueOf(t1.getBlockHeight()).compareTo(t2.getBlockHeight());
                }
                return i;
            }
        });
    }


    public static Page findByPage(long instanceId, int pageNum, int pageSize, SortEnum sortEnum) {
        PageService<Tx> pageService = new PageService<Tx>(Tx.class, instanceId);
        Page page = pageService.findPage(sortEnum, pageNum, pageSize, new PageCallback() {
            @Override
            public Page gettingData(Page page) {
                try {
                    TxDAO txDAO = new TxDAO(instanceId);
                    TxIndexDAO txIndexDAO = new TxIndexDAO(instanceId);


                    long startId = page.getStartId();
                    long endId = page.getEndId();


                    List<String> keyList = Lists.newArrayList();

                    List<TxIndex> txIndexList = txIndexDAO.gets(startId, endId);

                    List<Tx> dataList = Lists.newArrayList();


                    for (TxIndex txIndex : txIndexList) {
                        if (txIndex != null) {
                            keyList.add(txIndex.getTxHash());
                            Tx tx = txDAO.find(txIndex.getTxHash());
                            dataList.add(tx);
                        } else {
                            keyList.add(null);
                            dataList.add(null);
                        }

                    }

                    keyList.removeAll(Collections.singleton(null));
                    dataList.removeAll(Collections.singleton(null));

                    page.setKeyList(keyList);
                    page.setDataList(dataList);
                } catch (RocksDBException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                return page;
            }
        });

        return page;
    }


    public static Feedback validateTxBasic(final TxBase unvTx) {
        Feedback feedback = FeedbackService.newFeedback();

        try {
            String from = unvTx.getFrom();


            if (unvTx.getVersion() < ChainConfig.TX_VERSION) {
                return feedback.asFail().setMessage(TxCauseMsg.THE_TX_VERSION_IS_TOO_LOW);
            }


            String signerName;


            String signerType = "";
            int txType = unvTx.getTxType();
            if (txType == TxType.TRANSFER_CROSS_IN.value) {
                signerType = "TRANSFER_CROSS_IN";

                Tx tx = (Tx) unvTx;

                TxInputData inputData = tx.getInputData();
                TxInput txInput = inputData.get(0);

                Tx sourceTx = TxService.getTx(txInput.getInstance(), txInput.getTx());

                signerName = SignVerify.getWalletAddress(sourceTx.getMinedSign());
            } else if (TxService.isSystemSender(txType)) {
                signerType = "System Sender";

                signerName = ReflectService.getMinerAddress();
            } else {
                signerType = "From Sender";
                signerName = unvTx.getFrom();
            }
            if (!SignVerify.verifySign(unvTx.getSenderSign(), signerName)) {
                return feedback.asFail().setMessage(TxCauseMsg.VERIFY_SIGN_FAIL + " SignerName=" + signerName + ", unvTx.getSenderSign()=" + SignVerify.getWalletAddress(unvTx.getSenderSign()) + ", validateSignerType=" + signerType);
            }


            if (unvTx.getValue().compareTo(BigInteger.ZERO) == -1) {
                return feedback.asFail().setMessage(TxCauseMsg.TRANSFERS_CANNOT_BE_NEGATIVE);
            }


            if (unvTx.getTxHeight() == 0) {
                return feedback.asFail().setMessage(TxCauseMsg.THE_TX_HEIGHT_CANNOT_BE_ZERO);
            } else {


                long nextTxHeight = AccountTxHeightService.nextTxHeight(from, unvTx.getInstance(), unvTx.getToken());

                if (unvTx.getTxHeight() != nextTxHeight) {

                    if (!ChainConfig.ENABLED_SLACK_AUDIT_TX_HEIGHT) {
                        return feedback.asFail().setMessage(String.format(TxCauseMsg.THE_TX_HEIGHT_CANNOT_BE_WRONG + ". TxHeight must be %d. txHeight=%s", nextTxHeight, unvTx.getTxHeight()));
                    }
                }
            }


            if (TokenService.isNft(unvTx.getToken())) {
                TxContext nftTC = TxService.getTxContext((Tx) unvTx);
                if (nftTC.getEventType() != TxEventType.TOKEN_NFT_MINT && nftTC.getEventType() != TxEventType.TOKEN_NFT_MINTED && nftTC.getEventType() != TxEventType.TOKEN_NFT_TRANSFER) {
                    return feedback.asFail().setMessage(String.format("Protocol error, Not NFT transaction. EventType=%s", nftTC.getEventType()));
                }
            }
        } catch (Exception e) {

            return feedback.asFail().setMessage(String.format(TxCauseMsg.UNKNOWN_EXCEPTION, e.getMessage()));
        }

        return feedback;
    }


    public static Feedback validateTxEnough(final TxBase unvTx) {
        Feedback feedback = FeedbackService.newFeedback();

        try {
            final long instance = unvTx.getInstance();
            final String from = unvTx.getFrom();

            boolean systemSender = isSystemSender(unvTx.getTxType());


            boolean checkEnough = true;

            if (systemSender) {

                checkEnough = false;
            }

            boolean mustCheckEnough = mustCheckEnough(unvTx.getTxType());


            if (checkEnough && !systemSender) {

                if (unvTx.getGas().compareTo(BigInteger.ZERO) != 1) {
                    return feedback.asFail().setMessage(TxCauseMsg.THE_GAS_IS_ZERO);
                } else {


                    BigInteger minGasAmount = TxGasService.calcGasAmount(unvTx.getInstance(), true);

                    if (!unvTx.getFrom().equals(ChainConfig.GENESIS_WALLET_ADDRESS) && unvTx.getGas().compareTo(minGasAmount) == -1) {
                        return feedback.asFail().setMessage(String.format(TxCauseMsg.NOT_ENOUGH_GAS + " txGas=%d, minGas=%d", unvTx.getGas(), minGasAmount));
                    }
                }

                if (unvTx.getToken() != CoreTokenEnum.NAC.id) {

                    Feedback gasFeedback = TxBalanceService.checkGasBalanceEnough(unvTx);
                    if (gasFeedback.isFailed()) {

                        return gasFeedback.setMessage(String.format("%s Sender=%s, Token=%s, Tx=%s", gasFeedback.getMessage(), unvTx.getFrom(), TokenSingleton.get().get(unvTx.getToken()).getSymbol(), unvTx));
                    }
                }
            }


            if (checkEnough || mustCheckEnough) {

                BigInteger usedAmount = unvTx.getValue();

                if (unvTx.getToken() == CoreTokenEnum.NAC.id && unvTx.getGasType() == TxGasType.NAC.value) {
                    usedAmount = usedAmount.add(unvTx.getGas());
                }


                BigInteger balanceAmount = TxBalanceService.balance(instance, unvTx.getToken(), unvTx.getFrom());

                if (balanceAmount.compareTo(usedAmount) == -1) {
                    return feedback.asFail().setMessage(String.format(TxCauseMsg.TOKEN_IS_NOT_ENOUGH + " balanceAmount=%d, usedAmount=%d, unvTx.getInstance()=%d, unvTx.getToken()=%d, unvTx.getFrom()=%s", balanceAmount, usedAmount, unvTx.getInstance(), unvTx.getToken(), unvTx.getFrom()));
                }
            }
        } catch (Exception e) {

            return feedback.asFail().setMessage(String.format(TxCauseMsg.UNKNOWN_EXCEPTION, e.getMessage()));
        }

        return feedback;
    }


    public static Feedback validateTx(final TxBase unvTx) {
        Feedback feedback;


        feedback = validateTxBasic(unvTx);
        if (feedback.isFailed()) {
            return feedback;
        }


        feedback = validateTxEnough(unvTx);
        if (feedback.isFailed()) {
            return feedback;
        }

        return feedback;
    }


    public static boolean excluedFromAddress(String from) {

        final List<String> fromList = Lists.newArrayList(TxReservedWord.GENESIS.name, TxReservedWord.COINBASE.name, TxReservedWord.MINT.name, TxReservedWord.TRANSFER_CROSS_IN.name);
        for (String word : fromList) {
            if (word.equals(from)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isSystemSender(int txType) {
        final List<Integer> txTypes = Lists.newArrayList(TxType.COINBASE.value, TxType.MINT.value, TxType.TRANSFER_EVENT_REFUND.value, TxType.TRANSFER_EVENT_WITHDRAW.value, TxType.TRANSFER_GAS.value, TxType.TRANSFER_CHANGE.value, TxType.TRANSFER_CROSS_IN.value);
        for (int type : txTypes) {
            if (type == txType) {
                return true;
            }
        }
        return false;
    }


    public static boolean isGenesis(String fromAddress) {

        if (fromAddress.equals(TxReservedWord.GENESIS.name())) {
            return true;
        }

        if (fromAddress.equals(ChainConfig.GENESIS_WALLET_ADDRESS)) {
            return true;
        }

        return false;
    }


    public static boolean mustCheckEnough(int txType) {

        final List<Integer> txTypes = Lists.newArrayList(TxType.TRANSFER_EVENT_WITHDRAW.value);
        for (int type : txTypes) {
            if (type == txType) {
                return true;
            }
        }
        return false;
    }


    public static TxMark getTxMark(Tx tx) {
        return getTxContext(tx).getTxMark();
    }


    public static TxContext getTxContext(Tx tx) {
        return TxContextService.toTxContext(tx.getContext());
    }

}
