package org.nachain.libs.encrypt.ecc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ECCBase {


    protected static final int KEY_SIZE = 256;


    protected static final String PUBLIC_KEY = "ECDSAPublicKey";


    protected static final String PRIVATE_KEY = "ECDSAPrivateKey";


    protected static final String PROVIDER = "BC";


    protected static String TRANSFORMATION = "";

    static {
        log.debug("static initialize");


        if (PROVIDER.equals("BC")) {

            Security.addProvider(new BouncyCastleProvider());

            TRANSFORMATION = "ECIES";
        } else {

            TRANSFORMATION = "";
        }
    }


    public static Map<String, Object> initKey(String KEY_ALGORITHM) throws NoSuchAlgorithmException {


        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM);


        kpg.initialize(KEY_SIZE, new SecureRandom());


        KeyPair keypair = kpg.generateKeyPair();
        ECPublicKey publicKey = (ECPublicKey) keypair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) keypair.getPrivate();


        Map<String, Object> map = new HashMap<String, Object>(2);

        map.put(PUBLIC_KEY, publicKey);
        map.put(PRIVATE_KEY, privateKey);

        return map;
    }


    public static byte[] getPrivateKey(Map<String, Object> keyMap) {

        Key key = (Key) keyMap.get(PRIVATE_KEY);

        return key.getEncoded();
    }

    public static String getHexPrivateKey(Map<String, Object> keyMap) {
        return encodeByHex(getPrivateKey(keyMap));
    }

    public static String getBase64PrivateKey(Map<String, Object> keyMap) {
        return encodeByBase64(getPrivateKey(keyMap));
    }


    public static byte[] getPublicKey(Map<String, Object> keyMap) {

        Key key = (Key) keyMap.get(PUBLIC_KEY);

        return key.getEncoded();
    }

    public static String getHexPublicKey(Map<String, Object> keyMap) {
        return encodeByHex(getPublicKey(keyMap));
    }

    public static String getBase64PublicKey(Map<String, Object> keyMap) {
        return encodeByBase64(getPublicKey(keyMap));
    }


    public static String encodeByHex(byte[] key) {
        return Hex.encodeHexString(key);
    }


    public static byte[] decodeByHex(String key) throws DecoderException {
        return Hex.decodeHex(key.toCharArray());
    }


    public static String encodeByBase64(byte[] key) {
        return Base64.encodeBase64String(key);
    }


    public static byte[] decodeByBase64(String key) {
        return Base64.decodeBase64(key);
    }

}