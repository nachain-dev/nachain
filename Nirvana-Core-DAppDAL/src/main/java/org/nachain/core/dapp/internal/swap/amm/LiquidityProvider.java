package org.nachain.core.dapp.internal.swap.amm;

import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


public class LiquidityProvider {


    protected String pairName;


    protected String providerAddress;


    protected BigInteger baseAmount;


    protected BigInteger quoteAmount;


    protected long timestamp;


    protected String hash;

    public String getPairName() {
        return pairName;
    }

    public LiquidityProvider setPairName(String pairName) {
        this.pairName = pairName;
        return this;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    public LiquidityProvider setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
        return this;
    }

    public BigInteger getBaseAmount() {
        return baseAmount;
    }

    public LiquidityProvider setBaseAmount(BigInteger baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    public BigInteger getQuoteAmount() {
        return quoteAmount;
    }

    public LiquidityProvider setQuoteAmount(BigInteger quoteAmount) {
        this.quoteAmount = quoteAmount;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public LiquidityProvider setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public LiquidityProvider setHash(String hash) {
        this.hash = hash;
        return this;
    }

    @Override
    public String toString() {
        return "LiquidityProvider{" +
                "pairName='" + pairName + '\'' +
                ", providerAddress='" + providerAddress + '\'' +
                ", baseAmount=" + baseAmount +
                ", quoteAmount=" + quoteAmount +
                ", timestamp=" + timestamp +
                ", hash='" + hash + '\'' +
                '}';
    }

    public String toHashString() {
        return "LiquidityProvider{" +
                "pairName='" + pairName + '\'' +
                ", providerAddress='" + providerAddress + '\'' +
                ", baseAmount=" + baseAmount +
                ", quoteAmount=" + quoteAmount +
                ", timestamp=" + timestamp +
                '}';
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }


    public byte[] encodeHash() {

        return Hash.h256(this.toHashString().getBytes());
    }


    public String encodeHashString() {
        return Hex.encode0x(encodeHash());
    }

}
