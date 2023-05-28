package org.nachain.core.chain.transaction;

import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;

public class TxInputService {


    public static TxInput toTxInput(String json) {
        return JsonUtils.jsonToPojo(json, TxInput.class);
    }


    public static TxInput newTxInput() {
        return new TxInput().setAmount(BigInteger.ZERO).setInstance(0).setTx("");
    }


    public static TxInput newTxInput(long instance, String tx, BigInteger amount) {
        return new TxInput(instance, tx, amount);
    }

}
