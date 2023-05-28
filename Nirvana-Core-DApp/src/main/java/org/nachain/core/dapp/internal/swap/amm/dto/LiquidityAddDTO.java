package org.nachain.core.dapp.internal.swap.amm.dto;

import org.nachain.core.dapp.internal.swap.ISwapAction;
import org.nachain.core.dapp.internal.swap.SwapActionEnum;
import org.nachain.core.dapp.internal.swap.amm.LiquidityProvider;
import org.nachain.core.util.JsonUtils;


public class LiquidityAddDTO implements ISwapAction {

    private LiquidityProvider liquidityProvider;

    public LiquidityProvider getLiquidityProvider() {
        return liquidityProvider;
    }

    public LiquidityAddDTO setLiquidityProvider(LiquidityProvider liquidityProvider) {
        this.liquidityProvider = liquidityProvider;
        return this;
    }

    @Override
    public String toString() {
        return "LiquidityAddDTO{" +
                "liquidityProvider=" + liquidityProvider +
                '}';
    }


    public static LiquidityAddDTO to(String json) {
        return JsonUtils.jsonToPojo(json, LiquidityAddDTO.class);
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }


    @Override
    public SwapActionEnum getAction() {
        return SwapActionEnum.AMM_LIQUIDITY_ADD;
    }
}
