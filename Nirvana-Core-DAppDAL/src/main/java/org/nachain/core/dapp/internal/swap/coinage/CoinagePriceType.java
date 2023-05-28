package org.nachain.core.dapp.internal.swap.coinage;

import static java.util.Arrays.stream;


public enum CoinagePriceType {


    SWAP_AMM(1, "SWAP_AMM"),


    FIXED_PRICE(2, "FIXED_PRICE"),


    ENTRUST(3, "ENTRUST");

    public final int id;
    public final String name;

    CoinagePriceType(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public static CoinagePriceType of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static CoinagePriceType of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }
}
