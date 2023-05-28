package org.nachain.libs.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MathUtil {


    public static boolean isNumeric(String str) {
        int begin = 0;
        boolean once = true;
        if (str == null || str.trim().equals("")) {
            return false;
        }
        str = str.trim();
        if (str.startsWith("+") || str.startsWith("-")) {
            if (str.length() == 1) {

                return false;
            }
            begin = 1;
        }
        for (int i = begin; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                if (str.charAt(i) == '.' && once) {

                    once = false;
                } else {
                    return false;
                }
            }
        }
        if (str.length() == (begin + 1) && !once) {

            return false;
        }
        return true;
    }


    public static boolean isInteger(String str) {
        int begin = 0;
        if (str == null || str.trim().equals("")) {
            return false;
        }
        str = str.trim();
        if (str.startsWith("+") || str.startsWith("-")) {
            if (str.length() == 1) {

                return false;
            }
            begin = 1;
        }
        for (int i = begin; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static boolean isNumericEx(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }


    public static boolean isIntegerEx(String str) {
        str = str.trim();
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            if (str.startsWith("+")) {
                return isIntegerEx(str.substring(1));
            }
            return false;
        }
    }


    public static String formatByComma(double d) {
        String returnValue = "";

        DecimalFormat nf = new DecimalFormat("###,##0.##");
        returnValue = nf.format(d);

        return returnValue;
    }


    public static String clearNumberZero(String str) {
        String returnValue = str;

        if (returnValue.endsWith(".0")) {
            returnValue = returnValue.substring(0, returnValue.indexOf("."));
        }

        return returnValue;
    }

    public static String clearNumberZero(double num) {
        return clearNumberZero(String.valueOf(num));
    }


    public static String clearNumberDecimal(String str) {
        String returnValue = str;

        if (str.contains("."))
            returnValue = returnValue.substring(0, str.indexOf("."));

        return returnValue;
    }

    public static String clearNumberDecimal(double num) {
        return clearNumberDecimal(String.valueOf(num));
    }


    public static boolean isPrime(int x) {
        if (x <= 7) {
            if (x == 2 || x == 3 || x == 5 || x == 7)
                return true;
        }
        int c = 7;
        if (x % 2 == 0)
            return false;
        if (x % 3 == 0)
            return false;
        if (x % 5 == 0)
            return false;
        int end = (int) Math.sqrt(x);
        while (c <= end) {
            if (x % c == 0) {
                return false;
            }
            c += 4;
            if (x % c == 0) {
                return false;
            }
            c += 2;
            if (x % c == 0) {
                return false;
            }
            c += 4;
            if (x % c == 0) {
                return false;
            }
            c += 2;
            if (x % c == 0) {
                return false;
            }
            c += 4;
            if (x % c == 0) {
                return false;
            }
            c += 6;
            if (x % c == 0) {
                return false;
            }
            c += 2;
            if (x % c == 0) {
                return false;
            }
            c += 6;
        }
        return true;
    }


    public boolean isPrimes(int n) {
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }


    public static int intelligentize2Int(String value) {
        int returnValue = 0;

        if (value != null) {
            value = value.trim();
            if (!value.equals("")) {
                try {
                    returnValue = Integer.parseInt(value);
                } catch (NumberFormatException e) {

                    int length = value.length();
                    String newValue = "";
                    String singleChar = "";
                    for (int i = 0; i < length; i++) {
                        singleChar = value.substring(i, i + 1);
                        if (isInteger(singleChar)) {
                            newValue += singleChar;
                        } else {
                            break;
                        }
                    }

                    if (!newValue.equals("")) {
                        returnValue = Integer.parseInt(newValue);
                    }
                }
            }
        }

        return returnValue;
    }


    private static final int DEF_DIV_SCALE = 10;


    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double jia(double v1, double v2) {
        return add(v1, v2);
    }


    public static double subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double jian(double v1, double v2) {
        return subtract(v1, v2);
    }


    public static double multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static double cheng(double v1, double v2) {
        return multiply(v1, v2);
    }

    public static long multiply(long v1, double v2) {
        return (long) multiply((double) v1, v2);
    }

    public static long cheng(long v1, double v2) {
        return (long) multiply((double) v1, v2);
    }


    public static double divide(double v1, double v2) {
        return divide(v1, v2, DEF_DIV_SCALE);
    }

    public static double chu(double v1, double v2) {
        return divide(v1, v2);
    }

    public static long divide(long v1, double v2) {
        double v1_d = (double) v1;
        return (long) divide(v1_d, v2);
    }

    public static long chu(long v1, double v2) {
        return (long) divide(v1, v2);
    }


    public static double divide(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("scale must be greater than or equal to zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double chu(double v1, double v2, int scale) {
        return divide(v1, v2, scale);
    }


    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("scale must be greater than or equal to zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static double floor(double v, int scale) {
        double rtv = v;

        int percent = 0;

        if (scale == 0) {
            return Math.floor(rtv);
        } else {

            percent = CommUtil.null2Int("1" + CommUtil.reduplicate("0", scale));
        }

        rtv = rtv * percent;
        rtv = Math.floor(rtv);
        rtv = rtv / percent;

        return rtv;
    }


    public static BigDecimal formatMoney(long money, int decimal, int decimalLength) {

        BigDecimal bd1 = new BigDecimal(money);

        BigDecimal bd2 = new BigDecimal(decimal);

        BigDecimal bd3 = bd1.divide(bd2, decimalLength, BigDecimal.ROUND_DOWN);

        bd3 = bd3.setScale(decimalLength, BigDecimal.ROUND_DOWN);

        return bd3;
    }


    public static BigDecimal formatDouble(double v, int decimalLength) {

        BigDecimal bd1 = new BigDecimal(v);

        BigDecimal bd2 = new BigDecimal(1);

        BigDecimal bd3 = bd1.divide(bd2, decimalLength, BigDecimal.ROUND_DOWN);

        bd3 = bd3.setScale(decimalLength, BigDecimal.ROUND_DOWN);

        return bd3;
    }


    public static String formatDouble2String(double v, int decimalLength) {
        return formatDouble(v, decimalLength).toPlainString();
    }


    public static String toPlainString(double v) {
        BigDecimal bd = new BigDecimal(v);

        return bd.toPlainString();
    }


}