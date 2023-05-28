package org.nachain.core.crypto.bip32.util;

public class BitUtil {


    public static boolean checkBit(byte data, int index) {
        byte bit = (byte) ((data >> (8 - index)) & 1);
        return bit == 0x1;
    }


    public static byte setBit(byte data, int index) {
        data |= 1 << (8 - index);
        return data;
    }


    public static byte unsetBit(byte data, int index) {
        data &= ~(1 << (8 - index));
        return data;
    }
}
