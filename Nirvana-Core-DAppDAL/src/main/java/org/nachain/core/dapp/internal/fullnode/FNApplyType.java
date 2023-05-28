package org.nachain.core.dapp.internal.fullnode;

import static java.util.Arrays.stream;

public enum FNApplyType {


    NOMC_NAC("NOMC_NAC", 1),


    DOUBLE_NAC("DOUBLE_NAC", 2);

    public final String name;
    public final int value;

    FNApplyType(String name, int value) {
        this.name = name;
        this.value = value;
    }


    public static FNApplyType of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }

    public static FNApplyType of(int value) {
        return stream(values()).filter(v -> v.value == value).findAny().orElse(null);
    }
}