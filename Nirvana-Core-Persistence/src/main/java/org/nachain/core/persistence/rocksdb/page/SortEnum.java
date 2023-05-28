package org.nachain.core.persistence.rocksdb.page;

import static java.util.Arrays.stream;


public enum SortEnum {


    ASC("ASC"),


    DESC("DESC");

    public final String name;

    SortEnum(String name) {
        this.name = name;
    }


    public static SortEnum of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }
}
