package org.nachain.libs.log;

import org.nachain.libs.log.global.LogConfig;


public class ToConsole {


    public static void printConsole(String logName, String loggerLevel, String info) {

        String content = ContentService.getContent(logName, loggerLevel, info);
        if (loggerLevel.equals("ERROR") || loggerLevel.equals("FATAL")) {

            if (LogConfig.isAloneErrPrintln) {
                System.err.println(content);
            } else {
                System.out.println(content);
            }
        } else {
            System.out.println(content);
        }

    }
}