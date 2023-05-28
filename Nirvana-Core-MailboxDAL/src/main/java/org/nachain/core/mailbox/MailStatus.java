package org.nachain.core.mailbox;

import static java.util.Arrays.stream;


public enum MailStatus {


    COMPLETED("COMPLETED", 1), PENDING("PENDING", 0), FAILED("FAILED", -1);

    private final String name;
    private final int value;

    MailStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }


    public static MailStatus of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }

    public static MailStatus of(int value) {
        return stream(values()).filter(v -> v.value == value).findAny().orElse(null);
    }


}
