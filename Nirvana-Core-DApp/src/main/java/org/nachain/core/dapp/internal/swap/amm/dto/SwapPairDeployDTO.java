package org.nachain.core.dapp.internal.swap.amm.dto;

import org.nachain.core.dapp.internal.swap.ISwapAction;
import org.nachain.core.dapp.internal.swap.SwapActionEnum;
import org.nachain.core.dapp.internal.swap.SwapPair;


public class SwapPairDeployDTO implements ISwapAction {


    private SwapPair swapPair;

    public SwapPair getSwapPair() {
        return swapPair;
    }

    public void setSwapPair(SwapPair swapPair) {
        this.swapPair = swapPair;
    }

    @Override
    public String toString() {
        return "SwapPairDeployDTO{" +
                "swapPair=" + swapPair +
                '}';
    }

    @Override
    public SwapActionEnum getAction() {
        return SwapActionEnum.SWAP_PAIR_DEPLOY;
    }
}

