package org.nachain.core.chain.block;

import java.math.BigInteger;


public class BlockExtraData {


    private String client;


    private String dataCenter;


    private String dappInvoking;


    private BigInteger gasDestroy;


    private BigInteger uninstallAward;


    private BigInteger gasAward;


    private BigInteger bleedValue;


    private long acBlockHeight;


    private long nacBlockHeight;

    @Override
    public String toString() {
        return "BlockExtraData{" +
                "client='" + client + '\'' +
                ", dataCenter='" + dataCenter + '\'' +
                ", dappInvoking='" + dappInvoking + '\'' +
                ", gasDestroy=" + gasDestroy +
                ", uninstallAward=" + uninstallAward +
                ", gasAward=" + gasAward +
                ", bleedValue=" + bleedValue +
                ", acBlockHeight=" + acBlockHeight +
                ", nacBlockHeight=" + nacBlockHeight +
                '}';
    }

    public BlockExtraData() {
        client = "";
        dataCenter = "";
        dappInvoking = "";
        gasDestroy = BigInteger.ZERO;
        uninstallAward = BigInteger.ZERO;
        gasAward = BigInteger.ZERO;
        bleedValue = BigInteger.ZERO;
        acBlockHeight = 0;
        acBlockHeight = 0;
    }

    public String getClient() {
        return client;
    }

    public BlockExtraData setClient(String client) {
        this.client = client;
        return this;
    }

    public String getDataCenter() {
        return dataCenter;
    }

    public BlockExtraData setDataCenter(String dataCenter) {
        this.dataCenter = dataCenter;
        return this;
    }

    public String getDappInvoking() {
        return dappInvoking;
    }

    public BlockExtraData setDappInvoking(String dappInvoking) {
        this.dappInvoking = dappInvoking;
        return this;
    }

    public BigInteger getGasDestroy() {
        return gasDestroy;
    }

    public BlockExtraData setGasDestroy(BigInteger gasDestroy) {
        this.gasDestroy = gasDestroy;
        return this;
    }


    public BigInteger getUninstallAward() {
        return uninstallAward;
    }

    public BlockExtraData setUninstallAward(BigInteger uninstallAward) {
        this.uninstallAward = uninstallAward;
        return this;
    }

    public BigInteger getGasAward() {
        return gasAward;
    }

    public BlockExtraData setGasAward(BigInteger gasAward) {
        this.gasAward = gasAward;
        return this;
    }

    public BigInteger getBleedValue() {
        return bleedValue;
    }

    public BlockExtraData setBleedValue(BigInteger bleedValue) {
        this.bleedValue = bleedValue;
        return this;
    }

    public long getAcBlockHeight() {
        return acBlockHeight;
    }

    public BlockExtraData setAcBlockHeight(long acBlockHeight) {
        this.acBlockHeight = acBlockHeight;
        return this;
    }

    public long getNacBlockHeight() {
        return nacBlockHeight;
    }

    public BlockExtraData setNacBlockHeight(long nacBlockHeight) {
        this.nacBlockHeight = nacBlockHeight;
        return this;
    }
}
