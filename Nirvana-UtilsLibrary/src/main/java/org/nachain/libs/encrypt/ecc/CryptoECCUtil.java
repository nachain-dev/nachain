package org.nachain.libs.encrypt.ecc;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;


public class CryptoECCUtil extends ECCBase {


    public static final String KEY_ALGORITHM = "EC";


    public static Map<String, Object> initKey() throws NoSuchAlgorithmException {
        return initKey(KEY_ALGORITHM);
    }


    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, PROVIDER);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        ECPublicKey pubKey = (ECPublicKey) keyFactory.generatePublic(x509KeySpec);


        Cipher encrypter = Cipher.getInstance(TRANSFORMATION, keyFactory.getProvider());
        try {
            encrypter.init(Cipher.ENCRYPT_MODE, pubKey);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("If the key is greater than 128 (192 or 256), please overwrite the local_policy.jar and us_export_policy. jar files in the JRE because of the export restrictions of software encryption in the United States  If the key is greater than 128 (192 or 256), please overwrite the local_policy.jar and us_export_policy. jar files in the JRE because of the export restrictions of software encryption in the United States", e);
        }
        return encrypter.doFinal(data);
    }

    public static byte[] encryptByBase64PublicKey(byte[] data, String publicKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        byte[] keyBytes = decodeByBase64(publicKey);

        return encryptByPublicKey(data, keyBytes);
    }


    public static byte[] decryptByPrivateKey(byte[] data, byte[] privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, PROVIDER);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        ECPrivateKey priKey = (ECPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);


        Cipher decrypter = Cipher.getInstance(TRANSFORMATION, keyFactory.getProvider());
        decrypter.init(Cipher.DECRYPT_MODE, priKey);

        return decrypter.doFinal(data);
    }

    public static byte[] decryptByPrivateKey(byte[] data, String privateKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        byte[] keyBytes = decodeByBase64(privateKey);

        return decryptByPrivateKey(data, keyBytes);
    }


}