package org.nachain.core.dapp.internal.swap.amm.dto;

import org.nachain.core.dapp.internal.swap.ISwapAction;
import org.nachain.core.dapp.internal.swap.SwapActionEnum;
import org.nachain.core.util.JsonUtils;


public class LiquidityRemoveDTO implements ISwapAction {

    private String liquidityProviderHash;

    public String getLiquidityProviderHash() {
        return liquidityProviderHash;
    }

    public LiquidityRemoveDTO setLiquidityProviderHash(String liquidityProviderHash) {
        this.liquidityProviderHash = liquidityProviderHash;
        return this;
    }

    @Override
    public String toString() {
        return "LiquidityRemoveDTO{" +
                "liquidityProviderHash='" + liquidityProviderHash + '\'' +
                '}';
    }


    public static LiquidityRemoveDTO to(String json) {
        return JsonUtils.jsonToPojo(json, LiquidityRemoveDTO.class);
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }


    @Override
    public SwapActionEnum getAction() {
        return SwapActionEnum.AMM_LIQUIDITY_REMOVE;
    }
}
