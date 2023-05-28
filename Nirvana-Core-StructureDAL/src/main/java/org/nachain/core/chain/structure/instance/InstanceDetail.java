package org.nachain.core.chain.structure.instance;

import java.math.BigInteger;


public class InstanceDetail {


    private long id;


    private long blockHeight;


    private BigInteger txHeight;


    private BigInteger coinbase;


    private BigInteger gasUsed;


    private BigInteger gas;


    private BigInteger gasDestroy;


    private BigInteger uninstallAward;


    private BigInteger gasAward;


    private BigInteger destroyNac;


    private BigInteger bleedValue;


    private long markBlockHeight;


    private long markTxDasHeight;


    private long timestamp;

    public long getId() {
        return id;
    }

    public InstanceDetail setId(long id) {
        this.id = id;
        return this;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public InstanceDetail setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public BigInteger getTxHeight() {
        return txHeight;
    }

    public InstanceDetail setTxHeight(BigInteger txHeight) {
        this.txHeight = txHeight;
        return this;
    }

    public BigInteger getDestroyNac() {
        return destroyNac;
    }

    public InstanceDetail setDestroyNac(BigInteger destroyNac) {
        this.destroyNac = destroyNac;
        return this;
    }

    public BigInteger getGasUsed() {
        return gasUsed;
    }

    public InstanceDetail setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
        return this;
    }

    public long getMarkBlockHeight() {
        return markBlockHeight;
    }

    public InstanceDetail setMarkBlockHeight(long markBlockHeight) {
        this.markBlockHeight = markBlockHeight;
        return this;
    }

    public long getMarkTxDasHeight() {
        return markTxDasHeight;
    }

    public InstanceDetail setMarkTxDasHeight(long markTxDasHeight) {
        this.markTxDasHeight = markTxDasHeight;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public InstanceDetail setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public BigInteger getCoinbase() {
        return coinbase;
    }

    public InstanceDetail setCoinbase(BigInteger coinbase) {
        this.coinbase = coinbase;
        return this;
    }

    public BigInteger getGasDestroy() {
        return gasDestroy;
    }

    public InstanceDetail setGasDestroy(BigInteger gasDestroy) {
        this.gasDestroy = gasDestroy;
        return this;
    }

    public BigInteger getUninstallAward() {
        return uninstallAward;
    }

    public InstanceDetail setUninstallAward(BigInteger uninstallAward) {
        this.uninstallAward = uninstallAward;
        return this;
    }

    public BigInteger getGasAward() {
        return gasAward;
    }

    public InstanceDetail setGasAward(BigInteger gasAward) {
        this.gasAward = gasAward;
        return this;
    }

    public BigInteger getGas() {
        return gas;
    }

    public InstanceDetail setGas(BigInteger gas) {
        this.gas = gas;
        return this;
    }

    public BigInteger getBleedValue() {
        return bleedValue;
    }

    public InstanceDetail setBleedValue(BigInteger bleedValue) {
        this.bleedValue = bleedValue;
        return this;
    }

    @Override
    public String toString() {
        return "InstanceDetail{" +
                "id=" + id +
                ", blockHeight=" + blockHeight +
                ", txHeight=" + txHeight +
                ", coinbase=" + coinbase +
                ", gasUsed=" + gasUsed +
                ", gas=" + gas +
                ", gasDestroy=" + gasDestroy +
                ", uninstallAward=" + uninstallAward +
                ", gasAward=" + gasAward +
                ", destroyNac=" + destroyNac +
                ", bleedValue=" + bleedValue +
                ", markBlockHeight=" + markBlockHeight +
                ", markTxDasHeight=" + markTxDasHeight +
                ", timestamp=" + timestamp +
                '}';
    }


    public InstanceDetail addCoinbase(BigInteger amount) {
        coinbase = coinbase.add(amount);
        return this;
    }


    public InstanceDetail addGasUsed(BigInteger amount) {
        gasUsed = gasUsed.add(amount);
        return this;
    }


    public InstanceDetail addGas(BigInteger amount) {
        gas = gas.add(amount);
        return this;
    }


    public InstanceDetail addGasDestroy(BigInteger amount) {
        gasDestroy = gasDestroy.add(amount);
        return this;
    }


    public InstanceDetail addUninstallAward(BigInteger amount) {
        uninstallAward = uninstallAward.add(amount);
        return this;
    }


    public InstanceDetail addGasAward(BigInteger amount) {
        gasAward = gasAward.add(amount);
        return this;
    }


    public InstanceDetail addDestroyNac(BigInteger amount) {
        destroyNac = destroyNac.add(amount);
        return this;
    }


    public InstanceDetail addBleedValue(BigInteger amount) {
        bleedValue = bleedValue.add(amount);
        return this;
    }


}

