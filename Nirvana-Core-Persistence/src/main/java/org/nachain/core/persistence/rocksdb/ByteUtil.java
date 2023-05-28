package org.nachain.core.persistence.rocksdb;

import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedBytes;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteUtil {

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final byte[] ZERO_BYTE_ARRAY = new byte[]{0};


    public static byte[] cloneBytes(byte[] data) {
        if (data == null) {
            return null;
        }

        int length = data.length;
        byte[] rc = new byte[length];
        if (length > 0) {
            System.arraycopy(data, 0, rc, 0, length);
        }
        return rc;
    }


    public static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
        if (b == null) {
            return null;
        }
        byte[] bytes = new byte[numBytes];
        byte[] biBytes = b.toByteArray();
        int start = (biBytes.length == numBytes + 1) ? 1 : 0;
        int length = Math.min(biBytes.length, numBytes);
        System.arraycopy(biBytes, start, bytes, numBytes - length, length);
        return bytes;
    }


    public static byte[] bigIntegerToBytes(BigInteger value) {
        if (value == null) {
            return null;
        }

        byte[] data = value.toByteArray();

        if (data.length != 1 && data[0] == 0) {
            byte[] tmp = new byte[data.length - 1];
            System.arraycopy(data, 1, tmp, 0, tmp.length);
            data = tmp;
        }
        return data;
    }


    public static byte[] merge(byte[]... arrays) {
        int count = 0;
        for (byte[] array : arrays) {
            count += array.length;
        }


        byte[] mergedArray = new byte[count];
        int start = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, mergedArray, start, array.length);
            start += array.length;
        }
        return mergedArray;
    }


    public static byte[] appendByte(byte[] bytes, byte b) {
        byte[] result = Arrays.copyOf(bytes, bytes.length + 1);
        result[result.length - 1] = b;
        return result;
    }


    public static String nibblesToPrettyString(byte[] nibbles) {
        StringBuilder builder = new StringBuilder();
        for (byte nibble : nibbles) {
            final String nibbleString = oneByteToHexString(nibble);
            builder.append("\\x").append(nibbleString);
        }
        return builder.toString();
    }


    public static String oneByteToHexString(byte value) {
        String retVal = Integer.toString(value & 0xFF, 16);
        if (retVal.length() == 1) {
            retVal = "0" + retVal;
        }
        return retVal;
    }


    public static String toHexString(byte[] data) {
        return data == null ? "" : Hex.toHexString(data);
    }


    public static int byteArrayToInt(byte[] b) {
        if (b == null || b.length == 0) {
            return 0;
        }
        return new BigInteger(1, b).intValue();
    }

    public static boolean isSingleZero(byte[] array) {
        return (array.length == 1 && array[0] == 0);
    }


    public static byte[] intToBytesNoLeadZeroes(int val) {

        if (val == 0) {
            return EMPTY_BYTE_ARRAY;
        }

        int lenght = 0;

        int tmpVal = val;
        while (tmpVal != 0) {
            tmpVal = tmpVal >>> 8;
            ++lenght;
        }

        byte[] result = new byte[lenght];

        int index = result.length - 1;
        while (val != 0) {

            result[index] = (byte) (val & 0xFF);
            val = val >>> 8;
            index -= 1;
        }

        return result;
    }


    public static byte[] intToBytes(int val) {
        return ByteBuffer.allocate(4).putInt(val).array();
    }


    public static BigInteger bytesToBigInteger(byte[] bb) {
        return (bb == null || bb.length == 0) ? BigInteger.ZERO : new BigInteger(1, bb);
    }


    public static long byteArrayToLong(byte[] b) {
        if (b == null || b.length == 0) {
            return 0;
        }
        return new BigInteger(1, b).longValueExact();
    }

    public static int firstNonZeroByte(byte[] data) {
        for (int i = 0; i < data.length; ++i) {
            if (data[i] != 0) {
                return i;
            }
        }
        return -1;
    }

    public static byte[] stripLeadingZeroes(byte[] data) {

        if (data == null) {
            return null;
        }

        final int firstNonZero = firstNonZeroByte(data);
        switch (firstNonZero) {
            case -1:
                return ZERO_BYTE_ARRAY;

            case 0:
                return data;

            default:
                byte[] result = new byte[data.length - firstNonZero];
                System.arraycopy(data, firstNonZero, result, 0, data.length - firstNonZero);

                return result;
        }
    }


    public static byte[] copyToArray(BigInteger value) {
        byte[] dest = ByteBuffer.allocate(32).array();
        byte[] src = ByteUtil.bigIntegerToBytes(value);
        if (src != null) {
            System.arraycopy(src, 0, dest, dest.length - src.length, src.length);
        }
        return dest;
    }


    public static int numberOfLeadingZeros(byte[] bytes) {

        int i = firstNonZeroByte(bytes);

        if (i == -1) {
            return bytes.length * 8;
        } else {
            int byteLeadingZeros = Integer.numberOfLeadingZeros((int) bytes[i] & 0xff) - 24;
            return i * 8 + byteLeadingZeros;
        }
    }


    public static byte[] parseBytes(byte[] input, int offset, int len) {

        if (offset >= input.length || len == 0) {
            return EMPTY_BYTE_ARRAY;
        }

        byte[] bytes = new byte[len];
        System.arraycopy(input, offset, bytes, 0, Math.min(input.length - offset, len));
        return bytes;
    }


    public static byte[] parseWord(byte[] input, int idx) {
        return parseBytes(input, 32 * idx, 32);
    }


    public static byte[] parseWord(byte[] input, int offset, int idx) {
        return parseBytes(input, offset + 32 * idx, 32);
    }

    public static boolean greater(byte[] bytes1, byte[] bytes2) {
        return compare(bytes1, bytes2) > 0;
    }

    public static boolean greaterOrEquals(byte[] bytes1, byte[] bytes2) {
        return compare(bytes1, bytes2) >= 0;
    }

    public static boolean less(byte[] bytes1, byte[] bytes2) {
        return compare(bytes1, bytes2) < 0;
    }

    public static boolean lessOrEquals(byte[] bytes1, byte[] bytes2) {
        return compare(bytes1, bytes2) <= 0;
    }

    public static boolean equals(byte[] bytes1, byte[] bytes2) {
        return compare(bytes1, bytes2) == 0;
    }

    public static boolean isNullOrZeroArray(byte[] array) {
        return (array == null) || (array.length == 0);
    }


    public static int compare(byte[] bytes1, byte[] bytes2) {
        Preconditions.checkNotNull(bytes1);
        Preconditions.checkNotNull(bytes2);
        Preconditions.checkArgument(bytes1.length == bytes2.length);
        int length = bytes1.length;
        for (int i = 0; i < length; ++i) {
            int ret = UnsignedBytes.compare(bytes1[i], bytes2[i]);
            if (ret != 0) {
                return ret;
            }
        }

        return 0;
    }

}