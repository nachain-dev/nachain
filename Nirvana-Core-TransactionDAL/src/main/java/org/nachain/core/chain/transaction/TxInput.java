package org.nachain.core.chain.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


public class TxInput {

    private long instance;


    private String tx;


    private BigInteger amount;

    public TxInput() {
    }

    public TxInput(long instance, String tx, BigInteger amount) {
        this.instance = instance;
        this.tx = tx;
        this.amount = amount;
    }

    public long getInstance() {
        return instance;
    }

    public TxInput setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public String getTx() {
        return tx;
    }

    public TxInput setTx(String tx) {
        this.tx = tx;
        return this;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public TxInput setAmount(BigInteger amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public String toString() {
        return "TxInput{" +
                "instance=" + instance +
                ", tx='" + tx + '\'' +
                ", amount=" + amount +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }
}
