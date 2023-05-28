package org.nachain.libs.encrypt;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class CryptoAESUtil {


    private static final String Algorithm = "AES";


    private static final String SecureRandom_Algorithm = "SHA1PRNG";


    private final static int KEY_SIZE = 128;


    private static SecretKeySpec generatorKey(byte[] password) throws NoSuchAlgorithmException {

        byte[] Password_16Byte = new byte[16];

        for (int i = 0; i < password.length && i < Password_16Byte.length; i++) {
            Password_16Byte[i] = password[i];
        }


        SecureRandom random = SecureRandom.getInstance(SecureRandom_Algorithm);
        random.setSeed(password);


        KeyGenerator kgen = KeyGenerator.getInstance(Algorithm);
        kgen.init(KEY_SIZE, random);

        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, Algorithm);

        return key;
    }


    public static byte[] encrypt(byte[] byteContent, byte[] password) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeyException {


        SecretKeySpec key = generatorKey(password);


        Cipher cipher = Cipher.getInstance(Algorithm);

        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] result = cipher.doFinal(byteContent);

        return result;
    }


    public static byte[] encrypt(String content, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeyException {
        return encrypt(content.getBytes("utf-8"), password.getBytes());
    }


    public static byte[] decrypt(byte[] content, byte[] password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {


        SecretKeySpec key = generatorKey(password);


        Cipher cipher = Cipher.getInstance(Algorithm);

        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] result = cipher.doFinal(content);

        return result;
    }


    public static byte[] decrypt(byte[] content, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeyException {
        return decrypt(content, password.getBytes());
    }


}