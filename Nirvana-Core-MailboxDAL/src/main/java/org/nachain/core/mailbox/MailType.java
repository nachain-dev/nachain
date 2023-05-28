package org.nachain.core.mailbox;

import static java.util.Arrays.stream;


public enum MailType {


    MSG_SEND_TX(1, "MSG_SEND_TX"),


    MSG_SEND_TX_DAS(2, "MSG_SEND_TX_DAS"),


    MSG_INSTRUCTION_SET(3, "MSG_INSTRUCTION_SET");

    public final int id;
    public final String symbol;

    MailType(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }


    public static MailType of(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }


    public static MailType of(int id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }
}
