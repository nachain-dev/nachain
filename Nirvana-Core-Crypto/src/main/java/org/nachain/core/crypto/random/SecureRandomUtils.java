package org.nachain.core.crypto.random;

import org.nachain.core.util.SystemUtils;

import java.security.SecureRandom;


public class SecureRandomUtils {
    private static final SecureRandom SECURE_RANDOM;

    static {
        if (SystemUtils.isAndroidRuntime()) {
            new LinuxSecureRandom();
        }
        SECURE_RANDOM = new SecureRandom();
    }

    private SecureRandomUtils() {
    }

    public static SecureRandom secureRandom() {
        return SECURE_RANDOM;
    }


}
