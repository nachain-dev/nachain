package org.nachain.libs.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StringUtil {

    public static String CHATSET_UTF_8 = "utf-8";


    public static String selectIfNull(Object check, Object trueVal, Object falseVal) {
        return check == null ? String.valueOf(trueVal) : String.valueOf(falseVal);
    }


    public static String selectIf(boolean flag, Object trueVal, Object falseVal) {
        return flag ? String.valueOf(trueVal) : String.valueOf(falseVal);
    }


    public static long[] toLongs(String[] data) {
        if (data == null) return null;

        long[] list = new long[data.length];
        for (int i = 0; i < list.length; i++) {
            String s = data[i];
            if (StringUtils.isBlank(s)) {
                continue;
            }
            list[i] = Long.parseLong(s);
        }
        return list;
    }


    public static int[] toInts(String[] data) {
        if (data == null) return null;

        int[] list = new int[data.length];
        for (int i = 0; i < list.length; i++) {
            String s = data[i];
            if (StringUtils.isBlank(s)) {
                continue;
            }
            list[i] = Integer.parseInt(s);
        }
        return list;
    }


    public static double[] toDoubles(String[] data) {
        if (data == null) return null;

        double[] list = new double[data.length];
        for (int i = 0; i < list.length; i++) {
            String s = data[i];
            if (StringUtils.isBlank(s)) {
                continue;
            }
            list[i] = Double.parseDouble(s);
        }
        return list;
    }


    public static String onlyText(String html) {
        if (StringUtils.isBlank(html)) {
            return "";
        }
        String s = HtmlUtil.clearXHTML(html);
        s = s.replaceAll("\\s", "");
        return s;
    }


    public static String onlyLimitText(String html, int size) {
        return StringUtils.abbreviate(onlyText(html), size);
    }


    public static boolean hasBlankChar(String s) {
        if (StringUtils.isBlank(s)) {
            return true;
        }

        int i = s.indexOf(" ");
        int j = s.indexOf("　");
        if (i == -1 && j == -1) return false;
        return true;
    }


    public static String removeBlankChar(String s) {

        return s.replace(" ", "").replace("　", "");
    }


    public static String toQueryUrl(Map<String, Object> paramMap) throws UnsupportedEncodingException {
        if (paramMap == null || paramMap.size() == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String key : paramMap.keySet()) {
            Object value = paramMap.get(key);
            stringBuilder.append("&");
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(String.valueOf(value), "utf-8"));
        }

        return stringBuilder.toString();
    }


    public static final String PLACEHOLDER_PREFIX = "${";

    public static final String PLACEHOLDER_SUFFIX = "}";


    public static String formatString(String text, Map<String, String> parameter) {
        if (parameter == null || parameter.isEmpty()) {
            return text;
        }
        StringBuffer buf = new StringBuffer(text);
        int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
        while (startIndex != -1) {
            int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
            if (endIndex != -1) {
                String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
                try {
                    String propVal = parameter.get(placeholder);
                    if (propVal != null) {
                        buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);
                        nextIndex = startIndex + propVal.length();
                    } else {
                        System.out.println("Could not resolve placeholder '" + placeholder + "' in [" + text + "] ");
                    }
                } catch (Exception ex) {
                    System.out.println("Could not resolve placeholder '" + placeholder + "' in [" + text + "]: " + ex);
                }
                startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
            } else {
                startIndex = -1;
            }
        }
        return buf.toString();
    }


    public static List<String> splitByComma(String data) {

        if (StringUtils.isBlank(data)) {
            ArrayList<String> stringList = new ArrayList<String>();
            stringList.add(data);
            return stringList;
        }

        return java.util.Arrays.asList(data.split(",|,"));
    }


    public static String noTag(String val) {

        val = val.replaceAll("<", "&lt;");
        val = val.replaceAll(">", "&gt;");
        return val;
    }


    public static String stringSecurity(String input) {


        input = StringUtil.noTag(input);
        return StringUtils.defaultString(input, "").trim();
    }


    public static String getStackTrace(Throwable throwable) {

        StringWriter sw = new StringWriter();

        PrintWriter pw = new PrintWriter(sw);

        throwable.printStackTrace(pw);

        return sw.toString();
    }


    public static String toLowerCaseAndTrim(String input) {

        if (StringUtils.isBlank(input)) {
            return "";
        }

        input = StringUtils.trim(StringUtils.lowerCase(input));

        return input;
    }


    public static String getText(int value, int[] values, String[] texts) {
        String returnValue = "";

        int len = values.length;
        for (int i = 0; i < len; i++) {
            if (value == values[i]) {
                returnValue = texts[i];
                break;
            }
        }

        return returnValue;
    }


    public static String substring(String data, String skipString, int skipNum) {
        String content = data;
        int pos = 0;
        int count = 0;
        while (pos < content.length()) {
            pos = content.indexOf(skipString, pos);
            if (pos == -1) {
                break;
            } else {
                count++;
                pos++;
                if (count == skipNum + 1) {
                    break;
                }
            }
        }
        if (pos == -1) {
            content = data;
        } else {
            content = content.substring(0, pos - skipString.length());
        }

        return content;
    }


    public static String clearWordFormat(String content) {
        Pattern p = Pattern.compile("</?SPAN[^>]*>");
        Matcher match = p.matcher(content);
        content = match.replaceAll("");

        p = Pattern.compile("(<P)([^>]*>.*?)(<\\/P>)");
        match = p.matcher(content);
        content = match.replaceAll("<div$2</div>");

        p = Pattern.compile("<(\\w[^>]*) class=([^ |>]*)([^>]*)");
        match = p.matcher(content);
        content = match.replaceAll("<$1$3");

        p = Pattern.compile("<(\\w[^>]*) style=\"([^\"]*)\"([^>]*)");
        match = p.matcher(content);
        content = match.replaceAll("<$1$3");

        p = Pattern.compile("<(\\w[^>]*) lang=([^ |>]*)([^>]*)");
        match = p.matcher(content);
        content = match.replaceAll("<$1$3");

        p = Pattern.compile("<\\\\?\\?xml[^>]*>");
        match = p.matcher(content);
        content = match.replaceAll("");

        p = Pattern.compile("<\\/?\\w+:[^>]*>");
        match = p.matcher(content);
        content = match.replaceAll("");

        p = Pattern.compile("/ /");
        match = p.matcher(content);
        content = match.replaceAll(" ");

        return content;
    }


    public static String clearNot_09azAZ(String value) {
        String rtv = value;


        rtv = rtv.replaceAll("[^0-9a-zA-Z_\\-]", "");

        return rtv;
    }


    public static String first2UpperCase(String content) {
        String returnValue = content;
        returnValue = returnValue.substring(0, 1).toUpperCase() + returnValue.substring(1);
        return returnValue;
    }


    public static String first2LowerCase(String content) {
        String returnValue = null;

        returnValue = content.substring(0, 1).toLowerCase() + content.substring(1);

        return returnValue;
    }


    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }


    public static String fixedLength(String data, int fixedLength, String addString) {
        String rtv = data;


        int dataLength = data.length();


        if (dataLength < fixedLength) {

            int addTotal = fixedLength - dataLength;
            rtv = rtv + CommUtil.reduplicate(" ", addTotal);
        }

        return rtv;
    }


    public static String getSecurityRealName(String data) {
        String realName = data;

        if (StringUtils.isBlank(data)) {
            return "-";
        }
        if (realName.getBytes().length == realName.length()) {
            realName = realName.substring(0, 1) + "*****" + realName.substring(realName.length() - 1, realName.length());
            return realName;
        }
        realName = "*" + realName.substring(1, realName.length());

        return realName;
    }


    public static String getSecurityIDNumber(String data) {
        String IDNumber = data;

        if (StringUtils.isBlank(data)) {
            return "-";
        }
        if (IDNumber.length() > 8) {
            IDNumber = IDNumber.substring(0, 3) + "******" + IDNumber.substring(IDNumber.length() - 3, IDNumber.length());
        } else {
            IDNumber = IDNumber.substring(0, 1) + "*****" + IDNumber.substring(IDNumber.length() - 1, IDNumber.length());
        }

        return IDNumber;
    }
}