package org.nachain.core.dapp.internal.swap.amm.dto;

import org.nachain.core.dapp.internal.swap.ISwapAction;
import org.nachain.core.dapp.internal.swap.SwapActionEnum;
import org.nachain.core.dapp.internal.swap.trading.AbstractBuy;
import org.nachain.core.util.JsonUtils;


public class BuyDTO extends AbstractBuy implements ISwapAction {


    private double slippage;

    public double getSlippage() {
        return slippage;
    }

    public BuyDTO setSlippage(double slippage) {
        this.slippage = slippage;
        return this;
    }

    @Override
    public String toString() {
        return "BuyDTO{" +
                "slippage=" + slippage +
                ", tradingType=" + tradingType +
                ", pairName='" + pairName + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", buyTokenAmount=" + buyTokenAmount +
                ", payAmount=" + payAmount +
                ", timestamp=" + timestamp +
                '}';
    }


    public static BuyDTO to(String json) {
        return JsonUtils.jsonToPojo(json, BuyDTO.class);
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }

    @Override
    public SwapActionEnum getAction() {
        return SwapActionEnum.AMM_BUY;
    }
}
