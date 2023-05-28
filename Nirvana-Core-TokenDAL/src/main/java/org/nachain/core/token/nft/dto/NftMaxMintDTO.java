package org.nachain.core.token.nft.dto;


public class NftMaxMintDTO {


    long instance;


    long token;


    long maxMintAmount;

    public long getInstance() {
        return instance;
    }

    public void setInstance(long instance) {
        this.instance = instance;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public long getMaxMintAmount() {
        return maxMintAmount;
    }

    public void setMaxMintAmount(long maxMintAmount) {
        this.maxMintAmount = maxMintAmount;
    }

    @Override
    public String toString() {
        return "NftMaxMintDTO{" +
                "instance=" + instance +
                ", token=" + token +
                ", maxMintAmount=" + maxMintAmount +
                '}';
    }
}
