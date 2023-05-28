package org.nachain.libs.log.global;

import org.nachain.libs.config.ConfigFile;
import org.nachain.libs.encrypt.CryptoFile;
import org.nachain.libs.log.Log;
import org.nachain.libs.log.Logger;
import org.nachain.libs.log.ToCustom;
import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.FilePathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LogConfig {

    public static String ProjectName;

    public static final String SecretKey = "a55ew1bs";

    public static final String DefaultGroup = "buglog";


    public static final Map<String, Object> WriteLockMap = new HashMap<String, Object>();


    public static ConcurrentHashMap<String, Log> logs;


    public static final String LOG_FOLDER_NAME = "logs";


    public static String toCustomClassName;


    public static boolean isToCustomInitOK;


    public static boolean isAloneErrPrintln;

    static {


        LogConfig.logs = new ConcurrentHashMap<String, Log>();


        loadConfig();


        try {
            ToCustom.init();
        } catch (ExceptionInInitializerError eie) {
            System.err.print("ToCustom Error handling class initialization:");
            eie.printStackTrace();
        }
    }


    public static Object getWriteLock(String key) {

        if (!WriteLockMap.containsKey(key)) {
            synchronized (LogConfig.WriteLockMap) {
                if (!WriteLockMap.containsKey(key)) {
                    byte[] lockObj = new byte[0];
                    WriteLockMap.put(key, lockObj);
                }
            }
        }

        return WriteLockMap.get(key);
    }


    private static void loadConfig() {


        String userHomePath = System.getProperty("user.home") + File.separator;


        String filePath = userHomePath;
        try {
            filePath = new FilePathUtil(Logger.class).getProjectRootPath();
        } catch (Exception e) {

            Log defaultLog = new Log();
            defaultLog.setLogName("Log");
            defaultLog.setLoggerLevel("debug");
            defaultLog.setConsole(true);
            defaultLog.setFile(true);
            defaultLog.setCustom(false);
            defaultLog.setLevelNumber(Logger.getLevelNumber(defaultLog.getLoggerLevel()));
            defaultLog.setFatalPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + defaultLog.getLogName() + "/fatal.log");
            defaultLog.setErrorPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + defaultLog.getLogName() + "/error.log");
            defaultLog.setWarnPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + defaultLog.getLogName() + "/warn.log");
            defaultLog.setInfoPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + defaultLog.getLogName() + "/info.log");
            defaultLog.setDebugPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + defaultLog.getLogName() + "/debug.log");
            LogConfig.logs.put(defaultLog.getLogName(), defaultLog);

            Logger.getLog("Log").fatal(filePath + " An error occurred when obtaining FilePathUtil. GetProjectRootPath ().", e, false);
        }


        Log defaultLog = new Log();
        defaultLog.setLogName("Log");
        defaultLog.setLoggerLevel("debug");
        defaultLog.setConsole(true);
        defaultLog.setFile(true);
        defaultLog.setCustom(false);
        defaultLog.setLevelNumber(Logger.getLevelNumber(defaultLog.getLoggerLevel()));
        defaultLog.setFatalPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + defaultLog.getLogName() + "/fatal.log");
        defaultLog.setErrorPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + defaultLog.getLogName() + "/error.log");
        defaultLog.setWarnPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + defaultLog.getLogName() + "/warn.log");
        defaultLog.setInfoPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + defaultLog.getLogName() + "/info.log");
        defaultLog.setDebugPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + defaultLog.getLogName() + "/debug.log");
        LogConfig.logs.put(defaultLog.getLogName(), defaultLog);


        Log libsLog = new Log();
        libsLog.setLogName("Libs");
        libsLog.setLoggerLevel("debug");
        libsLog.setConsole(true);
        libsLog.setFile(true);
        libsLog.setCustom(false);
        libsLog.setLevelNumber(Logger.getLevelNumber(libsLog.getLoggerLevel()));
        libsLog.setFatalPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + libsLog.getLogName() + "/fatal.log");
        libsLog.setErrorPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + libsLog.getLogName() + "/error.log");
        libsLog.setWarnPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + libsLog.getLogName() + "/warn.log");
        libsLog.setInfoPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + libsLog.getLogName() + "/info.log");
        libsLog.setDebugPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + libsLog.getLogName() + "/debug.log");
        LogConfig.logs.put(libsLog.getLogName(), libsLog);


        ConfigFile prop = new ConfigFile();

        String configFilePath = null;
        try {
            configFilePath = filePath.concat("log.config");


            byte[] decodeData = CryptoFile.getConfigFileData(configFilePath, LogConfig.SecretKey.getBytes());


            prop.setData(decodeData);
        } catch (FileNotFoundException e) {
            Logger.getLog("Log").fatal(configFilePath + " File not found error, please check whether this file exists or is available.", e, false);
            return;
        } catch (IOException e) {
            Logger.getLog("Log").fatal("Read" + configFilePath + "file error", e, false);
            return;
        } catch (Exception e) {
            Logger.getLog("Log").fatal("Read" + configFilePath + "file error", e, false);
            return;
        }

        String rootLogger = prop.getValue("log.rootLogger");
        LogConfig.toCustomClassName = prop.getValue("log.toCustomClassName");

        LogConfig.isAloneErrPrintln = CommUtil.null2Boolean(prop.getValue("log.Console.isAloneErrPrintln"));

        LogConfig.ProjectName = prop.getValue("log.projectName");

        String[] loggers = rootLogger.split(",");
        for (int i = 0; i < loggers.length; i++) {
            Log log = new Log();
            log.setLogName(loggers[i]);
            String LoggerLevel = prop.getValue("log." + log.getLogName() + ".LoggerLevel");
            if (LoggerLevel == null || LoggerLevel.equals("")) {
                libsLog.warn("warning: rootLogger " + log.getLogName() + " Incomplete configuration! Please check it " + filePath.concat("log.config") + " config file!", false);
            } else {
                log.setLoggerLevel(LoggerLevel);
                log.setConsole(Boolean.parseBoolean(prop.getValue("log." + log.getLogName() + ".Console")));
                log.setFile(Boolean.parseBoolean(prop.getValue("log." + log.getLogName() + ".File")));
                log.setCustom(Boolean.parseBoolean(prop.getValue("log." + log.getLogName() + ".Custom")));
                log.setLevelNumber(Logger.getLevelNumber(log.getLoggerLevel()));
                log.setFatalPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + log.getLogName() + "/fatal.log");
                log.setErrorPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + log.getLogName() + "/error.log");
                log.setWarnPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + log.getLogName() + "/warn.log");
                log.setInfoPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + log.getLogName() + "/info.log");
                log.setDebugPath(filePath + LogConfig.LOG_FOLDER_NAME + "/" + log.getLogName() + "/debug.log");
                LogConfig.logs.put(log.getLogName(), log);
                log.debug("Reset Log instance!LogName:" + log.getLogName() + ";LoggerLevel:" + log.getLoggerLevel() + ";DebugPath:" + log.getDebugPath(), false);
                log = null;
            }
        }
    }

}