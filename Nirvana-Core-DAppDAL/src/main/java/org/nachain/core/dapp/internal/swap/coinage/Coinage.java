package org.nachain.core.dapp.internal.swap.coinage;

import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


public class Coinage {


    private String coinageName;


    private String base;


    private String quote;


    private BigInteger baseAmount;


    private BigInteger quoteAmount;


    private double fee;


    private BigInteger initializationAmount;


    private CoinagePriceType coinagePriceType;


    private BigInteger fixedPrice = BigInteger.ZERO;

    public String getCoinageName() {
        return coinageName;
    }

    public Coinage setCoinageName(String coinageName) {
        this.coinageName = coinageName;
        return this;
    }

    public double getFee() {
        return fee;
    }

    public Coinage setFee(double fee) {
        this.fee = fee;
        return this;
    }

    public BigInteger getInitializationAmount() {
        return initializationAmount;
    }

    public Coinage setInitializationAmount(BigInteger initializationAmount) {
        this.initializationAmount = initializationAmount;
        return this;
    }

    public String getBase() {
        return base;
    }

    public Coinage setBase(String base) {
        this.base = base;
        return this;
    }

    public String getQuote() {
        return quote;
    }

    public Coinage setQuote(String quote) {
        this.quote = quote;
        return this;
    }

    public BigInteger getBaseAmount() {
        return baseAmount;
    }

    public Coinage setBaseAmount(BigInteger baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    public BigInteger getQuoteAmount() {
        return quoteAmount;
    }

    public Coinage setQuoteAmount(BigInteger quoteAmount) {
        this.quoteAmount = quoteAmount;
        return this;
    }

    public CoinagePriceType getCoinagePriceType() {
        return coinagePriceType;
    }

    public Coinage setCoinagePriceType(CoinagePriceType coinagePriceType) {
        this.coinagePriceType = coinagePriceType;
        return this;
    }

    public BigInteger getFixedPrice() {
        return fixedPrice;
    }

    public Coinage setFixedPrice(BigInteger fixedPrice) {
        this.fixedPrice = fixedPrice;
        return this;
    }

    @Override
    public String toString() {
        return "Coinage{" +
                "coinageName='" + coinageName + '\'' +
                ", base='" + base + '\'' +
                ", quote='" + quote + '\'' +
                ", baseAmount=" + baseAmount +
                ", quoteAmount=" + quoteAmount +
                ", fee=" + fee +
                ", initializationAmount=" + initializationAmount +
                ", coinagePriceType=" + coinagePriceType +
                ", fixedPrice=" + fixedPrice +
                '}';
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }


    public static Coinage to(String json) {
        return JsonUtils.jsonToPojo(json, Coinage.class);
    }
}
