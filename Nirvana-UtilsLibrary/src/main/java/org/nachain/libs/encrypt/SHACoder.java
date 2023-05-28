package org.nachain.libs.encrypt;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;


public abstract class SHACoder {


    public static byte[] encodeSHA(byte[] data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA");


        return md.digest(data);
    }


    public static String encodeSHAHex(byte[] data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA");


        return Hex.encodeHexString(md.digest(data));
    }


    public static byte[] encodeSHA256(byte[] data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");


        return md.digest(data);
    }


    public static String encodeSHA256Hex(byte[] data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");


        return new String(Hex.encodeHex(md.digest(data)));
    }


    public static byte[] encodeSHA384(byte[] data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-384");


        return md.digest(data);
    }


    public static String encodeSHA384Hex(byte[] data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-384");


        return new String(Hex.encodeHex(md.digest(data)));
    }


    public static byte[] encodeSHA512(byte[] data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-512");


        return md.digest(data);
    }


    public static String encodeSHA512Hex(byte[] data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-512");


        return new String(Hex.encodeHex(md.digest(data)));
    }

}