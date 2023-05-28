package org.nachain.core.chain.transaction;

import java.math.BigInteger;
import java.util.ArrayList;


public class TxSmartMatching {


    private ArrayList<Tx> inputTxList;


    private BigInteger inputTxValue;


    private Tx changeTx;


    private BigInteger changeValue;


    private BigInteger changeTxUsed;


    private boolean enough;

    public TxSmartMatching() {
        inputTxValue = BigInteger.ZERO;
        changeValue = BigInteger.ZERO;
        changeTxUsed = BigInteger.ZERO;
    }

    public ArrayList<Tx> getInputTxList() {
        return inputTxList;
    }

    public TxSmartMatching setInputTxList(ArrayList<Tx> inputTxList) {
        this.inputTxList = inputTxList;
        return this;
    }

    public BigInteger getInputTxValue() {
        return inputTxValue;
    }

    public TxSmartMatching setInputTxValue(BigInteger inputTxValue) {
        this.inputTxValue = inputTxValue;
        return this;
    }

    public Tx getChangeTx() {
        return changeTx;
    }

    public TxSmartMatching setChangeTx(Tx changeTx) {
        this.changeTx = changeTx;
        return this;
    }

    public BigInteger getChangeValue() {
        return changeValue;
    }

    public TxSmartMatching setChangeValue(BigInteger changeValue) {
        this.changeValue = changeValue;
        return this;
    }

    public BigInteger getChangeTxUsed() {
        return changeTxUsed;
    }

    public TxSmartMatching setChangeTxUsed(BigInteger changeTxUsed) {
        this.changeTxUsed = changeTxUsed;
        return this;
    }

    public boolean isEnough() {
        return enough;
    }

    public TxSmartMatching setEnough(boolean enough) {
        this.enough = enough;
        return this;
    }
}


