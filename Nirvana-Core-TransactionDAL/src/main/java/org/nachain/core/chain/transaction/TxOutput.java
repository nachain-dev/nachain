package org.nachain.core.chain.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


public class TxOutput {

    private long instance;


    private String targetTx;


    private BigInteger amount;

    public TxOutput() {
    }

    public TxOutput(long instance, String targetTx, BigInteger amount) {
        this.instance = instance;
        this.targetTx = targetTx;
        this.amount = amount;
    }

    public long getInstance() {
        return instance;
    }

    public TxOutput setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public String getTargetTx() {
        return targetTx;
    }

    public TxOutput setTargetTx(String targetTx) {
        this.targetTx = targetTx;
        return this;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public TxOutput setAmount(BigInteger amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public String toString() {
        return "TxOutput{" +
                "instance=" + instance +
                ", targetTx='" + targetTx + '\'' +
                ", amount=" + amount +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }

}
