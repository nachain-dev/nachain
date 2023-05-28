package org.nachain.core.base;

import java.math.BigInteger;

import static java.util.Arrays.stream;

public enum TokenUnit {


    NANO_TOKEN(0, "nToken"),


    MICRO_TOKEN(3, "μTOKEN"),


    MILLI_TOKEN(6, "mTOKEN"),


    TOKEN(9, "TOKEN"),


    KILO_TOKEN(12, "kTOKEN"),


    MEGA_TOKEN(15, "MTOKEN"),


    GIGA_TOKEN(18, "GTOKEN");

    public final int exp;
    public final long factor;
    public final String symbol;

    TokenUnit(int exp, String symbol) {

        this.exp = exp;

        this.factor = BigInteger.TEN.pow(exp).longValue();

        this.symbol = symbol;
    }


    public static TokenUnit of(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }


    public static TokenUnit of(int id) {
        return stream(values()).filter(v -> v.exp == id).findAny().orElse(null);
    }
}
