package org.nachain.core.token.nft.dto;


public class NftMintDTO {


    long instance;


    long token;


    long mintAmount;

    public long getInstance() {
        return instance;
    }

    public NftMintDTO setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getToken() {
        return token;
    }

    public NftMintDTO setToken(long token) {
        this.token = token;
        return this;
    }

    public long getMintAmount() {
        return mintAmount;
    }

    public NftMintDTO setMintAmount(long mintAmount) {
        this.mintAmount = mintAmount;
        return this;
    }

    @Override
    public String toString() {
        return "NftMintDTO{" +
                "instance=" + instance +
                ", token=" + token +
                ", mintAmount=" + mintAmount +
                '}';
    }
}
