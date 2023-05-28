package org.nachain.core.dapp.internal.dao.votes;

import static java.util.Arrays.stream;

public enum VoteTypeEnum {


    SUPER_NODE_VOTE_MAXIMUM("SUPER_NODE_VOTE_MAXIMUM", 1);

    public final String name;
    public final int value;

    VoteTypeEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }


    public static VoteTypeEnum of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }

    public static VoteTypeEnum of(int value) {
        return stream(values()).filter(v -> v.value == value).findAny().orElse(null);
    }
}