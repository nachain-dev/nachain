package org.nachain.core.token.nft.dto;


public class NftMintedDTO {


    long instance;


    long token;


    long nftItemId;


    String owner;


    String fromTx;

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

    public long getNftItemId() {
        return nftItemId;
    }

    public void setNftItemId(long nftItemId) {
        this.nftItemId = nftItemId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFromTx() {
        return fromTx;
    }

    public void setFromTx(String fromTx) {
        this.fromTx = fromTx;
    }

    @Override
    public String toString() {
        return "NftMintedDTO{" +
                "instance=" + instance +
                ", token=" + token +
                ", nftItemId=" + nftItemId +
                ", owner='" + owner + '\'' +
                ", fromTx='" + fromTx + '\'' +
                '}';
    }
}
