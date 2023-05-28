package org.nachain.core.intermediate.feedback;

import static java.util.Arrays.stream;


public enum FeedbackStatus {


    SUCCEED("SUCCEED", 1), PENDING("PENDING", 0), FAILED("FAILED", -1);

    public final String name;
    public final int value;

    FeedbackStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }


    public static FeedbackStatus of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static FeedbackStatus of(int value) {
        return stream(values()).filter(v -> v.value == value).findAny().orElse(null);
    }

    public static String getName(int value) {
        return of(value).name;
    }

}
