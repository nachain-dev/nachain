package org.nachain.core.dapp.internal.swap.amm;

import java.math.BigDecimal;


public class Amm {


    private String pairName;


    private String base;


    private String quote;

    private BigDecimal baseAmount = BigDecimal.ZERO;

    private BigDecimal quoteAmount = BigDecimal.ZERO;


    private BigDecimal product = BigDecimal.ZERO;


    private BigDecimal lastBasePrice = BigDecimal.ZERO;


    public String getPairName() {
        return pairName;
    }

    public Amm setPairName(String pairName) {
        this.pairName = pairName;
        return this;
    }

    public String getBase() {
        return base;
    }

    public Amm setBase(String base) {
        this.base = base;
        return this;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public Amm setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    public BigDecimal getLastBasePrice() {
        return lastBasePrice;
    }

    public Amm setLastBasePrice(BigDecimal lastBasePrice) {
        this.lastBasePrice = lastBasePrice;
        return this;
    }

    public String getQuote() {
        return quote;
    }

    public Amm setQuote(String quote) {
        this.quote = quote;
        return this;
    }

    public BigDecimal getQuoteAmount() {
        return quoteAmount;
    }

    public Amm setQuoteAmount(BigDecimal quoteAmount) {
        this.quoteAmount = quoteAmount;
        return this;
    }

    public BigDecimal getProduct() {
        return product;
    }

    public Amm setProduct(BigDecimal product) {
        this.product = product;
        return this;
    }

    @Override
    public String toString() {
        return "Amm{" +
                "pairName='" + pairName + '\'' +
                ", base='" + base + '\'' +
                ", quote='" + quote + '\'' +
                ", baseAmount=" + baseAmount +
                ", quoteAmount=" + quoteAmount +
                ", product=" + product +
                ", lastBasePrice=" + lastBasePrice +
                '}';
    }
}
