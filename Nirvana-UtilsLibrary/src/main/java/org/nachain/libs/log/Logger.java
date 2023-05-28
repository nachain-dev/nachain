package org.nachain.libs.log;

import org.nachain.libs.log.global.LogConfig;


public class Logger {


    public static Log getLog(String logName) {


        if (LogConfig.logs.containsKey(logName)) {
            return (Log) LogConfig.logs.get(logName);
        }

        String info = logName + " the log object does not exist. Check whether the log object exists in the configuration file.  \r\n";
        LogConfig.logs.get("Log").fatal(info, new Exception(info), false);


        return (Log) LogConfig.logs.get("Log");
    }


    public static int getLevelNumber(String loggerLevel) {
        int levelNumber = 0;

        if (loggerLevel.toUpperCase().equals("DEBUG")) {
            levelNumber = LogConst.DEBUG;
        } else if (loggerLevel.toUpperCase().equals("INFO")) {
            levelNumber = LogConst.INFO;
        } else if (loggerLevel.toUpperCase().equals("WARN")) {
            levelNumber = LogConst.WARN;
        } else if (loggerLevel.toUpperCase().equals("ERROR")) {
            levelNumber = LogConst.ERROR;
        } else if (loggerLevel.toUpperCase().equals("FATAL")) {
            levelNumber = LogConst.FATAL;
        }

        return levelNumber;
    }


}