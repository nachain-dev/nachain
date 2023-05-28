package org.nachain.libs.encrypt;

import org.apache.commons.lang3.StringUtils;
import org.nachain.libs.global.LibsConfig;
import org.spongycastle.util.encoders.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 extends MD5Base {


    public static String toMD5(String source) {
        MD5 md5 = new MD5();
        return md5.getMD5ofStr(source);
    }


    public static String toMD5X2(String source) {
        String returnValue = source;
        MD5 md5 = new MD5();
        for (int i = 1; i <= 2; i++) {
            returnValue = md5.getMD5ofStr(returnValue).toLowerCase();
        }

        return againBySalt(returnValue);
    }

    private static String againBySalt(String returnValue) {

        String md5PswSalt = LibsConfig.Md5PswSalt;
        if (StringUtils.isNotBlank(md5PswSalt)) {
            returnValue = new MD5().getMD5ofStr(returnValue + md5PswSalt).toLowerCase();
        }
        return returnValue;
    }


    public static String toMD5Xn(String source, int n) {
        String returnValue = source;
        MD5 md5 = new MD5();
        for (int i = 1; i <= n; i++) {
            returnValue = md5.getMD5ofStr(returnValue);
        }


        returnValue = againBySalt(returnValue);

        return returnValue;
    }


    public static String toMD5LowerCase(String source) {
        MD5 md5 = new MD5();
        String value = md5.getMD5ofStr(source).toLowerCase();


        return againBySalt(value);
    }


    public static String toMD5LowerCaseX2(String source) {
        return toMD5LowerCaseXn(source, 2);
    }


    public static String toMD5LowerCaseXn(String source, int n) {
        String returnValue = source;
        MD5 md5 = new MD5();
        for (int i = 1; i <= n; i++) {
            returnValue = md5.getMD5ofStr(returnValue).toLowerCase();
        }


        returnValue = againBySalt(returnValue);

        return returnValue;
    }


    public static String updateOldMD5X2BySalt(String oldMD5X2Value) {
        return againBySalt(oldMD5X2Value);
    }


    public static String getFileMD5(File file) throws NoSuchAlgorithmException, IOException {

        MessageDigest MD5 = MessageDigest.getInstance("MD5");
        FileInputStream fileInputStream = new FileInputStream(file);

        byte[] buffer = new byte[8192];
        int length;
        while ((length = fileInputStream.read(buffer)) != -1) {
            MD5.update(buffer, 0, length);
        }

        return new String(Hex.encode(MD5.digest()));
    }


}