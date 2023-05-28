package org.nachain.core.chain.transaction;

import static java.util.Arrays.stream;


public enum TxType {


    GENESIS_BLOCK("GENESIS_BLOCK", 0),


    COINBASE("COINBASE", 1),


    MINT("MINT", 2),


    TRANSFER("TRANSFER", 11),


    TRANSFER_CHANGE("TRANSFER_CHANGE", 12),


    TRANSFER_CROSS_OUT("TRANSFER_CROSS_OUT", 13),


    TRANSFER_CROSS_IN("TRANSFER_CROSS_IN", 14),


    TRANSFER_EVENT_REFUND("TRANSFER_EVENT_REFUND", 15),


    TRANSFER_EVENT_WITHDRAW("TRANSFER_EVENT_WITHDRAW", 16),


    TRANSFER_GAS("TRANSFER_GAS", 17);

    public final String name;
    public final int value;

    TxType(String name, int value) {
        this.name = name;
        this.value = value;
    }


    public static TxType of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }

    public static TxType of(int value) {
        return stream(values()).filter(v -> v.value == value).findAny().orElse(null);
    }

}
