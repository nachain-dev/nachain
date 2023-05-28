package org.nachain.core.util;

import org.apache.commons.lang3.StringUtils;
import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommUtils {


    private static long timeTravel;

    {

        timeTravel = 0;
    }


    public static byte[] byteMerger(byte[] byteA, byte[] byteB) {
        byte[] byteNew = new byte[byteA.length + byteB.length];
        System.arraycopy(byteA, 0, byteNew, 0, byteA.length);
        System.arraycopy(byteB, 0, byteNew, byteA.length, byteB.length);
        return byteNew;
    }


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


    public static long currentTimeMillis() {

        if (SystemUtils.isAndroidRuntime()) {
            return System.currentTimeMillis() + timeTravel;
        } else {
            return Timestamp.valueOf(LocalDateTime.now()).getTime() + timeTravel;
        }
    }


    public static long currentTimeMillisNoTimeTravel() {

        if (SystemUtils.isAndroidRuntime()) {
            return System.currentTimeMillis();
        } else {
            return Timestamp.valueOf(LocalDateTime.now()).getTime();
        }
    }


    public static void timeTravel(long millisecond) {
        timeTravel += millisecond;
    }

    public static void timeTravelSecond(long second) {
        timeTravel += second * 1000;
    }


    public static void resetTimeTravel() {
        timeTravel = 0;
    }

    public static long getTimeTravel() {
        return timeTravel;
    }


    public static List<String> remove(List<String> list, String target) {
        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>(list);
        for (String item : cowList) {
            if (item.equals(target)) {
                cowList.remove(item);
            }
        }
        return cowList;
    }


    public static String moneyToPlainString(double money) {
        BigDecimal bd = new BigDecimal(money);

        return bd.toPlainString();
    }


    public static long toTokenValue(long value) {
        return value * 100000000L;
    }

    public static long toTokenValue(double value) {
        return (long) (value * 100000000L);
    }

    public static double deTokenValue(double value) {
        return value / 100000000L;
    }


    public static boolean isWalletAddressValid(String address) {
        if (StringUtils.isBlank(address)) {
            return false;
        }

        int length = address.length();

        return length == 34;
    }


    public static BigDecimal countVotes(List<BigDecimal> list) {

        BigDecimal price = BigDecimal.ZERO;


        if (list.size() == 0) {
            return price;
        }


        Comparator<BigDecimal> reverseComparator = Collections.reverseOrder();
        Collections.sort(list, reverseComparator);


        int skipTop = (int) (list.size() * 0.245);
        int skipEnd = list.size() - (int) (list.size() * 0.245);
        for (int i = 0; i < list.size(); i++) {
            BigDecimal num = list.get(i);

            if (i >= skipTop && i < skipEnd) {
                price = price.add(num);
            }
        }


        if (price.compareTo(BigDecimal.ZERO) == 1) {
            long count = list.size() - skipTop * 2;
            price = price.divide(BigDecimal.valueOf(count));
        }


        return price;
    }

}
