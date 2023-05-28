package org.nachain.core.intermediate;

import static java.util.Arrays.stream;


public enum TxBatchType {


    ALL(1, "ALL"),


    IN(2, "IN"),


    OUT(3, "OUT");

    public final int exp;
    public final String symbol;

    TxBatchType(int exp, String symbol) {
        this.exp = exp;
        this.symbol = symbol;
    }


    public static TxBatchType of(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }


    public static TxBatchType of(int id) {
        return stream(values()).filter(v -> v.exp == id).findAny().orElse(null);
    }
}
