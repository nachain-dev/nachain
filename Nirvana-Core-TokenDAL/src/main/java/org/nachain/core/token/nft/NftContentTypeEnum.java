package org.nachain.core.token.nft;

import static java.util.Arrays.stream;


public enum NftContentTypeEnum {


    CUSTOM(1, "CUSTOM"),


    IMAGE(2, "IMAGE"),


    VOICE(3, "VOICE"),


    VIDEO(4, "VIDEO"),


    TEXT(5, "TEXT"),


    ATTRIBUTE(6, "ATTRIBUTE"),


    NAME(7, "NAME");

    public final int id;
    public final String name;

    NftContentTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public static NftContentTypeEnum of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static NftContentTypeEnum of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }

}
