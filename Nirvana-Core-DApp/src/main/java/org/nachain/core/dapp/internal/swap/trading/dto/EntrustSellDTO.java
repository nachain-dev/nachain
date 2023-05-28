package org.nachain.core.dapp.internal.swap.trading.dto;

import org.nachain.core.dapp.internal.swap.ISwapAction;
import org.nachain.core.dapp.internal.swap.SwapActionEnum;
import org.nachain.core.dapp.internal.swap.trading.request.SellRequest;


public class EntrustSellDTO extends SellRequest implements ISwapAction {

    @Override
    public String toString() {
        return "SellRequestDTO{" +
                "tradingType=" + tradingType +
                ", pairName='" + pairName + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", sellTokenAmount=" + sellTokenAmount +
                ", earnAmount=" + earnAmount +
                ", timestamp=" + timestamp +
                '}';
    }


    @Override
    public SwapActionEnum getAction() {
        return SwapActionEnum.TRADING_SELL;
    }
}
