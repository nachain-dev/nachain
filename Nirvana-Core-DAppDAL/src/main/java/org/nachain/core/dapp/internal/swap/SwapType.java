package org.nachain.core.dapp.internal.swap;

import static java.util.Arrays.stream;


public enum SwapType {


    SWAP_AMM(1, "SWAP_AMM"),


    ENTRUST(2, "ENTRUST"),


    COINAGE(3, "COINAGE");

    public final int id;
    public final String name;

    SwapType(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public static SwapType of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static SwapType of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }
}
