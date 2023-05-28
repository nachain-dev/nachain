package org.nachain.libs.shorturl;

import java.security.MessageDigest;

public class Encript {


    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};


    public static String md5(String inputStr) {
        return encodeByMD5(inputStr);
    }


    public static boolean authenticatePassword(String password, String inputString) {
        if (password.equals(encodeByMD5(inputString))) {
            return true;
        } else {
            return false;
        }

    }


    private static String encodeByMD5(String originString) {

        if (originString != null) {

            try {


                MessageDigest md5 = MessageDigest.getInstance("MD5");


                byte[] results = md5.digest(originString.getBytes());


                String result = byteArrayToHexString(results);

                return result;

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        return null;

    }


    private static String byteArrayToHexString(byte[] b) {

        StringBuffer resultSb = new StringBuffer();

        for (int i = 0; i < b.length; i++) {

            resultSb.append(byteToHexString(b[i]));

        }

        return resultSb.toString();

    }


    private static String byteToHexString(byte b) {

        int n = b;

        if (n < 0)

            n = 256 + n;

        int d1 = n / 16;

        int d2 = n % 16;

        return hexDigits[d1] + hexDigits[d2];

    }

}