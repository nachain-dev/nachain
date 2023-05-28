package org.nachain.core.dapp.internal.swap.coinage.dto;

import org.nachain.core.dapp.internal.swap.ISwapAction;
import org.nachain.core.dapp.internal.swap.SwapActionEnum;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


public class CoinageAddDTO implements ISwapAction {


    private String coinageName;


    private String base;


    private BigInteger amount;

    public String getCoinageName() {
        return coinageName;
    }

    public CoinageAddDTO setCoinageName(String coinageName) {
        this.coinageName = coinageName;
        return this;
    }

    public String getBase() {
        return base;
    }

    public CoinageAddDTO setBase(String base) {
        this.base = base;
        return this;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public CoinageAddDTO setAmount(BigInteger amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public String toString() {
        return "CoinageAddDTO{" +
                "coinageName='" + coinageName + '\'' +
                ", base='" + base + '\'' +
                ", amount=" + amount +
                '}';
    }


    public static CoinageAddDTO to(String json) {
        return JsonUtils.jsonToPojo(json, CoinageAddDTO.class);
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }


    @Override
    public SwapActionEnum getAction() {
        return SwapActionEnum.COINAGE_ADD;
    }
}
