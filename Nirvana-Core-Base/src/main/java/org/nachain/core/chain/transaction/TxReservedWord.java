package org.nachain.core.chain.transaction;

import static java.util.Arrays.stream;


public enum TxReservedWord {


    COINBASE("COINBASE"),


    GENESIS("GENESIS"),


    MINT("MINT"),


    INSTANCE("INSTANCE"),


    TRANSFER("TRANSFER"),


    TRANSFER_CHANGE("TRANSFER_CHANGE"),


    TRANSFER_CROSS_OUT("TRANSFER_CROSS_OUT"),


    TRANSFER_CROSS_IN("TRANSFER_CROSS_IN"),


    TRANSFER_EVENT_REFUND("TRANSFER_EVENT_REFUND"),


    TRANSFER_EVENT_WITHDRAW("TRANSFER_EVENT_WITHDRAW"),


    TRANSFER_GAS("TRANSFER_GAS");

    public final String name;

    TxReservedWord(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


    public static TxReservedWord of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }
}
