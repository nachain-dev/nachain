package org.nachain.core.chain.structure.instance;

import static java.util.Arrays.stream;


public enum InstanceType {


    Core(1, "Core"),


    Token(2, "Token"),


    DApp(3, "DApp"),


    DWeb(4, "DWeb"),


    DContract(5, "DContract");

    public final int exp;
    public final String name;

    InstanceType(int exp, String name) {
        this.exp = exp;
        this.name = name;
    }


    public static InstanceType of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static InstanceType of(int id) {
        return stream(values()).filter(v -> v.exp == id).findAny().orElse(null);
    }

}
