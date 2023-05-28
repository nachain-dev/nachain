package org.nachain.core.consensus;

import static java.util.Arrays.stream;


public enum Consensus {


    PoW("PoW", 1), PoS("PoS", 2), DPoS("DPoS", 3), PoWF("PoWF", 4), DAG("DAG", 5), DAS("DAS", 6);

    public final String name;
    public final int index;

    Consensus(String name, int index) {
        this.name = name;
        this.index = index;
    }


    public static Consensus of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static Consensus of(int index) {
        return stream(values()).filter(v -> v.index == index).findAny().orElse(null);
    }

    public static String getName(int index) {
        return of(index).name;
    }

}
