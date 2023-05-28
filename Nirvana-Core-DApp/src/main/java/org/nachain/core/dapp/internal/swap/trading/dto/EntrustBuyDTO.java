package org.nachain.core.dapp.internal.swap.trading.dto;

import org.nachain.core.dapp.internal.swap.ISwapAction;
import org.nachain.core.dapp.internal.swap.SwapActionEnum;
import org.nachain.core.dapp.internal.swap.trading.request.BuyRequest;


public class EntrustBuyDTO extends BuyRequest implements ISwapAction {

    @Override
    public String toString() {
        return "BuyRequestDTO{" +
                "tradingType=" + tradingType +
                ", pairName='" + pairName + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", buyTokenAmount=" + buyTokenAmount +
                ", payAmount=" + payAmount +
                ", timestamp=" + timestamp +
                '}';
    }


    @Override
    public SwapActionEnum getAction() {
        return SwapActionEnum.TRADING_BUY;
    }
}
