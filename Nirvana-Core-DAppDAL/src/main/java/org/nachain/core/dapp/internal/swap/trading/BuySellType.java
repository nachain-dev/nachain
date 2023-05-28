package org.nachain.core.dapp.internal.swap.trading;

import static java.util.Arrays.stream;


public enum BuySellType {


    BUY(1, "BUY"),


    SELL(2, "SELL");

    public final int id;
    public final String name;

    BuySellType(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public static BuySellType of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static BuySellType of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }
}
