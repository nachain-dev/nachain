package org.nachain.core.chain.config;

import static java.util.Arrays.stream;


public enum IndexEnum {


    INSTANCE("Instance"),


    INSTANCE_TYPE("InstanceType"),


    FULL_NODE("FullNode"),


    SWAP_QUOTE("SwapQuote");

    public final String name;

    IndexEnum(String name) {
        this.name = name;
    }


    public static IndexEnum of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }

}
