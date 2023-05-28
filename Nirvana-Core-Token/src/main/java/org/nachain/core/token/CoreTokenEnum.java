package org.nachain.core.token;

import static java.util.Arrays.stream;


public enum CoreTokenEnum {


    NULL(-1, "", "NULL", "No tokens are defined"),


    NAC(1, "Nirvana Coin", "NAC", "Nirvana Coin"),


    NOMC(2, "Nirvana Nomc", "NOMC", "Nirvana operators maintenance chain"),


    USDN(3, "Nirvana USDN", "USDN", "Nirvana USD");

    public final long id;
    public final String name;
    public final String symbol;
    public final String info;

    CoreTokenEnum(long id, String name, String symbol, String info) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.info = info;
    }


    public static CoreTokenEnum of(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }


    public static CoreTokenEnum of(long id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }

}
