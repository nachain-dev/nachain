package org.nachain.core.dapp.internal.swap.amm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class LiquidityPool {


    private String pairName;


    private BigInteger baseAmount;


    private BigInteger quoteAmount;


    private BigInteger product;


    private List<String> lps = new ArrayList<>();

    public String getPairName() {
        return pairName;
    }

    public LiquidityPool setPairName(String pairName) {
        this.pairName = pairName;
        return this;
    }

    public BigInteger getBaseAmount() {
        return baseAmount;
    }

    public LiquidityPool setBaseAmount(BigInteger baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    public BigInteger getQuoteAmount() {
        return quoteAmount;
    }

    public LiquidityPool setQuoteAmount(BigInteger quoteAmount) {
        this.quoteAmount = quoteAmount;
        return this;
    }

    public List<String> getLps() {
        return lps;
    }

    public LiquidityPool setLps(List<String> lps) {
        this.lps = lps;
        return this;
    }

    public BigInteger getProduct() {
        return product;
    }

    public LiquidityPool setProduct(BigInteger product) {
        this.product = product;
        return this;
    }

    @Override
    public String toString() {
        return "LiquidityPool{" +
                "pairName='" + pairName + '\'' +
                ", baseAmount=" + baseAmount +
                ", quoteAmount=" + quoteAmount +
                ", product=" + product +
                ", lps=" + lps +
                '}';
    }
}
