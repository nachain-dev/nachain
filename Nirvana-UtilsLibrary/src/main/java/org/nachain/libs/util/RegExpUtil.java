package org.nachain.libs.util;

import java.util.regex.Pattern;


public class RegExpUtil {

    public static final Pattern JapaneseWordPattern = Pattern.compile("[\u0800-\u4e00]+$");


    public static final String IpPattern = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";


    public static final Pattern EmailPattern = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");


}