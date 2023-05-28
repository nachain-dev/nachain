package org.nachain.core.chain.structure.instance;

import static java.util.Arrays.stream;

public enum ChainTypeEnum {


    PoWF(1, "PoWF"),


    DPoS(2, "DPoS"),


    DAS(3, "DAS");

    public final int id;
    public final String name;

    ChainTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public static ChainTypeEnum of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static ChainTypeEnum of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }

}
