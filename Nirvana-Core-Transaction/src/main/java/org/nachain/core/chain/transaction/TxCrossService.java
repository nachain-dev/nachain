package org.nachain.core.chain.transaction;

import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxContextService;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.crypto.Key;
import org.nachain.core.intermediate.AccountTxHeightService;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;

public class TxCrossService {


    public static Tx newCrossOutTx(long fromInstance, long toInstance, long token, String from, BigInteger value, BigInteger gas, long gasType, long txHeight, TxContext txContext, Key key) throws Exception {

        TxInputData inputData = TxInputData.newEmptyInputData();
        TxOutput outputData = TxOutputService.newOutput();
        TxInputData gasInputData = TxInputData.newEmptyInputData();

        return newCrossOutTx(fromInstance, toInstance, token, from, value, gas, gasType, txHeight, inputData, outputData, gasInputData, txContext.getData(), key);
    }


    public static Tx newCrossOutTx(long fromInstance, long toInstance, long token, String from, BigInteger value, BigInteger gas, long gasType, long txHeight, TxInputData inputData, TxOutput outputData, TxInputData gasInputData, Object contextData, Key key) throws Exception {


        TxType txType = TxType.TRANSFER_CROSS_OUT;


        TxEventType eventType = TxEventType.TOKEN_TRANSFER;


        TxContext txContext = TxContextService.newCrossOutContext(eventType, contextData);
        txContext.setReferrerInstance(fromInstance).setReferrerTx("");
        txContext.setCrossToInstance(toInstance);


        String to = TxReservedWord.TRANSFER_CROSS_OUT.name;


        Tx tx = TxService.newTx(txType, fromInstance, token, from, to, value, gas, gasType, txHeight, inputData, outputData, gasInputData, txContext, from + " to " + to, 0, key);

        return tx;
    }


    public static Tx newCrossInTx(Tx crossOutTx, long toInstance, Key senderKey) throws Exception {

        long token = crossOutTx.getToken();


        TxInputData inputData = TxInputData.newEmptyInputData();
        inputData.add(TxInputService.newTxInput(crossOutTx.getInstance(), crossOutTx.getHash(), crossOutTx.getValue()));


        TxInputData gasInputData = TxInputData.newEmptyInputData();


        TxOutput outputData = TxOutputService.newOutput();


        long referrerInstance = crossOutTx.getInstance();

        String referrerTxHash = crossOutTx.getHash();

        Object contextData = TxContextService.toTxContext(crossOutTx.getContext()).getData();

        return newCrossInTx(referrerInstance, referrerTxHash, toInstance, token, crossOutTx.getFrom(), crossOutTx.getValue(), BigInteger.ZERO, crossOutTx.getGasType(), inputData, outputData, gasInputData, contextData, senderKey);
    }


    public static Tx newCrossInTx(long referrerInstance, String referrerTxHash,
                                  long toInstance, long token, String to, BigInteger tokenValue, BigInteger gas, long gasType, TxInputData inputData, TxOutput outputData, TxInputData gasInputData, Object
                                          contextData, Key senderKey) throws Exception {

        TxType txType = TxType.TRANSFER_CROSS_IN;


        TxEventType eventType = TxEventType.TOKEN_TRANSFER;


        TxContext txContext = TxContextService.newCrossInContext(referrerInstance, referrerTxHash, toInstance, eventType, contextData);


        String from = TxReservedWord.TRANSFER_CROSS_IN.name;


        long txHeight = AccountTxHeightService.nextTxHeight(to, toInstance, token);


        Tx tx = TxService.newTx(txType, toInstance, token, from, to, tokenValue, gas, gasType, txHeight, inputData, outputData, gasInputData, txContext, TxReservedWord.TRANSFER_CROSS_IN.name, 0, senderKey);

        return tx;
    }


    public static boolean isCrossTx(TxBase txBase) {
        TxContext txContext = JsonUtils.jsonToPojo(txBase.getContext(), TxContext.class);
        return txContext.getReferrerInstance() != txContext.getCrossToInstance();
    }

}
