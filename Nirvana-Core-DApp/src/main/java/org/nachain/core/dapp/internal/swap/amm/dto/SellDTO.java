package org.nachain.core.dapp.internal.swap.amm.dto;

import org.nachain.core.dapp.internal.swap.ISwapAction;
import org.nachain.core.dapp.internal.swap.SwapActionEnum;
import org.nachain.core.dapp.internal.swap.trading.AbstractSell;
import org.nachain.core.util.JsonUtils;


public class SellDTO extends AbstractSell implements ISwapAction {


    private double slippage;

    public double getSlippage() {
        return slippage;
    }

    public SellDTO setSlippage(double slippage) {
        this.slippage = slippage;
        return this;
    }

    @Override
    public String toString() {
        return "SellDTO{" +
                "slippage=" + slippage +
                ", tradingType=" + tradingType +
                ", pairName='" + pairName + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", sellTokenAmount=" + sellTokenAmount +
                ", earnAmount=" + earnAmount +
                ", timestamp=" + timestamp +
                '}';
    }


    public static SellDTO to(String json) {
        return JsonUtils.jsonToPojo(json, SellDTO.class);
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }

    @Override
    public SwapActionEnum getAction() {
        return SwapActionEnum.AMM_SELL;
    }
}
