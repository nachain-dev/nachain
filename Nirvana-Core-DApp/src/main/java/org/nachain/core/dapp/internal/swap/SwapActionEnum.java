package org.nachain.core.dapp.internal.swap;

import static java.util.Arrays.stream;

public enum SwapActionEnum {


    SWAP_PAIR_DEPLOY(1, "SWAP_PAIR_DEPLOY"),


    AMM_BUY(10, "AMM_BUY"),


    AMM_SELL(11, "AMM_SELL"),


    AMM_LIQUIDITY_ADD(13, "AMM_LIQUIDITY_ADD"),


    AMM_LIQUIDITY_REMOVE(14, "AMM_LIQUIDITY_REMOVE"),


    TRADING_BUY(20, "TRADING_BUY"),


    TRADING_SELL(21, "TRADING_SELL"),


    TRADING_BUY_CANCEL(22, "TRADING_BUY_CANCEL"),


    TRADING_SELL_CANCEL(23, "TRADING_SELL_CANCEL"),


    COINAGE_DEPLOY(30, "COINAGE_DEPLOY"),


    COINAGE_ADD(31, "COINAGE_ADD"),


    COINAGE_BUY(32, "COINAGE_BUY");

    public final int id;
    public final String name;

    SwapActionEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public static SwapActionEnum of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static SwapActionEnum of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }

}
