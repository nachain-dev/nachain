package org.nachain.libs.util;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
public class CommUtil {


    public static String null2String(Object s) {
        return null2String(s, "");
    }

    public static String null2String(Object s, String defaultValue) {
        return s == null ? defaultValue : null2String(s.toString(), defaultValue);
    }

    public static String null2String(String s) {
        return null2String(s, "");
    }

    public static String null2String(String s, String defaultValue) {
        String v = defaultValue;
        if (s != null && !s.equals("")) {
            try {
                v = s;
            } catch (Exception e) {
                log.error("Error nulling data", e);
            }
        }
        return v;
    }


    public static boolean null2Boolean(Object s) {
        return null2Boolean(s, false);
    }

    public static boolean null2Boolean(Object s, boolean defaultValue) {
        return s == null ? defaultValue : null2Boolean(s.toString(), defaultValue);
    }

    public static boolean null2Boolean(String s) {
        return null2Boolean(s, false);
    }

    public static boolean null2Boolean(String s, boolean defaultValue) {
        boolean v = defaultValue;
        if (s != null && !s.equals("")) {
            try {
                v = Boolean.valueOf(s);
            } catch (Exception e) {
                log.error("Error nulling data", e);
            }
        }
        return v;
    }


    public static int null2Int(Object s) {
        return null2Int(s, 0);
    }

    public static int null2Int(Object s, int defaultValue) {
        return s == null ? defaultValue : null2Int(s.toString(), defaultValue);
    }

    public static int null2Int(String s) {
        return null2Int(s, 0);
    }

    public static int null2Int(String s, int defaultValue) {
        int v = defaultValue;
        if (s != null && !s.equals("")) {
            try {
                v = Integer.parseInt(s);
            } catch (Exception e) {
                log.error("Error nulling data", e);
            }
        }
        return v;
    }


    public static long null2Long(Object s) {
        return null2Long(s, 0);
    }

    public static long null2Long(Object s, long defaultValue) {
        return s == null ? defaultValue : null2Long(s.toString(), defaultValue);
    }

    public static long null2Long(String s) {
        return null2Long(s, 0);
    }

    public static long null2Long(String s, long defaultValue) {
        long v = defaultValue;
        if (s != null && !s.equals("")) {
            try {
                v = Long.parseLong(s);
            } catch (Exception e) {
                log.error("Error nulling data", e);
            }
        }
        return v;
    }


    public static float null2Float(Object s) {
        return null2Float(s, 0);
    }

    public static float null2Float(Object s, float defaultValue) {
        return s == null ? defaultValue : null2Float(s.toString(), defaultValue);
    }

    public static float null2Float(String s) {
        return null2Long(s, 0);
    }

    public static float null2Float(String s, float defaultValue) {
        float v = defaultValue;
        if (s != null && !s.equals("")) {
            try {
                v = Float.parseFloat(s);
            } catch (Exception e) {
                log.error("Error nulling data", e);
            }
        }
        return v;
    }


    public static double null2Double(Object s) {
        return null2Double(s, 0);
    }

    public static double null2Double(Object s, double defaultValue) {
        return s == null ? defaultValue : null2Double(s.toString(), defaultValue);
    }

    public static double null2Double(String s) {
        return null2Double(s, 0);
    }

    public static double null2Double(String s, double defaultValue) {
        double v = defaultValue;
        if (s != null && !s.equals("")) {
            try {
                v = Double.parseDouble(s);
            } catch (Exception e) {
                log.error("Error nulling data", e);
            }
        }
        return v;
    }


    public static Date null2Date(Object s) {
        Date v = null;

        if (s != null && !s.equals("")) {

            if (s instanceof Date) {
                v = (Date) s;
            } else if (!s.equals("") && (s instanceof String)) {
                try {
                    v = DateUtil.parseByDate(s.toString());
                } catch (Exception e) {
                    log.error("Error nulling date", e);
                }
            }
        }

        return v;
    }


    public static Date null2DateTime(Object s) {
        Date v = null;

        if (s != null && !s.equals("")) {

            if (s instanceof Date) {
                v = (Date) s;
            } else if (!s.equals("") && (s instanceof String)) {
                try {
                    v = DateUtil.parseByDateTime(s.toString());
                } catch (Exception e) {
                    log.error("Error nulling date", e);
                }
            }
        }

        return v;
    }


    public static Date null2DateTimeSSS(Object s) {
        Date v = null;

        if (s != null && !s.equals("")) {

            if (s instanceof Date) {
                v = (Date) s;
            } else if (!s.equals("") && (s instanceof String)) {
                try {
                    v = DateUtil.parseByDateTimeSSS(s.toString());
                } catch (Exception e) {
                    log.error("Error nulling date", e);
                }
            }
        }

        return v;
    }


    public static String double2String(double d) {
        String returnValue = "";

        returnValue = String.valueOf(d);
        int in = returnValue.indexOf(".");
        if (returnValue.substring(in).equals(".0")) {
            returnValue = returnValue.substring(0, in);
        }

        return returnValue;
    }


    public static String nullCheck(Object obj, String content, String nullContent) {
        String returnValue = null;

        if (obj != null) {
            returnValue = content;
        } else {
            returnValue = nullContent;
        }

        return returnValue;
    }


    public static String booleanCheck(boolean b, String trueContent, String falseContent) {
        String returnValue = null;

        if (b) {
            returnValue = trueContent;
        } else {
            returnValue = falseContent;
        }

        return returnValue;
    }


    public static Object booleanCheck(boolean b, Object trueContent, Object falseContent) {
        Object returnValue = null;

        if (b) {
            returnValue = trueContent;
        } else {
            returnValue = falseContent;
        }

        return returnValue;
    }


    public static int double2Int(double d) {
        int returnValue = 0;


        returnValue = (int) d;


        return returnValue;
    }


    public static String int2Hex(int n) {
        return Integer.toHexString(n);
    }


    public static int hex2Int(String hex) {
        return Integer.parseInt(hex, 16);
    }


    public static String int2Octal(int n) {
        return Integer.toOctalString(n);
    }


    public static String octal2Int(String octal) {
        return Integer.valueOf(octal, 8).toString();
    }


    public static String int2Binary(int n) {
        return Integer.toBinaryString(n);
    }


    public static int binary2Int(String bin) {
        return Integer.valueOf(bin, 2);
    }


    public static String array2String(Object obj, String split) {
        StringBuffer returnValue = new StringBuffer();

        if (obj != null) {
            if (obj instanceof String[]) {
                String[] values = (String[]) obj;
                int length = values.length;
                for (int i = 0; i < length; i++) {
                    returnValue.append(values[i].concat(split));
                }
            } else if (obj instanceof String) {
                returnValue.append(obj.toString());
            }
        }

        return returnValue.toString();
    }


    public static String array2String(String[] values, String split) {

        String rtv = "";

        StringBuffer sb = new StringBuffer();

        if (values != null) {
            int length = values.length;
            for (int i = 0; i < length; i++) {
                sb.append(values[i].concat(split));
            }

            rtv = sb.toString();
            if (rtv.length() > 0)
                rtv = rtv.substring(0, rtv.length() - 1);
        }

        return rtv;
    }


    public static String array2String(int[] values, String split) {
        StringBuffer returnValue = new StringBuffer();

        if (values != null) {
            int length = values.length;
            for (int i = 0; i < length; i++) {
                returnValue.append(String.valueOf(values[i]).concat(split));
            }
        }

        return returnValue.toString();
    }


    public static int[] arrayString2Int(String[] values) {
        int[] returnValue = null;

        if (values != null) {
            int length = values.length;
            returnValue = new int[length];
            for (int i = 0; i < length; i++) {
                returnValue[i] = Integer.parseInt(values[i]);
            }
        }

        return returnValue;
    }


    public static long[] arrayString2Long(String[] values) {
        long[] returnValue = null;

        if (values != null) {
            int length = values.length;
            returnValue = new long[length];
            for (int i = 0; i < length; i++) {
                returnValue[i] = Long.parseLong(values[i]);
            }
        }

        return returnValue;
    }


    public static double[] arrayString2Double(String[] values) {
        double[] returnValue = null;

        if (values != null) {
            int length = values.length;
            returnValue = new double[length];
            for (int i = 0; i < length; i++) {
                returnValue[i] = Double.parseDouble(values[i]);
            }
        }

        return returnValue;
    }


    public static Integer[] arrayString2Integer(String[] values) {
        Integer[] returnValue = null;

        if (values != null) {
            int length = values.length;
            returnValue = new Integer[length];
            for (int i = 0; i < length; i++) {
                returnValue[i] = Integer.parseInt(values[i]);
            }
        }

        return returnValue;
    }


    public static boolean[] arrayString2Boolean(String[] values) {
        boolean[] returnValue = null;

        if (values != null) {
            int length = values.length;
            returnValue = new boolean[length];
            for (int i = 0; i < length; i++) {
                returnValue[i] = Boolean.parseBoolean(values[i]);
            }
        }

        return returnValue;
    }


    public static String list2String(List<?> list, String split) {
        StringBuffer returnValue = new StringBuffer();

        if (list != null) {
            ListIterator<?> listIter = list.listIterator();
            while (listIter.hasNext()) {
                String v = null2String(listIter.next());
                returnValue.append("\"" + v + "\"");
                if (listIter.hasNext()) {
                    returnValue.append(split);
                }
            }
        }

        return returnValue.toString();
    }


    public static String string2ArrayString(String str, String split) {
        StringBuffer returnValue = new StringBuffer();

        if (str != null) {
            String[] values = str.split(split);
            int length = values.length;
            for (int i = 0; i < length; i++) {
                returnValue.append("\"".concat(values[i]).concat("\""));
                if (i + 1 < length) {
                    returnValue.append(split);
                }
            }
        }

        return returnValue.toString();
    }


    public static double round(double number, int param) {
        double returnValue;

        String format = "#.";
        for (int i = 0; i < param; i++) {
            format = format.concat("#");
        }

        if (param == 0) {
            format = format.substring(0, format.toString().length() - 1);
        }
        DecimalFormat df = new DecimalFormat(format);
        returnValue = Double.parseDouble(df.format(number));

        return returnValue;
    }


    public static String digit(double number, int minIntegerDigits, int maxFractionDigits) {
        String returnValue;

        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        numberFormat.setMinimumIntegerDigits(minIntegerDigits);

        numberFormat.setMaximumFractionDigits(maxFractionDigits);

        returnValue = numberFormat.format(number);

        return returnValue;
    }


    public static String toUnicode(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            String temp = "";
            int strInt = str.charAt(i);
            if (strInt > 127) {
                temp += "\\u" + Integer.toHexString(strInt);
            } else {
                temp = String.valueOf(str.charAt(i));
            }
            result += temp;
        }
        return result;
    }


    public static String toUTF8ByUnicode(String unicodeStr) {
        try {
            String gbkStr = new String(unicodeStr.getBytes("UTF-8"), "UTF-8");
            return gbkStr;
        } catch (UnsupportedEncodingException e) {
            return unicodeStr;
        }
    }


    public static String replace(String strSource, String strFrom, String strTo) {

        if (strFrom == null || strFrom.equals(""))
            return strSource;
        String strDest = "";

        int intFromLen = strFrom.length();
        int intPos;

        while ((intPos = strSource.indexOf(strFrom)) != -1) {

            strDest = strDest + strSource.substring(0, intPos);

            strDest = strDest + strTo;

            strSource = strSource.substring(intPos + intFromLen);
        }

        strDest = strDest + strSource;

        return strDest;
    }


    public static String clearNewLine(String s) {
        if (s == null) {
            s = "";
            return s;
        }
        s = replace(s, "\r\n", "");
        s = replace(s, "\r", "");
        s = replace(s, "\n", "");

        return s;
    }


    public static String SBC2DBC(String content) {
        String returnValue = content;


        String SBC = "１２３４５６７８９０ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ";
        String DBC = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String[] SBCs = new String[SBC.length()];
        String[] DBCs = new String[DBC.length()];
        for (int i = 0; i < SBC.length(); i++) {
            SBCs[i] = SBC.substring(i, i + 1);
            DBCs[i] = DBC.substring(i, i + 1);
        }


        for (int i = 0; i < SBCs.length; i++) {
            String sbc = SBCs[i];
            String dbc = DBCs[i];
            returnValue = returnValue.replaceAll(sbc, dbc);
        }

        return returnValue;
    }


    public static String interceptString(String source, int total) {
        String returnValue = source;


        if (returnValue == null) {
            returnValue = "";
            return returnValue;
        }


        if (returnValue.length() <= total)
            return returnValue;

        returnValue = returnValue.substring(0, total);

        return returnValue;
    }


    public static String contentSubstring(String content, int startSkip, int endSkip, String startData, String endData) {
        String returnValue = "";

        int contentLen = content.length();
        int beginIndex = -1;
        int endIndex = -1;
        boolean startEmpty = (startData == null || startData.length() == 0);
        boolean endEmpty = (endData == null || endData.length() == 0);


        if (startEmpty)
            beginIndex = 0;


        if (!startEmpty) {
            beginIndex = content.indexOf(startData);
            for (int i = 0; i < startSkip; i++) {
                beginIndex = content.indexOf(startData, beginIndex + startData.length());

            }
        }
        if (!endEmpty) {
            endIndex = content.indexOf(endData);
            for (int i = 0; i < endSkip; i++) {

                endIndex = content.indexOf(endData, endIndex + endData.length());

            }


            if (endIndex <= beginIndex) {

                long loopOut = contentLen;
                while (true) {
                    endIndex = content.indexOf(endData, endIndex + endData.length());
                    if (endIndex > beginIndex) {
                        break;
                    }

                    loopOut--;
                    if (loopOut <= 0 || endIndex >= contentLen) {
                        break;
                    }
                }


                if (endData.equals(" ")) {
                    if (endIndex <= beginIndex) {
                        endIndex = contentLen;
                    }
                }


            }
        }


        beginIndex += startData.length();


        if (!startEmpty && endEmpty) {
            returnValue = content.substring(beginIndex);
        } else if (!startEmpty && !endEmpty) {
            returnValue = content.substring(beginIndex, endIndex);
        } else if (startEmpty && !endEmpty) {
            returnValue = content.substring(beginIndex, endIndex);
        }


        return returnValue;
    }


    public static String contentSubstring(String content, String startData, String endData) {
        return contentSubstring(content, 0, 0, startData, endData);
    }


    public static int countStringTotal(String source, String target) {
        int returnValue = 0;


        int sourceLen = source.length();

        int targetLen = target.length();

        int newStrLen = replace(source, target, "").length();
        returnValue = (sourceLen - newStrLen) / targetLen;

        return returnValue;
    }


    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }


    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }


    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }


    public static boolean isEmpty(List<Object> list) {
        return list == null || list.isEmpty();
    }


    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }


    public static String removeSameString(String str) {
        Set<String> mLinkedSet = new LinkedHashSet<String>();
        String[] strArray = str.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < strArray.length; i++) {
            if (!mLinkedSet.contains(strArray[i])) {
                mLinkedSet.add(strArray[i]);
                sb.append(strArray[i] + " ");
            }
        }

        return sb.toString().substring(0, sb.toString().length() - 1);
    }


    public static List<String> removeDuplicate(List<String> list) {

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }


        return list;
    }


    public static String getLabel(int value, int[] values, String[] labels) {
        String returnValue = "";

        for (int i = 0; i < values.length; i++) {
            if (value == values[i]) {
                returnValue = labels[i];
                continue;
            }
        }

        return returnValue;
    }


    public static String getLabel(int value, List values, List labels) {
        String returnValue = "";

        for (int i = 0; i < values.size(); i++) {
            if (value == CommUtil.null2Int(values.get(i))) {
                returnValue = CommUtil.null2String(labels.get(i));
                continue;
            }
        }

        return returnValue;
    }


    public static String getLabel(double value, List values, List labels) {
        String returnValue = "";

        for (int i = 0; i < values.size(); i++) {
            if (value == CommUtil.null2Double(values.get(i))) {
                returnValue = CommUtil.null2String(labels.get(i));
                continue;
            }
        }

        return returnValue;
    }


    public static String reduplicate(String content, int total) {
        String returnValue = "";

        for (int i = 1; i <= total; i++) {
            returnValue += content;
        }

        return returnValue;
    }


    public static String stringCombination(String data, String newData) {
        String returnValue = "";

        returnValue = data;

        if (!data.contains(newData)) {
            returnValue = data + newData;
        }

        return returnValue;
    }


    public static HashMap<String, String> analyzeParameter(String data) {
        return analyzeParameter(data, "&");
    }


    public static HashMap<String, String> analyzeParameter(String data, String splitWord) {
        HashMap<String, String> parameterMap = new HashMap<String, String>();

        if (data == null || data.equals(""))
            return parameterMap;


        String[] parameterParts = data.split(splitWord);
        for (int i = 0; i < parameterParts.length; i++) {
            String[] parameter = parameterParts[i].split("=");
            String key = null;
            String name = null;
            if (parameter.length >= 1) {
                key = parameter[0];
            }
            if (parameter.length >= 2) {
                name = parameter[1];
            }
            if (key != null && name != null) {
                parameterMap.put(key, name);
            }
        }

        return parameterMap;
    }


}