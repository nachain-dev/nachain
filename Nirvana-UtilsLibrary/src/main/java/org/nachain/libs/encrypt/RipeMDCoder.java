package org.nachain.libs.encrypt;

import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.util.encoders.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.Security;


public class RipeMDCoder {


    public static byte[] encodeRipeMD128(byte[] data) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        MessageDigest md = MessageDigest.getInstance("RipeMD128");

        return md.digest(data);

    }


    public static String encodeRipeMD128Hex(byte[] data) throws Exception {

        byte[] b = encodeRipeMD128(data);

        return new String(Hex.encode(b));
    }


    public static byte[] encodeRipeMD160(byte[] data) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        MessageDigest md = MessageDigest.getInstance("RipeMD160");

        return md.digest(data);

    }


    public static String encodeRipeMD160Hex(byte[] data) throws Exception {

        byte[] b = encodeRipeMD160(data);

        return new String(Hex.encode(b));
    }


    public static byte[] encodeRipeMD256(byte[] data) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        MessageDigest md = MessageDigest.getInstance("RipeMD256");

        return md.digest(data);

    }


    public static String encodeRipeMD256Hex(byte[] data) throws Exception {

        byte[] b = encodeRipeMD256(data);

        return new String(Hex.encode(b));
    }


    public static byte[] encodeRipeMD320(byte[] data) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        MessageDigest md = MessageDigest.getInstance("RipeMD320");

        return md.digest(data);

    }


    public static String encodeRipeMD320Hex(byte[] data) throws Exception {

        byte[] b = encodeRipeMD320(data);

        return new String(Hex.encode(b));
    }


    public static byte[] initHmacRipeMD128Key() throws Exception {


        Security.addProvider(new BouncyCastleProvider());

        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacRipeMD128");

        SecretKey secretKey = keyGenerator.generateKey();

        return secretKey.getEncoded();
    }


    public static byte[] encodeHmacRipeMD128(byte[] data, byte[] key) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        SecretKey secretKey = new SecretKeySpec(key, "HmacRipeMD128");

        Mac mac = Mac.getInstance(secretKey.getAlgorithm());

        mac.init(secretKey);

        return mac.doFinal(data);
    }


    public static String encodeHmacRipeMD128Hex(byte[] data, byte[] key) throws Exception {

        byte[] b = encodeHmacRipeMD128(data, key);

        return new String(Hex.encode(b));
    }


    public static byte[] initHmacRipeMD160Key() throws Exception {


        Security.addProvider(new BouncyCastleProvider());

        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacRipeMD160");

        SecretKey secretKey = keyGenerator.generateKey();

        return secretKey.getEncoded();
    }


    public static byte[] encodeHmacRipeMD160(byte[] data, byte[] key) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        SecretKey secretKey = new SecretKeySpec(key, "HmacRipeMD160");

        Mac mac = Mac.getInstance(secretKey.getAlgorithm());

        mac.init(secretKey);

        return mac.doFinal(data);
    }


    public static String encodeHmacRipeMD160Hex(byte[] data, byte[] key) throws Exception {

        byte[] b = encodeHmacRipeMD160(data, key);

        return new String(Hex.encode(b));
    }

}