package org.nachain.core.dapp.internal.swap.coinage.dto;

import org.nachain.core.dapp.internal.swap.ISwapAction;
import org.nachain.core.dapp.internal.swap.SwapActionEnum;
import org.nachain.core.dapp.internal.swap.coinage.CoinagePriceType;

import java.math.BigInteger;


public class CoinageDeployDTO implements ISwapAction {


    private String base;

    private String quote;

    private double buyFee;

    private CoinagePriceType coinagePriceType;

    private BigInteger fixedPrice;

    public String getBase() {
        return base;
    }

    public CoinageDeployDTO setBase(String base) {
        this.base = base;
        return this;
    }

    public String getQuote() {
        return quote;
    }

    public CoinageDeployDTO setQuote(String quote) {
        this.quote = quote;
        return this;
    }

    public double getBuyFee() {
        return buyFee;
    }

    public CoinageDeployDTO setBuyFee(double buyFee) {
        this.buyFee = buyFee;
        return this;
    }

    public CoinagePriceType getCoinagePriceType() {
        return coinagePriceType;
    }

    public CoinageDeployDTO setCoinagePriceType(CoinagePriceType coinagePriceType) {
        this.coinagePriceType = coinagePriceType;
        return this;
    }

    public BigInteger getFixedPrice() {
        return fixedPrice;
    }

    public CoinageDeployDTO setFixedPrice(BigInteger fixedPrice) {
        this.fixedPrice = fixedPrice;
        return this;
    }

    @Override
    public String toString() {
        return "CoinageDeployDTO{" +
                "base='" + base + '\'' +
                ", quote='" + quote + '\'' +
                ", buyFee=" + buyFee +
                ", coinagePriceType=" + coinagePriceType +
                ", fixedPrice=" + fixedPrice +
                '}';
    }

    @Override
    public SwapActionEnum getAction() {
        return SwapActionEnum.COINAGE_DEPLOY;
    }
}

