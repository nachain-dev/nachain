package org.nachain.libs.log;

import org.nachain.libs.global.LibsConfig;
import org.nachain.libs.log.global.LogConfig;
import org.nachain.libs.util.CommUtil;


public class ToCustom {

    private static Log log = Logger.getLog(LibsConfig.LogName);

    private static ICustom custom;

    private static boolean isExistClass;

    static {
        init();
    }

    public static void init() {
        if (custom == null) {
            synchronized (ToCustom.class) {

                if (custom == null) {

                    if (!CommUtil.isEmpty(LogConfig.toCustomClassName)) {
                        try {
                            custom = (ICustom) Class.forName(LogConfig.toCustomClassName).newInstance();

                            isExistClass = true;

                            LogConfig.isToCustomInitOK = true;
                        } catch (Exception e) {

                            isExistClass = false;
                            log.error("Configuration class could not be found:" + LogConfig.toCustomClassName, e, false);
                        }
                    }
                }
            }
        }
    }


    private static ICustom getCustom() {
        return custom;
    }


    public static boolean isExistClass() {
        return isExistClass;
    }


    public static void add(String loggerName, String level, String event, String explainInfo) {
        if (isExistClass) {
            getCustom().add(loggerName, level, event, explainInfo);
        }
    }


    public static void add(LogInfo logInfo) {
        if (isExistClass) {
            getCustom().add(logInfo);
        }
    }


}