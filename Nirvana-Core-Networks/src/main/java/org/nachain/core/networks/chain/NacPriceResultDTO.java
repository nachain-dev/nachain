package org.nachain.core.networks.chain;

import java.math.BigDecimal;
import java.math.BigInteger;


public class NacPriceResultDTO {


    BigInteger nacAmount;


    BigDecimal nacPrice;


    BigInteger fullNodeHashRate;

    public BigInteger getNacAmount() {
        return nacAmount;
    }

    public NacPriceResultDTO setNacAmount(BigInteger nacAmount) {
        this.nacAmount = nacAmount;
        return this;
    }

    public BigDecimal getNacPrice() {
        return nacPrice;
    }

    public NacPriceResultDTO setNacPrice(BigDecimal nacPrice) {
        this.nacPrice = nacPrice;
        return this;
    }

    public BigInteger getFullNodeHashRate() {
        return fullNodeHashRate;
    }

    public NacPriceResultDTO setFullNodeHashRate(BigInteger fullNodeHashRate) {
        this.fullNodeHashRate = fullNodeHashRate;
        return this;
    }

    @Override
    public String toString() {
        return "NacPriceResultDTO{" +
                "nacAmount=" + nacAmount +
                ", nacPrice=" + nacPrice +
                ", fullNodeHashRate=" + fullNodeHashRate +
                '}';
    }
}
