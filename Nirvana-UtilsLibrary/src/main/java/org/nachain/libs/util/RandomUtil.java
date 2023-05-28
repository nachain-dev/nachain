package org.nachain.libs.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomUtil {


    private static final char[] Chars = "0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();


    public static String getDateRandID(int RandLen) {
        StringBuffer returnValue = new StringBuffer();


        if (RandLen > 16)
            RandLen = 16;


        String randDate;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        randDate = sdf.format(date);
        returnValue.append(randDate);


        if (RandLen > 0) {

            StringBuffer randMultiple = new StringBuffer();
            randMultiple.append(1);
            for (int i = 0; i < RandLen; i++) {
                randMultiple.append(0);
            }


            double randNum = Math.random() * Double.parseDouble(randMultiple.toString());


            returnValue.append(String.valueOf((long) randNum));
        }

        return returnValue.toString();
    }


    public static String getRandomNum(int RandLen) {
        String returnValue = "";

        Random random = new Random();
        StringBuffer RandomString = new StringBuffer();
        for (int i = 0; i < RandLen; i++) {
            RandomString.append(String.valueOf(random.nextInt(10)));
        }
        returnValue = RandomString.toString();

        return returnValue;
    }


    public static long getRandomLong(long minRandNum, long maxRandNum) {
        long returnValue = 0;

        returnValue = Math.round(Math.random() * (maxRandNum - minRandNum) + minRandNum);

        return returnValue;
    }


    public static String getRandomLetterAndNum(int RandLen) {
        String returnValue = "";

        String[] key = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        Random random = new Random();
        StringBuffer RandomString = new StringBuffer();
        for (int i = 0; i < RandLen; i++) {
            RandomString.append(key[random.nextInt(key.length)]);
        }
        returnValue = RandomString.toString();

        return returnValue;
    }


    public static String getRandomStringNum(int RandLen) {
        return getRandomLetterAndNum(RandLen);
    }


    public static String getRandomString(int RandLen) {
        String returnValue = "";
        Random random = new Random();
        StringBuffer RandomString = new StringBuffer();
        for (int i = 0; i < RandLen; i++) {
            RandomString.append(Chars[random.nextInt(Chars.length)]);
        }
        returnValue = RandomString.toString();

        return returnValue;
    }


    public static String getRandomString(int RandLen, int RandRange) {
        String returnValue = "";


        int range = RandRange;

        if (range <= 0) {
            range = 1;
        } else if (range > Chars.length) {
            range = Chars.length;
        }

        Random random = new Random();
        StringBuffer RandomString = new StringBuffer();
        for (int i = 0; i < RandLen; i++) {
            RandomString.append(Chars[random.nextInt(range)]);
        }
        returnValue = RandomString.toString();

        return returnValue;
    }


    public static String getRandomDateNum(int RandLen) {
        String s = String.valueOf(new Date().getTime());
        Random r = new Random(10);
        s += Math.abs(r.nextInt());
        if (s.length() > RandLen)
            s = s.substring(0, RandLen);
        return s;
    }

}
