package org.nachain.libs.validate;

import org.nachain.libs.util.CommUtil;
import org.nachain.libs.validate.result.VldResult;
import org.nachain.libs.validate.type.Length;

import java.util.Date;

public class VldUtil {


    public static boolean isEmpty(VldResult vldResult, String value, String valueName, String valueInfo) {
        boolean rtv = false;
        if (value.equals("")) {
            rtv = true;
            vldResult.addError(valueName, value, buildEmptyInfo(valueInfo));
        }
        return rtv;
    }

    public static boolean isEmpty(VldResult vldResult, int value, String valueName, String valueInfo) {
        return isEmpty(vldResult, (double) value, valueName, valueInfo);
    }

    public static boolean isEmpty(VldResult vldResult, long value, String valueName, String valueInfo) {
        return isEmpty(vldResult, (double) value, valueName, valueInfo);
    }

    public static boolean isEmpty(VldResult vldResult, double value, String valueName, String valueInfo) {
        boolean rtv = false;
        if (value == 0) {
            rtv = true;
            vldResult.addError(valueName, value, buildEmptyInfo(valueInfo));
        }
        return rtv;
    }

    public static boolean isEmpty(VldResult vldResult, Date value, String valueName, String valueInfo) {
        boolean rtv = false;
        if (value == null) {
            rtv = true;
            vldResult.addError(valueName, value, buildEmptyInfo(valueInfo));
        }
        return rtv;
    }


    public static boolean isError(VldResult vldResult, boolean isError, String valueName, String valueInfo) {
        boolean rtv = false;
        if (isError) {
            rtv = true;
            vldResult.addError(valueName, "", buildErrorInfo(valueInfo));
        }
        return rtv;
    }


    public static String buildEmptyInfo(String valueInfo) {
        return valueInfo + "Cannot be empty, please re-enter!";
    }

    public static String buildErrorInfo(String valueInfo) {
        return valueInfo + "Error, please re-enter!";
    }


    public static boolean vldLength(String content, Length length) {

        int maxLength = length.max();

        int minLength = length.min();
        if (content.length() < minLength) {
            return false;
        }
        if (content.length() > maxLength) {
            return false;
        }
        return true;
    }


    public static boolean vldNotBlank(String content) {
        if (CommUtil.isEmpty(content)) {
            return false;
        }
        return true;
    }

}