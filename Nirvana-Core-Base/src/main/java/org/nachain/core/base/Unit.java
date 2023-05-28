package org.nachain.core.base;

import java.math.BigInteger;

import static java.util.Arrays.stream;

public enum Unit {


    NANO_NAC(0, "nNAC"),


    MICRO_NAC(3, "μNAC"),


    MILLI_NAC(6, "mNAC"),


    NAC(9, "NAC"),


    KILO_NAC(12, "kNAC"),


    MEGA_NAC(15, "MNAC"),


    GIGA_NAC(18, "GNAC");

    public final int exp;
    public final long factor;
    public final String symbol;

    Unit(int exp, String symbol) {

        this.exp = exp;

        this.factor = BigInteger.TEN.pow(exp).longValue();

        this.symbol = symbol;
    }


    public static Unit of(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }


    public static Unit of(int id) {
        return stream(values()).filter(v -> v.exp == id).findAny().orElse(null);
    }
}
