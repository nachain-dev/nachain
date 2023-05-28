package org.nachain.core.dapp.internal.bridge;

import static java.util.Arrays.stream;


public enum ChainName {

    ETH(1, "Ethereum"),

    TRON(2, "TRON"),

    BSC(3, "Binance Smart Chain");

    public final int id;
    public final String name;

    ChainName(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public static ChainName of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static ChainName of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }
}
