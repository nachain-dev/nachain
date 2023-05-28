package org.nachain.core.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;


public abstract class SHAUtils {


    public static byte[] encodeSHA(byte[] data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA");


        return md.digest(data);
    }


    public static String encodeSHAHex(byte[] data) throws Exception {

        return Hex.encodeHexString(encodeSHA(data));
    }


    public static byte[] encodeSHA256(byte[] data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");


        return md.digest(data);
    }


    public static String encodeSHA256Hex(byte[] data) throws Exception {

        return new String(Hex.encodeHex(encodeSHA256(data)));
    }


    public static byte[] encodeSHA384(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-384");

        return md.digest(data);
    }


    public static String encodeSHA384Hex(byte[] data) throws Exception {

        return new String(Hex.encodeHex(encodeSHA384(data)));
    }


    public static byte[] encodeSHA512(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        return md.digest(data);
    }


    public static String encodeSHA512Hex(byte[] data) throws Exception {

        return new String(Hex.encodeHex(encodeSHA512(data)));
    }

}