package org.nachain.libs.util;

public class UnitPathUtil {


    public static String buildUnitPath(int number, int length) {
        String returnValue = "";

        String path = String.valueOf(number);


        StringBuffer tempPath = new StringBuffer();

        int zeroLen = length - path.length();
        for (int i = 0; i < zeroLen; i++) {
            tempPath.append("0");
        }
        tempPath.append(path);
        returnValue = tempPath.toString();

        return returnValue;
    }


    public static String getSingleUnitPath(String path, int index) {
        String returnValue = "";

        index--;
        String[] paths = path.split("\\.");
        if (paths.length > index)
            returnValue = paths[index];

        return returnValue;
    }


    public static String getUnitPath(String path, int index) {
        String returnValue = "";

        index--;
        String[] paths = path.split("\\.");
        for (int i = 0; i <= index; i++) {
            returnValue += paths[i];
            if (i + 1 <= index)
                returnValue += ".";
        }

        return returnValue;
    }


    public static String getFirstUnitPath(String path) {
        String returnValue = path;

        int in = path.indexOf(".");
        if (in >= 0)
            returnValue = path.substring(0, in);

        return returnValue;
    }


    public static String getLastUnitPath(String path) {
        String returnValue = path;

        int in = path.lastIndexOf(".");
        if (in >= 0)
            returnValue = path.substring(in + 1);

        return returnValue;
    }


    public static int countPathLength(String path) {
        int returnValue = 0;

        returnValue = CommUtil.countStringTotal(path, ".") + 1;

        return returnValue;
    }


}