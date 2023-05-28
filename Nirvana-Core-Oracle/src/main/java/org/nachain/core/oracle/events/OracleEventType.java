package org.nachain.core.oracle.events;

import static java.util.Arrays.stream;


public enum OracleEventType {


    NAC_PRICE(1, "NAC_PRICE"),


    NOMC_PRICE(2, "NOMC_PRICE");

    public final int id;
    public final String symbol;

    OracleEventType(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }


    public static OracleEventType of(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }


    public static OracleEventType of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }

}
