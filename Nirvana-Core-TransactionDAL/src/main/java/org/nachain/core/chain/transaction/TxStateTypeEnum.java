package org.nachain.core.chain.transaction;

import static java.util.Arrays.stream;


public enum TxStateTypeEnum {


    Tx(1, "Tx"),


    TxDas(2, "TxDas"),


    Instance(3, "Instance");

    public final int id;
    public final String symbol;

    TxStateTypeEnum(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }


    public static TxStateTypeEnum of(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }


    public static TxStateTypeEnum of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }

}
