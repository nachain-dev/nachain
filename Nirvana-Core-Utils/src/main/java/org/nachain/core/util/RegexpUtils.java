package org.nachain.core.util;

public class RegexpUtils {


    public static final String REGEXP_NORMAL = "^[a-zA-Z0-9_\\s,\\.!]*$";


    public static final String REGEXP_EN_NUM = "^[a-zA-Z0-9]*$";


    public static boolean isValidate(String regexp, String data) {
        return data.matches(regexp);
    }

}
