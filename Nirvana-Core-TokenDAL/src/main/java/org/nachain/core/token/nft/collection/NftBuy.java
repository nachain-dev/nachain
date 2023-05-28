package org.nachain.core.token.nft.collection;

import java.math.BigInteger;


public class NftBuy {


    protected long instance;


    protected long token;


    protected long tokenId;


    private BigInteger buyPrice;

    public long getInstance() {
        return instance;
    }

    public NftBuy setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getToken() {
        return token;
    }

    public NftBuy setToken(long token) {
        this.token = token;
        return this;
    }

    public long getTokenId() {
        return tokenId;
    }

    public NftBuy setTokenId(long tokenId) {
        this.tokenId = tokenId;
        return this;
    }

    public BigInteger getBuyPrice() {
        return buyPrice;
    }

    public NftBuy setBuyPrice(BigInteger buyPrice) {
        this.buyPrice = buyPrice;
        return this;
    }

    @Override
    public String toString() {
        return "NftBuy{" +
                "instance=" + instance +
                ", token=" + token +
                ", tokenId=" + tokenId +
                ", buyPrice=" + buyPrice +
                '}';
    }
}
