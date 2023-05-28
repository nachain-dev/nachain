package org.nachain.core.crypto;

import org.nachain.core.util.exception.CryptoException;
import org.spongycastle.crypto.digests.RIPEMD160Digest;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.Security;


public class Hash {

    public static final int HASH_LEN = 32;


    public static final String HASH_ALGORITHM = "BLAKE2B-256";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private Hash() {
    }


    public static byte[] h256(byte[] input) {

        if (Native.isEnabled()) {
            return Native.h256(input);
        } else {
            try {
                MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
                return digest.digest(input);
            } catch (Exception e) {
                throw new CryptoException(e);
            }
        }
    }


    public static byte[] h256(byte[] one, byte[] two) {
        byte[] all = new byte[one.length + two.length];
        System.arraycopy(one, 0, all, 0, one.length);
        System.arraycopy(two, 0, all, one.length, two.length);

        return Hash.h256(all);
    }


    public static byte[] h160(byte[] input) {
        if (Native.isEnabled()) {
            return Native.h160(input);
        } else {
            try {
                byte[] h256 = h256(input);

                RIPEMD160Digest digest = new RIPEMD160Digest();
                digest.update(h256, 0, h256.length);
                byte[] out = new byte[20];
                digest.doFinal(out, 0);
                return out;
            } catch (Exception e) {
                throw new CryptoException(e);
            }
        }
    }

}
