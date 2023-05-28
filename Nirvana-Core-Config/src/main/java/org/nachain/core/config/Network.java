package org.nachain.core.config;

import static java.util.Arrays.stream;

public enum Network {


    MAINNET((byte) 0, "mainnet"),


    TESTNET((byte) 1, "testnet"),


    DEVNET((byte) 2, "devnet");

    public final byte id;
    public final String symbol;

    Network(byte id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }


    public static Network of(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }


    public static Network of(byte id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }

}
