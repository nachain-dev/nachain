package org.nachain.core.dapp.internal.swap.trading;

import static java.util.Arrays.stream;


public enum TradingType {


    BUY_LIMITED_PRICE(1, "BUY_LIMITED_PRICE"),


    SALE_LIMITED_PRICE(2, "SALE_LIMITED_PRICE"),


    BUY_MARKET_PRICE(3, "BUY_MARKET_PRICE"),


    SALE_MARKET_PRICE(4, "SALE_MARKET_PRICE");

    public final int id;
    public final String name;

    TradingType(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public static TradingType of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static TradingType of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }
}
