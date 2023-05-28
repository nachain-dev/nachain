package org.nachain.core.dapp.internal.supernode;

import java.math.BigInteger;


public class SuperNode {


    long instance;


    private String nominateAddress;


    private BigInteger amountTotal;


    private long checkInBlockHeight;


    private boolean worker;

    public long getInstance() {
        return instance;
    }

    public SuperNode setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public String getNominateAddress() {
        return nominateAddress;
    }

    public SuperNode setNominateAddress(String nominateAddress) {
        this.nominateAddress = nominateAddress;
        return this;
    }

    public BigInteger getAmountTotal() {
        return amountTotal;
    }

    public SuperNode setAmountTotal(BigInteger amountTotal) {
        this.amountTotal = amountTotal;
        return this;
    }

    public long getCheckInBlockHeight() {
        return checkInBlockHeight;
    }

    public SuperNode setCheckInBlockHeight(long checkInBlockHeight) {
        this.checkInBlockHeight = checkInBlockHeight;
        return this;
    }

    public boolean isWorker() {
        return worker;
    }

    public SuperNode setWorker(boolean worker) {
        this.worker = worker;
        return this;
    }

    @Override
    public String toString() {
        return "SuperNode{" +
                "instance=" + instance +
                ", nominateAddress='" + nominateAddress + '\'' +
                ", amountTotal=" + amountTotal +
                ", checkInBlockHeight=" + checkInBlockHeight +
                ", worker=" + worker +
                '}';
    }


    public BigInteger addAmount(BigInteger amount) {
        amountTotal = amountTotal.add(amount);
        return amountTotal;
    }


    public BigInteger subtractAmount(BigInteger amount) {
        amountTotal = amountTotal.subtract(amount);
        return amountTotal;
    }
}
