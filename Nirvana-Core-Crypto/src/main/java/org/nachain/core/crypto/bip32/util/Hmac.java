package org.nachain.core.crypto.bip32.util;


import org.nachain.core.util.exception.CryptoException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class Hmac {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String HMAC_SHA512 = "HmacSHA512";


    public static byte[] hmac256(byte[] message, byte[] secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(secret, HMAC_SHA256);
            mac.init(keySpec);
            return mac.doFinal(message);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new CryptoException("Unable to perform HmacSHA256.", e);
        }
    }


    public static byte[] hmac512(byte[] message, byte[] secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(secret, HMAC_SHA512);
            mac.init(keySpec);
            return mac.doFinal(message);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new CryptoException("Unable to perform HmacSHA512.", e);
        }
    }
}
