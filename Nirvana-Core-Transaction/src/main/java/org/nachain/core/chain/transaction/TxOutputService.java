package org.nachain.core.chain.transaction;

import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;

public class TxOutputService {


    public static TxOutput toTxOutput(String json) {
        return JsonUtils.jsonToPojo(json, TxOutput.class);
    }


    public static TxOutput newOutput() {
        return new TxOutput().setAmount(BigInteger.ZERO).setTargetTx("").setInstance(0);
    }


    public static TxOutput newTxOutput(long instance, String txHash, BigInteger amount) {
        return new TxOutput(instance, txHash, amount);
    }

}
