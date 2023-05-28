package org.nachain.core.crypto;

import org.nachain.core.util.exception.CryptoException;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.DataLengthException;
import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;

import java.nio.charset.StandardCharsets;


public class Aes {


    private final static String ivParameter = "0392039203920300";

    private Aes() {
    }


    private static byte[] cipherData(PaddedBufferedBlockCipher cipher, byte[] data)
            throws DataLengthException, IllegalStateException, InvalidCipherTextException {

        int size = cipher.getOutputSize(data.length);
        byte[] buf = new byte[size];


        int length1 = cipher.processBytes(data, 0, data.length, buf, 0);
        int length2 = cipher.doFinal(buf, length1);
        int length = length1 + length2;


        byte[] result = new byte[length];
        System.arraycopy(buf, 0, result, 0, result.length);

        return result;
    }


    public static byte[] encrypt(byte[] raw, byte[] key, byte[] iv) throws CryptoException {

        try {
            PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
            CipherParameters params = new ParametersWithIV(new KeyParameter(key), iv);
            aes.init(true, params);

            return cipherData(aes, raw);
        } catch (DataLengthException | IllegalArgumentException | IllegalStateException
                 | InvalidCipherTextException e) {
            throw new CryptoException(e);
        }

    }


    public static byte[] encrypt(byte[] raw, byte[] key) {
        return encrypt(raw, resizeKey(key), ivParameter.getBytes(StandardCharsets.UTF_8));
    }


    public static byte[] decrypt(byte[] encrypted, byte[] key, byte[] iv) throws CryptoException {
        try {
            PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
            CipherParameters params = new ParametersWithIV(new KeyParameter(key), iv);
            aes.init(false, params);

            return cipherData(aes, encrypted);
        } catch (DataLengthException | IllegalArgumentException | IllegalStateException
                 | InvalidCipherTextException e) {
            throw new CryptoException(e);
        }
    }


    public static byte[] decrypt(byte[] encrypted, byte[] key) {
        return decrypt(encrypted, resizeKey(key), ivParameter.getBytes(StandardCharsets.UTF_8));
    }


    public static byte[] resizeKey(final byte[] key) {
        int passLength = key.length;
        byte[] newKey = key;


        if (passLength > 32) {
            newKey = new byte[32];
            System.arraycopy(key, 0, newKey, 0, 32);
        } else if (passLength > 24) {
            newKey = new byte[24];
            System.arraycopy(key, 0, newKey, 0, 24);
        } else if (passLength > 16) {
            newKey = new byte[16];
            System.arraycopy(key, 0, newKey, 0, 16);
        } else if (passLength < 16) {
            newKey = new byte[16];
            System.arraycopy(key, 0, newKey, 0, passLength);
        }

        return newKey;
    }

}