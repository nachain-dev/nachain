package org.nachain.core.chain.transaction;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.base.Amount;
import org.nachain.core.base.Unit;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxContextService;
import org.nachain.core.chain.transaction.creditused.CreditUsed;
import org.nachain.core.chain.transaction.creditused.CreditUsedDAO;
import org.nachain.core.chain.transaction.creditused.CreditUsedService;
import org.nachain.core.chain.transaction.unused.UnusedPool;
import org.nachain.core.chain.transaction.unused.UnusedPoolDAO;
import org.nachain.core.chain.transaction.unused.UnusedPoolService;
import org.nachain.core.chain.transaction.unverified.UnverifiedTxDAO;
import org.nachain.core.chain.transaction.unverified.UnverifiedTxService;
import org.nachain.core.chain.transaction.unverifiedunused.UnverifiedUnusedPool;
import org.nachain.core.chain.transaction.unverifiedunused.UnverifiedUnusedPoolDAO;
import org.nachain.core.chain.transaction.unverifyunused.UnverifyUnusedService;
import org.nachain.core.config.KeyStoreHolder;
import org.nachain.core.config.miner.MinerConfig;
import org.nachain.core.config.miner.Mining;
import org.nachain.core.crypto.Key;
import org.nachain.core.intermediate.AccountTxHeightService;
import org.nachain.core.intermediate.feedback.Feedback;
import org.nachain.core.token.CoreTokenEnum;
import org.nachain.core.token.TokenSingleton;
import org.nachain.core.util.JsonUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TxSendService {


    public static Tx sendTx(final Tx sendTx) throws Exception {
        Tx unvTx;

        log.debug("Transfer objects:" + sendTx);


        TxContext txContext = JsonUtils.jsonToPojo(sendTx.getContext(), TxContext.class);
        long instance = sendTx.getInstance();
        long toInstance = txContext.getCrossToInstance();
        boolean isCross = txContext.getReferrerInstance() != txContext.getCrossToInstance();
        long token = sendTx.getToken();
        String from = sendTx.getFrom();
        String to = sendTx.getTo();
        BigInteger sendValue = sendTx.getValue();
        BigInteger gas = sendTx.getGas();
        long gasType = sendTx.getGasType();
        long txHeight = sendTx.getTxHeight();
        String remarks = sendTx.getRemarks();


        BigInteger usedToken = sendValue;


        Feedback validateFeedback = TxService.validateTx(sendTx);
        if (validateFeedback.isFailed()) {
            throw new ChainException(String.format("[instance=%d] %s Send Token=%s, Tx=%s", sendTx.getInstance(), validateFeedback.getMessage(), sendTx.getFrom(), TokenSingleton.get().get(sendTx.getToken()).getSymbol(), sendTx));
        }


        boolean hasGasInputData = true;

        if (sendTx.getToken() == CoreTokenEnum.NAC.id) {
            if (sendTx.getGasType() == TxGasType.NAC.value) {
                usedToken = usedToken.add(gas);

                hasGasInputData = false;
            }
        }


        UnusedPoolDAO fromUnusedPoolDAO = new UnusedPoolDAO(instance);

        UnverifiedTxDAO fromUnverifiedTxDAO = new UnverifiedTxDAO(instance);

        UnverifiedUnusedPoolDAO fromUnverifyUnusedPoolDAO = new UnverifiedUnusedPoolDAO(instance);

        TxDAO fromTxDAO = new TxDAO(instance);

        CreditUsedDAO creditUsedDAO = new CreditUsedDAO(instance);


        UnusedPool unusedPool = fromUnusedPoolDAO.find(from);

        List<Tx> unusedTxList;
        if (unusedPool != null) {
            unusedTxList = fromTxDAO.findList(token, unusedPool.getUnusedTxs());
        } else {
            unusedTxList = new ArrayList<>();
        }


        UnverifiedUnusedPool unverifyUnusedPool = fromUnverifyUnusedPoolDAO.find(from);
        List<Tx> unverifyUnusedList;
        if (unverifyUnusedPool != null) {
            unverifyUnusedList = fromUnverifiedTxDAO.findList(token, unverifyUnusedPool.getUnusedTxs());
        } else {
            unverifyUnusedList = new ArrayList<>();
        }

        unusedTxList.addAll(unverifyUnusedList);


        if (txContext.getReferrerInstance() != txContext.getCrossToInstance()) {

        } else {

            if (unusedTxList.size() == 0) {
                throw new ChainException("UnusedPool is empty, and the UnverifyUnused is empty");
            }
        }


        if (txHeight == 0) {
            throw new ChainException("TxHeight is 0");
        }


        Tx checkTx = fromTxDAO.find(sendTx.getHash());
        Tx checkUnTx = fromUnverifiedTxDAO.find(sendTx.getHash());
        if (checkTx != null || checkUnTx != null) {
            throw new ChainException("Don't submit duplicate transactions. Duplicate hash %s", sendTx.getHash());
        }

        log.debug("UnusedTx list size:" + unusedTxList.size() + ", include UnverifyUnused size:" + unverifyUnusedList.size());


        TxSmartMatching tsm = TxSmartMatchingService.txSmartMatching(unusedTxList, usedToken);


        if (!tsm.isEnough()) {

            throw new ChainException("[instance=%d] The balance is not enough, There are no input transactions available, so no transfer is possible (ToInstance:" + toInstance + ",To:" + to + ",tokenValue=" + sendValue + ",gas=" + gas + ",usedToken=" + usedToken + ",Tsm InputTxValue=" + tsm.getInputTxValue() + ",Tsm ChangeValue:" + tsm.getChangeValue() + ",TokenValue:" + sendValue + ", Tx=" + sendTx + ")", instance);
        }


        List<Tx> inputPayTxList = tsm.getInputTxList();
        TxInputData inputData = TxInputData.newEmptyInputData();
        for (Tx inputTx : inputPayTxList) {

            inputData.add(TxInputService.newTxInput(inputTx.getInstance(), inputTx.getHash(), inputTx.getValue()));
        }


        TxInputData gasInputData = TxInputData.newEmptyInputData();

        if (hasGasInputData && sendTx.getTxType() != TxType.TRANSFER_GAS.value) {

            Key minerKey = Mining.getKey();

            long gasTxHeight = AccountTxHeightService.nextTxHeight(from, instance, sendTx.getGasType());

            Tx gasTx = TxService.newGasTx(instance, from, sendTx.getHash(), sendTx.getGasType(), sendTx.getGas(), gasTxHeight, minerKey);

            TxSendService.sendTx(gasTx);

            gasInputData.add(TxInputService.newTxInput(gasTx.getInstance(), gasTx.getHash(), gasTx.getValue()));
        }


        unvTx = sendTx;
        unvTx.setInputData(inputData);
        unvTx.setGasInputData(gasInputData);


        if (tsm.getChangeValue().compareTo(BigInteger.ZERO) == 1) {

            Tx needChangeTx = tsm.getChangeTx();
            if (needChangeTx != null) {

                Key minerKey = Mining.getKey();

                Tx changeTx = TxService.newChangeTx(instance, token, from, unvTx.getHash(), tsm.getChangeValue(), gasType, txHeight, inputData, minerKey);

                UnverifiedTxService.saveUnverifiedAndBroadcast(changeTx);

                unvTx.setChangeTx(changeTx.getHash());
            }
        }


        for (Tx inputTx : inputPayTxList) {

            boolean txUsedExist = UnusedPoolService.delUsed(inputTx);


            if (!txUsedExist) {

                boolean txUnverifyUsedExist = UnverifyUnusedService.delUnverifyUsed(inputTx);


                if (txUnverifyUsedExist) {

                    CreditUsed creditUsed = CreditUsedService.newCreditUsed();
                    creditUsed.setCreditSourceTx(inputTx.getHash());
                    creditUsed.setToken(token);
                    creditUsed.setFromInstance(instance);
                    creditUsed.setToInstance(toInstance);
                    creditUsed.setTo(to);
                    creditUsed.setTokenValue(usedToken);

                    creditUsedDAO.add(creditUsed);
                }
            }
        }


        UnverifiedTxService.saveUnverifiedAndBroadcast(unvTx);

        return unvTx;
    }


    public static Key getSendKey(String senderWallet) {
        Key sendKey = KeyStoreHolder.getKey(senderWallet);
        if (sendKey == null) {
            if (senderWallet.equals(MinerConfig.WALLET_ADDRESS)) {
                sendKey = MinerConfig.MINER_KEY;
            }
        }
        if (sendKey == null) {
            throw new ChainException("Find not found %s in Key Store.", senderWallet);
        }

        return sendKey;
    }


    public static Tx sendSameChain(long fromInstanceId, long tokenId, String fromWallet, String toWallet, long sendValue) throws Exception {
        return sendSameChain(fromInstanceId, tokenId, fromWallet, toWallet, Amount.of(sendValue, Unit.NAC).toBigInteger());
    }


    public static Tx sendSameChain(long fromInstanceId, long tokenId, String fromWallet, String toWallet, double sendValue) throws Exception {
        return sendSameChain(fromInstanceId, tokenId, fromWallet, toWallet, Amount.of(BigDecimal.valueOf(sendValue), Unit.NAC).toBigInteger());
    }


    public static Tx sendSameChain(long fromInstanceId, long token, String fromWallet, String toWallet, BigInteger sendValue) throws Exception {

        long txHeight = AccountTxHeightService.nextTxHeight(fromWallet, fromInstanceId, token);

        return sendSameChain(fromInstanceId, token, fromWallet, toWallet, sendValue, txHeight, TxContextService.newTransferContext(fromInstanceId));
    }


    public static Tx sendSameChain(long fromInstanceId, long token, String fromWallet, String toWallet, BigInteger sendValue, long txHeight, TxContext txContext) throws Exception {

        Key sendKey = getSendKey(fromWallet);


        BigInteger gas = TxGasService.calcGasAmount(fromInstanceId);


        Tx sendTx = TxService.newTx(TxType.TRANSFER,
                fromInstanceId, token, fromWallet, toWallet, sendValue,
                gas, TxGasType.NAC.value, txHeight, txContext,
                "", 0, sendKey);


        return TxSendService.sendTx(sendTx);
    }


    public static Tx sendCrossChain(long fromInstanceId, long toInstanceId, long tokenId, String fromWallet, long sendValue) throws Exception {
        return sendCrossChain(fromInstanceId, toInstanceId, tokenId, fromWallet, Amount.of(sendValue, Unit.NAC).toBigInteger());
    }


    public static Tx sendCrossChain(long fromInstanceId, long toInstanceId, long tokenId, String fromWallet, double sendValue) throws Exception {
        return sendCrossChain(fromInstanceId, toInstanceId, tokenId, fromWallet, Amount.of(BigDecimal.valueOf(sendValue), Unit.NAC).toBigInteger());
    }


    public static Tx sendCrossChain(long fromInstanceId, long toInstanceId, long tokenId, String fromWallet, BigInteger sendValue) throws Exception {

        Key sendKey = getSendKey(fromWallet);


        long gasType = TxGasType.NAC.value;


        BigInteger gas = TxGasService.calcGasAmount(fromInstanceId);


        long txHeight = AccountTxHeightService.nextTxHeight(fromWallet, fromInstanceId, tokenId);


        TxContext txContext = TxContextService.newTransferContext(fromInstanceId);
        txContext.setReferrerInstance(fromInstanceId);
        txContext.setCrossToInstance(toInstanceId);


        Tx sendTx = TxCrossService.newCrossOutTx(fromInstanceId, toInstanceId, tokenId, fromWallet, sendValue, gas, gasType, txHeight, txContext, sendKey);


        return TxSendService.sendTx(sendTx);
    }

}
