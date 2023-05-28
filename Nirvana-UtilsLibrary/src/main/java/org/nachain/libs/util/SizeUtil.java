package org.nachain.libs.util;

public class SizeUtil {


    public static String simplifySpace(long size) {
        String returnValue = "";

        if (size / 1024 / 1024 / 1024 > 1) {
            returnValue = size / 1024 / 1024 / 1024 + " GB";
        } else if (size / 1024 / 1024 > 1) {
            returnValue = size / 1024 / 1024 + " MB";
        } else if (size / 1024 > 1) {
            returnValue = size / 1024 + " KB";
        } else {
            returnValue = size + " B";
        }

        return returnValue;
    }

}
