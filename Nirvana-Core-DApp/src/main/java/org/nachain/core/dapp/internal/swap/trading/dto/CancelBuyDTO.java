package org.nachain.core.dapp.internal.swap.trading.dto;

import org.nachain.core.dapp.internal.swap.ISwapAction;
import org.nachain.core.dapp.internal.swap.SwapActionEnum;


public class CancelBuyDTO implements ISwapAction {
    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "CancelBuyDTO{" +
                "hash='" + hash + '\'' +
                '}';
    }

    @Override
    public SwapActionEnum getAction() {
        return SwapActionEnum.TRADING_BUY_CANCEL;
    }
}
