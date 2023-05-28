package org.nachain.core.dapp.internal.swap.coinage.dto;

import org.nachain.core.dapp.internal.swap.ISwapAction;
import org.nachain.core.dapp.internal.swap.SwapActionEnum;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


public class CoinageBuyDTO implements ISwapAction {


    private String coinageName;


    private BigInteger buyBaseAmount;


    private BigInteger payQuoteAmount;


    private double slippage;

    public String getCoinageName() {
        return coinageName;
    }

    public CoinageBuyDTO setCoinageName(String coinageName) {
        this.coinageName = coinageName;
        return this;
    }

    public BigInteger getBuyBaseAmount() {
        return buyBaseAmount;
    }

    public CoinageBuyDTO setBuyBaseAmount(BigInteger buyBaseAmount) {
        this.buyBaseAmount = buyBaseAmount;
        return this;
    }

    public BigInteger getPayQuoteAmount() {
        return payQuoteAmount;
    }

    public CoinageBuyDTO setPayQuoteAmount(BigInteger payQuoteAmount) {
        this.payQuoteAmount = payQuoteAmount;
        return this;
    }

    public double getSlippage() {
        return slippage;
    }

    public CoinageBuyDTO setSlippage(double slippage) {
        this.slippage = slippage;
        return this;
    }

    @Override
    public String toString() {
        return "CoinageBuyDTO{" +
                "coinageName='" + coinageName + '\'' +
                ", buyBaseAmount=" + buyBaseAmount +
                ", payQuoteAmount=" + payQuoteAmount +
                ", slippage=" + slippage +
                '}';
    }


    public static CoinageBuyDTO to(String json) {
        return JsonUtils.jsonToPojo(json, CoinageBuyDTO.class);
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }


    @Override
    public SwapActionEnum getAction() {
        return SwapActionEnum.COINAGE_BUY;
    }
}
