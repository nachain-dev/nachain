package org.nachain.core.chain.das;

import java.math.BigInteger;


public class DasProfile {


    private long instance;


    private long token;


    private String address;


    private BigInteger balance;


    private BigInteger historyIn;


    private BigInteger historyOut;


    private BigInteger historyGas;


    private long txHeight;


    private long timestamp;

    public long getInstance() {
        return instance;
    }

    public DasProfile setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getToken() {
        return token;
    }

    public DasProfile setToken(long token) {
        this.token = token;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public DasProfile setAddress(String address) {
        this.address = address;
        return this;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public DasProfile setBalance(BigInteger balance) {
        this.balance = balance;
        return this;
    }

    public BigInteger getHistoryIn() {
        return historyIn;
    }

    public DasProfile setHistoryIn(BigInteger historyIn) {
        this.historyIn = historyIn;
        return this;
    }

    public BigInteger getHistoryOut() {
        return historyOut;
    }

    public DasProfile setHistoryOut(BigInteger historyOut) {
        this.historyOut = historyOut;
        return this;
    }

    public BigInteger getHistoryGas() {
        return historyGas;
    }

    public DasProfile setHistoryGas(BigInteger historyGas) {
        this.historyGas = historyGas;
        return this;
    }

    public long getTxHeight() {
        return txHeight;
    }

    public DasProfile setTxHeight(long txHeight) {
        this.txHeight = txHeight;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public DasProfile setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public String toString() {
        return "DasProfile{" +
                "instance=" + instance +
                ", token=" + token +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                ", historyIn=" + historyIn +
                ", historyOut=" + historyOut +
                ", historyGas=" + historyGas +
                ", txHeight=" + txHeight +
                ", timestamp=" + timestamp +
                '}';
    }
}
