package org.nachain.core.chain.das;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.das.unverified.UnverifiedTxDasDAO;
import org.nachain.core.chain.das.unverified.UnverifiedTxDasService;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.transaction.*;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxContextService;
import org.nachain.core.config.ChainConfig;
import org.nachain.core.crypto.Key;
import org.nachain.core.intermediate.AccountTxDasHeightService;
import org.nachain.core.signverify.SignVerify;
import org.nachain.core.util.CommUtils;

import java.math.BigInteger;


@Slf4j
public class TxDasService {


    public static TxDas newTxDas(TxType txType, long instanceID, long token, String from, String to, BigInteger value, BigInteger gas, long gasType, long txHeight, TxInputData inputData, TxInputData gasInputData, TxContext txContext, String remarks, long blockCondition, long schedule, Key senderKey) throws Exception {
        if (senderKey == null) {
            throw new ChainException("tx.setSenderSign() -> senderKey is null");
        }

        TxDas tx = new TxDas();

        tx.setSchedule(schedule);

        tx.setInstance(instanceID);

        tx.setVersion(ChainConfig.TX_VERSION);

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

        tx.setStatus(TxStatus.PENDING.value);

        tx.setBleedValue(BigInteger.ZERO);

        return tx;
    }


    public static TxDas newEventRefundTxDas(TxDas failedTxDas, Key senderKey) throws Exception {

        TxInputData inputData = TxInputData.newInputData(TxInputService.newTxInput(failedTxDas.getInstance(), failedTxDas.getHash(), failedTxDas.getValue()));

        long txHeight = AccountTxDasHeightService.nextTxHeight(TxReservedWord.TRANSFER_EVENT_REFUND.name, failedTxDas.getInstance(), failedTxDas.getToken());

        TxDas eventRefundTx = TxDasService.newTxDas(TxType.TRANSFER_EVENT_REFUND, failedTxDas.getInstance(), failedTxDas.getToken(), failedTxDas.getTo(), failedTxDas.getFrom(), failedTxDas.getValue(), BigInteger.ZERO, failedTxDas.getGasType(), txHeight, inputData, TxInputData.newEmptyInputData(), TxContextService.newTransferContext(failedTxDas.getInstance()), TxReservedWord.TRANSFER_EVENT_REFUND.name, 0, 0, senderKey);

        return eventRefundTx;
    }


    public static boolean sendTxDas(TxDas sendTx) throws Exception {


        UnverifiedTxDasService.saveToUnverifiedAndBroadcast(sendTx);

        return true;
    }


    public static void solidify(TxDas txDas, Key minerKey) throws Exception {
        long instance = txDas.getInstance();
        TxDasDAO txDasDAO = new TxDasDAO(instance);
        UnverifiedTxDasDAO unverifiedTxDasDAO = new UnverifiedTxDasDAO(instance);


        if (txDasDAO.get(txDas.getHash()) != null) {
            throw new ChainException("TxDas [" + txDas.getHash() + "] already exists and cannot be created");
        }


        TxDas tx = unverifiedTxDasDAO.find(txDas.getHash());
        if (tx != null) {

            UnverifiedTxDasService.unverifiedToMined(tx, minerKey);
        } else {
            throw new ChainException("UnverifiedTxDas [" + txDas.getHash() + "] Not available");
        }

    }
}
