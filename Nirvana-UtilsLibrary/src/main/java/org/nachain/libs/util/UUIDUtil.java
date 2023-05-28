package org.nachain.libs.util;

import org.apache.commons.net.util.Base64;
import org.nachain.libs.encode.Base58Util;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDUtil {

    public static class UUID8 {
        public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


        public static String getUUID() {
            StringBuffer stringBuffer = new StringBuffer();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            for (int i = 0; i < 8; i++) {
                String str = uuid.substring(i * 4, i * 4 + 4);
                int strInteger = Integer.parseInt(str, 16);
                stringBuffer.append(chars[strInteger % 0x3E]);
            }

            return stringBuffer.toString();
        }
    }

    public static class UUID19 {

        final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        final static Map<Character, Integer> digitMap = new HashMap<Character, Integer>();

        static {
            for (int i = 0; i < digits.length; i++) {
                digitMap.put(digits[i], (int) i);
            }
        }


        public static final int MAX_RADIX = digits.length;


        public static final int MIN_RADIX = 2;


        public static String toString(long i, int radix) {
            if (radix < MIN_RADIX || radix > MAX_RADIX)
                radix = 10;
            if (radix == 10)
                return Long.toString(i);

            final int size = 65;
            int charPos = 64;

            char[] buf = new char[size];
            boolean negative = (i < 0);

            if (!negative) {
                i = -i;
            }

            while (i <= -radix) {
                buf[charPos--] = digits[(int) (-(i % radix))];
                i = i / radix;
            }
            buf[charPos] = digits[(int) (-i)];

            if (negative) {
                buf[--charPos] = '-';
            }

            return new String(buf, charPos, (size - charPos));
        }

        static NumberFormatException forInputString(String s) {
            return new NumberFormatException("For input string: \"" + s + "\"");
        }


        public static long toNumber(String s, int radix) {
            if (s == null) {
                throw new NumberFormatException("null");
            }

            if (radix < MIN_RADIX) {
                throw new NumberFormatException("radix " + radix + " less than Numbers.MIN_RADIX");
            }
            if (radix > MAX_RADIX) {
                throw new NumberFormatException("radix " + radix + " greater than Numbers.MAX_RADIX");
            }

            long result = 0;
            boolean negative = false;
            int i = 0, len = s.length();
            long limit = -Long.MAX_VALUE;
            long multmin;
            Integer digit;

            if (len > 0) {
                char firstChar = s.charAt(0);
                if (firstChar < '0') {
                    if (firstChar == '-') {
                        negative = true;
                        limit = Long.MIN_VALUE;
                    } else if (firstChar != '+')
                        throw forInputString(s);

                    if (len == 1) {
                        throw forInputString(s);
                    }
                    i++;
                }
                multmin = limit / radix;
                while (i < len) {
                    digit = digitMap.get(s.charAt(i++));
                    if (digit == null) {
                        throw forInputString(s);
                    }
                    if (digit < 0) {
                        throw forInputString(s);
                    }
                    if (result < multmin) {
                        throw forInputString(s);
                    }
                    result *= radix;
                    if (result < limit + digit) {
                        throw forInputString(s);
                    }
                    result -= digit;
                }
            } else {
                throw forInputString(s);
            }
            return negative ? result : -result;
        }

        private static String digits(long val, int digits) {
            long hi = 1L << (digits * 4);
            return UUID19.toString(hi | (val & (hi - 1)), UUID19.MAX_RADIX).substring(1);
        }


        public static String getUUID() {
            UUID uuid = UUID.randomUUID();
            StringBuilder sb = new StringBuilder();
            sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
            sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
            sb.append(digits(uuid.getMostSignificantBits(), 4));
            sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
            sb.append(digits(uuid.getLeastSignificantBits(), 12));
            return sb.toString();
        }

    }


    public static class UUID22 {

        public static String getUUID() {
            UUID uuid = UUID.randomUUID();
            return compressedUUID(uuid);
        }

        public static String uuidTo22(String uuidString) {
            UUID uuid = UUID.fromString(uuidString);
            return compressedUUID(uuid);
        }

        public static String unUUID22(String compressedUuid) {
            if (compressedUuid.length() != 22) {
                throw new IllegalArgumentException("Invalid uuid!");
            }
            byte[] byUuid = Base64.decodeBase64(compressedUuid + "==");
            long most = bytes2long(byUuid, 0);
            long least = bytes2long(byUuid, 8);
            UUID uuid = new UUID(most, least);
            return uuid.toString();
        }

        protected static String compressedUUID(UUID uuid) {
            byte[] byUuid = new byte[16];
            long least = uuid.getLeastSignificantBits();
            long most = uuid.getMostSignificantBits();
            long2bytes(most, byUuid, 0);
            long2bytes(least, byUuid, 8);
            String compressUUID = Base64.encodeBase64URLSafeString(byUuid);
            return compressUUID;
        }

        protected static void long2bytes(long value, byte[] bytes, int offset) {
            for (int i = 7; i > -1; i--) {
                bytes[offset++] = (byte) ((value >> 8 * i) & 0xFF);
            }
        }

        protected static long bytes2long(byte[] bytes, int offset) {
            long value = 0;
            for (int i = 7; i > -1; i--) {
                value |= (((long) bytes[offset++]) & 0xFF) << 8 * i;
            }
            return value;
        }
    }

    public static String getUUID22() {
        return UUID22.getUUID();
    }

    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String base64Uuid() {
        UUID uuid = UUID.randomUUID();
        return base64Uuid(uuid);
    }

    protected static String base64Uuid(UUID uuid) {

        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return Base64.encodeBase64URLSafeString(bb.array());
    }

    public static String encodeBase64Uuid(String uuidString) {
        UUID uuid = UUID.fromString(uuidString);
        return base64Uuid(uuid);
    }

    public static String decodeBase64Uuid(String compressedUuid) {

        byte[] byUuid = Base64.decodeBase64(compressedUuid);

        ByteBuffer bb = ByteBuffer.wrap(byUuid);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid.toString();
    }

    public static String base58Uuid() {
        UUID uuid = UUID.randomUUID();
        return base58Uuid(uuid);
    }

    protected static String base58Uuid(UUID uuid) {

        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return Base58Util.encode(bb.array());
    }

    public static String encodeBase58Uuid(String uuidString) {
        UUID uuid = UUID.fromString(uuidString);
        return base58Uuid(uuid);
    }

    public static String decodeBase58Uuid(String base58uuid) {
        byte[] byUuid = Base58Util.decode(base58uuid);
        ByteBuffer bb = ByteBuffer.wrap(byUuid);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid.toString();
    }


}