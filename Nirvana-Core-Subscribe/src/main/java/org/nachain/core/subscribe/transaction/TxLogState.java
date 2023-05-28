package org.nachain.core.subscribe.transaction;

import static java.util.Arrays.stream;


public enum TxLogState {


    Unprocessed(0, "Unprocessed"),


    Begin(1, "Begin"),


    Processed(2, "Processed"),


    Fail(3, "Fail"),


    Rollback(4, "Rollback");

    public final int id;
    public final String name;

    TxLogState(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public static TxLogState of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static TxLogState of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }

}
