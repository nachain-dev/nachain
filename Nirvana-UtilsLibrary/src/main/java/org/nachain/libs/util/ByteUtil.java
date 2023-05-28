package org.nachain.libs.util;

import java.io.IOException;

public class ByteUtil {

    public static char[] bin(byte b) {
        char[] charBits = toCharBits(b, 8);
        return charBits;
    }

    public static char[] bin(char b) {
        char[] charBits = toCharBits(b, 16);
        return charBits;
    }

    public static char[] bin(short b) {
        char[] charBits = toCharBits(b, 16);
        return charBits;
    }

    public static char[] bin(int b) {
        char[] charBits = toCharBits(b, 32);
        return charBits;
    }

    public static char[] bin(long b) {
        char[] charBits = toCharBits(b, 64);
        return charBits;
    }

    public static char[] bin(float a) {
        char[] charBits = toCharBits(Float.floatToIntBits(a), 32);
        return charBits;
    }

    public static char[] bin(double a) {
        char[] charBits = toCharBits(Double.doubleToLongBits(a), 64);
        return charBits;
    }


    public static String byte2Hex(byte[] bs) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();

    }


    public static byte[] hex2Byte(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }

        return bytes;
    }


    private static char[] toCharBits(long num, int size) {
        char[] charBits = new char[size];
        for (int i = size - 1; i >= 0; --i) {
            charBits[i] = (num & 1) == 0 ? '0' : '1';
            num >>>= 1;
        }
        return charBits;
    }


    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }


    public static byte[] int2bytes(int i) throws IOException {
        int len = 0;
        int index = 10;
        boolean isNegative = false;
        byte[] buff = new byte[11];
        if (i < 0) {
            i = 0 - i;
            len++;
            isNegative = true;
        }
        while (i != 0) {
            buff[index--] = (byte) ((i % 10 + 48) & 0xff);
            i /= 10;
            len++;
        }
        if (isNegative) {
            buff[index] = 45 & 0xff;
        }
        byte[] rs = new byte[len];
        System.arraycopy(buff, 11 - len, rs, 0, len);
        return rs;
    }


}