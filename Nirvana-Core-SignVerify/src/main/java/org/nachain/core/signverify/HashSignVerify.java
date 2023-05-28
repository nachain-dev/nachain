package org.nachain.core.signverify;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.nachain.core.chain.sign.IMinedSignObject;
import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;

import java.util.Arrays;


@Slf4j
public class HashSignVerify {


    public static byte[] sign(IMinedSignObject b, String signer) throws Exception {
        return signData(b, signer);
    }


    public static byte[] sign(String signString, String signer) {
        return signData(signString, signer);
    }


    public static String signHexString(IMinedSignObject b, String signer) throws Exception {
        return Hex.encode0x(sign(b, signer));
    }


    public static String signHexString(String signString, String signer) {
        return Hex.encode0x(sign(signString, signer));
    }


    private static byte[] signData(IMinedSignObject b, String signer) throws Exception {
        return signData(b.toMinedSignString(), signer);
    }


    private static byte[] signData(String signString, String signer) {

        String signSource = signString + "Signer{" + signer + "}";


        byte[] signData = Hash.h256(signSource.getBytes());

        return signData;
    }


    public static boolean verifySign(IMinedSignObject b, String signer) throws Exception {
        return verifySign(b.toMinedSignString(), b.getMinedSign(), signer);
    }


    public static boolean verifySign(String signString, String sign, String signer) {

        byte[] signData = signData(signString, signer);


        String signHex = StringUtils.defaultString(sign);


        byte[] txSign = Hex.decode0x(signHex);


        return Arrays.equals(signData, txSign);
    }


}
