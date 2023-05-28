package org.nachain.core.token.nft.collection;


import java.math.BigInteger;


public class NftSell {


    protected long instance;


    protected long token;


    protected long tokenId;


    private BigInteger salePrice;

    public long getInstance() {
        return instance;
    }

    public NftSell setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getToken() {
        return token;
    }

    public NftSell setToken(long token) {
        this.token = token;
        return this;
    }

    public long getTokenId() {
        return tokenId;
    }

    public NftSell setTokenId(long tokenId) {
        this.tokenId = tokenId;
        return this;
    }

    public BigInteger getSalePrice() {
        return salePrice;
    }

    public NftSell setSalePrice(BigInteger salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    @Override
    public String toString() {
        return "NftSell{" +
                "instance=" + instance +
                ", token=" + token +
                ", tokenId=" + tokenId +
                ", salePrice=" + salePrice +
                '}';
    }
}
