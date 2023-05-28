package org.nachain.core.token;

public class TokenSingleton {

    private static TokenManager tokenManager;

    static {
        tokenManager = new TokenManager();
    }

    public static TokenManager get() {
        return tokenManager;
    }

}
