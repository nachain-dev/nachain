package org.nachain.libs.action;


public class ActionUtil {


    public static String getActionClass(String comeUrl, String actionPackageName) {


        String actionClass = "";
        actionClass = comeUrl.replaceAll("/", "\\.");
        actionClass = actionPackageName.concat(actionClass);

        int lastDotIn = actionClass.lastIndexOf(".") + 1;
        actionClass = actionClass.substring(0, lastDotIn).concat(actionClass.substring(lastDotIn, lastDotIn + 1).toUpperCase()).concat(actionClass.substring(lastDotIn + 1));

        return actionClass;
    }


}