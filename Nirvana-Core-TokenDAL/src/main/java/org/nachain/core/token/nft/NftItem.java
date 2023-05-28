package org.nachain.core.token.nft;

import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


public class NftItem {


    long instance;


    long token;


    long nftItemId;


    boolean isSale;


    BigInteger salePrice;


    String owner;


    String fromTx;


    String mintTx;

    public long getInstance() {
        return instance;
    }

    public NftItem setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getToken() {
        return token;
    }

    public NftItem setToken(long token) {
        this.token = token;
        return this;
    }

    public long getNftItemId() {
        return nftItemId;
    }

    public NftItem setNftItemId(long nftItemId) {
        this.nftItemId = nftItemId;
        return this;
    }


    public boolean isSale() {
        return isSale;
    }

    public NftItem setSale(boolean sale) {
        isSale = sale;
        return this;
    }

    public BigInteger getSalePrice() {
        return salePrice;
    }

    public NftItem setSalePrice(BigInteger salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public NftItem setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getFromTx() {
        return fromTx;
    }

    public NftItem setFromTx(String fromTx) {
        this.fromTx = fromTx;
        return this;
    }

    public String getMintTx() {
        return mintTx;
    }

    public void setMintTx(String mintTx) {
        this.mintTx = mintTx;
    }

    @Override
    public String toString() {
        return "NftItem{" +
                "instance=" + instance +
                ", token=" + token +
                ", nftItemId=" + nftItemId +
                ", isSale=" + isSale +
                ", salePrice=" + salePrice +
                ", owner='" + owner + '\'' +
                ", fromTx='" + fromTx + '\'' +
                ", mintTx='" + mintTx + '\'' +
                '}';
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }
}
