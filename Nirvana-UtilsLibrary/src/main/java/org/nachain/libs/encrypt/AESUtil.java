package org.nachain.libs.encrypt;

import org.spongycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {


    private static String ivParameter = "0392039203920300";


    public static String encrypt(String sSrc, String password) throws Exception {


        if (password.length() != 16) {
            throw new IllegalArgumentException("The password must contain 16 characters. The current password is " + password.length());
        }

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = password.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return Base64.toBase64String(encrypted);
    }


    public static String encrypt(String encData, String secretKey, String vector) throws Exception {

        if (secretKey == null) {
            throw new IllegalArgumentException("SecretKey The key cannot be empty.");
        }
        if (secretKey.length() != 16) {
            throw new IllegalArgumentException("SecretKey The length of the SecretKey must be 16 bits.");
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
        return Base64.toBase64String(encrypted);
    }


    public static String decrypt(String sSrc, String password) throws Exception {


        if (password.length() != 16) {
            throw new IllegalArgumentException("The password must contain 16 characters. The current password is " + password.length());
        }

        byte[] raw = password.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = Base64.decode(sSrc);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, "utf-8");
        return originalString;
    }


    public static String decrypt(String sSrc, String SecretKey, String ivs) throws Exception {
        if (SecretKey == null) {
            throw new IllegalArgumentException("The key cannot be empty!");
        }
        if (SecretKey.length() != 16) {
            throw new IllegalArgumentException("SecretKey The length of the SecretKey must be 16 bits.");
        }
        byte[] raw = SecretKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = Base64.decode(sSrc);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, "utf-8");

        return originalString;
    }


    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }


}