package org.nachain.core.crypto.bip32.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Stream;


public class BytesUtil {


    public static byte[] ser32(long i) {
        byte[] ser = new byte[4];
        ser[0] = (byte) (i >> 24);
        ser[1] = (byte) (i >> 16);
        ser[2] = (byte) (i >> 8);
        ser[3] = (byte) (i);
        return ser;
    }


    public static byte[] ser32LE(long i) {
        byte[] ser = new byte[4];
        ser[3] = (byte) (i >> 24);
        ser[2] = (byte) (i >> 16);
        ser[1] = (byte) (i >> 8);
        ser[0] = (byte) (i);
        return ser;
    }


    public static byte[] ser256(BigInteger p) {
        byte[] byteArray = p.toByteArray();
        byte[] ret = new byte[32];


        Arrays.fill(ret, (byte) 0);


        if (byteArray.length <= ret.length) {
            System.arraycopy(byteArray, 0, ret, ret.length - byteArray.length, byteArray.length);
        } else {
            System.arraycopy(byteArray, byteArray.length - ret.length, ret, 0, ret.length);
        }

        return ret;
    }


    public static BigInteger parse256(byte[] p) {
        return new BigInteger(1, p);
    }


    public static byte[] merge(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }


    public static byte[] merge(byte[]... arrays) {
        int total = Stream.of(arrays).mapToInt(a -> a.length).sum();

        byte[] buffer = new byte[total];
        int start = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, buffer, start, array.length);
            start += array.length;
        }

        return buffer;
    }


    public static byte[] getFingerprint(byte[] keyData) {
        byte[] point = Secp256k1.serP(Secp256k1.point(BytesUtil.parse256(keyData)));
        byte[] h160 = HashUtil.h160(point);
        return new byte[]{h160[0], h160[1], h160[2], h160[3]};
    }
}
