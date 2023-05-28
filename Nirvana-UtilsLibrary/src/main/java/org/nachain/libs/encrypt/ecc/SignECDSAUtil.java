package org.nachain.libs.encrypt.ecc;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;


public abstract class SignECDSAUtil extends ECCBase {


    private static final String KEY_ALGORITHM = "ECDSA";


    private static final String SIGNATURE_ALGORITHM = "SHA512withECDSA";


    public static Map<String, Object> initKey() throws NoSuchAlgorithmException {
        return ECCBase.initKey(KEY_ALGORITHM);
    }


    public static byte[] sign(byte[] data, byte[] privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {


        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);


        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);


        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);


        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);


        signature.initSign(priKey);


        signature.update(data);


        return signature.sign();
    }


    public static boolean verify(byte[] unSignData, byte[] publicKey, byte[] sign) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {


        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);


        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);


        PublicKey pubKey = keyFactory.generatePublic(keySpec);


        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);


        signature.initVerify(pubKey);


        signature.update(unSignData);


        return signature.verify(sign);
    }


}