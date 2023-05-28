package org.nachain.libs.template;

import org.nachain.libs.global.LibsConfig;
import org.nachain.libs.log.Log;
import org.nachain.libs.log.Logger;

public class TemplateUtil {

    private static Log log = Logger.getLog(LibsConfig.LogName);


    public static String merge(String tleContent, String[][] data) {
        String returnValue = "";


        returnValue = tleContent;


        for (int i = 0; i < data.length; i++) {
            returnValue = returnValue.replaceAll("\\$" + data[i][0], data[i][1]);
        }

        return returnValue;
    }
}