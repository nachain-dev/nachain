package org.nachain.core.chain.instanceprofile;

import java.math.BigInteger;


public class InstanceProfile {


    private long instance;


    private long token;


    private String address;


    private BigInteger balance;


    private BigInteger historyIn;


    private BigInteger historyOut;


    private long blockHeight;


    private long timestamp;

    public long getInstance() {
        return instance;
    }

    public InstanceProfile setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getToken() {
        return token;
    }

    public InstanceProfile setToken(long token) {
        this.token = token;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public InstanceProfile setAddress(String address) {
        this.address = address;
        return this;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public InstanceProfile setBalance(BigInteger balance) {
        this.balance = balance;
        return this;
    }

    public BigInteger getHistoryIn() {
        return historyIn;
    }

    public InstanceProfile setHistoryIn(BigInteger historyIn) {
        this.historyIn = historyIn;
        return this;
    }

    public BigInteger getHistoryOut() {
        return historyOut;
    }

    public InstanceProfile setHistoryOut(BigInteger historyOut) {
        this.historyOut = historyOut;
        return this;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public InstanceProfile setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public String toString() {
        return "InstanceProfile{" +
                "instance=" + instance +
                ", token=" + token +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                ", historyIn=" + historyIn +
                ", historyOut=" + historyOut +
                ", blockHeight=" + blockHeight +
                ", timestamp=" + timestamp +
                '}';
    }
}
