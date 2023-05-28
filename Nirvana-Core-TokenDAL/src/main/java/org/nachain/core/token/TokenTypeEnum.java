package org.nachain.core.token;

import static java.util.Arrays.stream;


public enum TokenTypeEnum {


    KERNEL(1, "KERNEL"),


    FIXED(2, "FIXED"),


    DYNAMIC(3, "DYNAMIC");

    public final int id;
    public final String symbol;

    TokenTypeEnum(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }


    public static TokenTypeEnum of(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }


    public static TokenTypeEnum of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }

}
