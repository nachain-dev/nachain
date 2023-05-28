package org.nachain.libs.encrypt;

import org.spongycastle.util.encoders.Hex;


public class HexUtil {


    public static String toHexString(byte[] data) {

        if (data == null)
            return "";

        return Hex.toHexString(data);
    }


    public static byte[] toBytes(String hexString) {

        if (hexString == null)
            return new byte[0];

        return Hex.decode(hexString);
    }
}