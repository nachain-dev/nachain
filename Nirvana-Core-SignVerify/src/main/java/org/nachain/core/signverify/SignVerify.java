package org.nachain.core.signverify;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.sign.IMinedSignObject;
import org.nachain.core.crypto.Key;
import org.nachain.core.util.Hex;
import org.nachain.core.wallet.WalletUtils;

import java.util.Arrays;


@Slf4j
public class SignVerify {


    public static byte[] sign(IMinedSignObject b, Key key) throws Exception {

        return sign(b.toMinedSignString(), key);
    }


    public static byte[] sign(String signString, Key key) {
        if (key == null) {
            throw new ChainException("The private key cannot be null.");
        }

        return key.signHash256(signString.getBytes()).toBytes();
    }


    public static String signHexString(IMinedSignObject b, Key key) throws Exception {
        return Hex.encode0x(sign(b, key));
    }

    public static String signHexString(String signString, Key key) {
        return Hex.encode0x(sign(signString, key));
    }


    public static boolean verifySign(IMinedSignObject b, byte[] publicKey) {

        return verifySign(b.getMinedSign(), publicKey);
    }


    public static boolean verifySign(String sign, byte[] publicKey) {

        byte[] unSignData = Hex.decode0x(sign);


        Key.Signature signature = Key.Signature.fromBytes(unSignData);


        return Arrays.equals(signature.getPublicKey(), publicKey);
    }


    public static boolean verifySign(String sign, String wallet) {

        String walletAddress = getWalletAddress(sign);


        return walletAddress.equals(wallet);
    }


    public static String getWalletAddress(String sign) {
        String walletAddress = "";

        try {

            byte[] unSignData = Hex.decode0x(sign);


            Key.Signature signature = Key.Signature.fromBytes(unSignData);
            byte[] publicKey = signature.getPublicKey();


            walletAddress = WalletUtils.generateWalletAddress(publicKey);
        } catch (Exception e) {
            log.error("Get wallet address error:" + sign, e);
            throw new RuntimeException(e);
        }


        return walletAddress;
    }


}
