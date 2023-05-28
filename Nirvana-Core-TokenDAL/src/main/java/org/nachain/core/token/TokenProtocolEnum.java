package org.nachain.core.token;

import static java.util.Arrays.stream;

public enum TokenProtocolEnum {


    NORMAL(1, "NORMAL"),


    NFT(2, "NFT");

    public final int id;
    public final String symbol;

    TokenProtocolEnum(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }


    public static TokenProtocolEnum of(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }


    public static TokenProtocolEnum of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }
}
