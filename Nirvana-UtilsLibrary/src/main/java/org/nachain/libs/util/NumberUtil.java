package org.nachain.libs.util;

public class NumberUtil {


    public static String int2Hex(int num) {
        return Integer.toHexString(num);
    }

    public static String long2Hex(long num) {
        return Long.toHexString(num);
    }


    public static int hex2Int(String num) {
        return Integer.valueOf(num, 16);
    }

    public static long hex2Long(String num) {
        return Long.valueOf(num, 16);
    }


}