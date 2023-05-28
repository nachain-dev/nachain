package org.nachain.libs.encrypt;

import java.security.MessageDigest;

public class MD5Base {


    public String getMD5ofStr(String str) {
        return getMD5ofStr(str, "UTF-8");
    }


    public String getMD5ofStr(String str, String charsetName) {
        StringBuffer sb = new StringBuffer(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes(charsetName));
            for (int i = 0; i < array.length; i++) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
            }
        } catch (Exception e) {
            System.err.println("Can not encode the string '" + str + "' to MD5!");
            e.printStackTrace();
            return null;

        }

        return sb.toString();
    }


}