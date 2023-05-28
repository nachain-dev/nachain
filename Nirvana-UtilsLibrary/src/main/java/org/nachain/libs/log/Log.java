package org.nachain.libs.log;

import org.nachain.libs.log.global.LogConfig;


public class Log implements java.io.Serializable, Cloneable {


    private String logName;

    private String loggerLevel;

    private boolean isConsole;

    private boolean isFile;

    private boolean isCustom;


    private int levelNumber;

    private String fatalPath;

    private String errorPath;

    private String warnPath;

    private String infoPath;

    private String debugPath;

    public String getDebugPath() {
        return debugPath;
    }

    public void setDebugPath(String debugPath) {
        this.debugPath = debugPath;
    }

    public String getErrorPath() {
        return errorPath;
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }

    public String getFatalPath() {
        return fatalPath;
    }

    public void setFatalPath(String fatalPath) {
        this.fatalPath = fatalPath;
    }

    public String getInfoPath() {
        return infoPath;
    }

    public void setInfoPath(String infoPath) {
        this.infoPath = infoPath;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }

    public boolean isConsole() {
        return isConsole;
    }

    public void setConsole(boolean isConsole) {
        this.isConsole = isConsole;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(String loggerLevel) {
        this.loggerLevel = loggerLevel;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getWarnPath() {
        return warnPath;
    }

    public void setWarnPath(String warnPath) {
        this.warnPath = warnPath;
    }


    public void fatal(boolean info, Exception e) {
        fatal(String.valueOf(info), e, true);
    }

    public void fatal(int info, Exception e) {
        fatal(String.valueOf(info), e, true);
    }

    public void fatal(long info, Exception e) {
        fatal(String.valueOf(info), e, true);
    }

    public void fatal(float info, Exception e) {
        fatal(String.valueOf(info), e, true);
    }

    public void fatal(double info, Exception e) {
        fatal(String.valueOf(info), e, true);
    }

    public void fatal(String info, Exception e) {
        fatal(info, e, true);
    }

    public void fatal(String info, String content) {
        fatal(info, content, true);
    }

    public void fatal(String content) {
        fatal(content, true);
    }

    public void fatal(String info, Exception e, boolean isToCustom) {
        if (levelNumber >= LogConst.FATAL) {

            String exceptionInfo = ContentService.getException(e);

            if (isConsole) {
                ToConsole.printConsole(logName, LogConst.FATAL_NAME, info.concat("\r\n").concat(exceptionInfo));
            }

            if (isFile) {

                synchronized (LogConfig.getWriteLock(logName + "_" + LogConst.FATAL)) {
                    ToFile.writeFile(logName, LogConst.FATAL_NAME, fatalPath, info.concat("\r\n").concat(exceptionInfo));
                }
            }

            if (isToCustom && isEnabledCustom()) {
                ToCustom.add(logName, LogConst.FATAL_NAME, info, info.concat("\r\n").concat(exceptionInfo));
            }
        }
    }


    private boolean isEnabledCustom() {
        return isCustom && LogConfig.isToCustomInitOK;
    }

    public void fatal(String info, String content, boolean isToCustom) {
        if (levelNumber >= LogConst.FATAL) {
            if (isConsole) {
                ToConsole.printConsole(logName, LogConst.FATAL_NAME, info.concat("\r\n").concat(content));
            }
            if (isFile) {

                synchronized (LogConfig.getWriteLock(logName + "_" + LogConst.FATAL)) {
                    ToFile.writeFile(logName, LogConst.FATAL_NAME, fatalPath, info.concat("\r\n").concat(content));
                }
            }

            if (isToCustom && isEnabledCustom()) {
                ToCustom.add(logName, LogConst.FATAL_NAME, info, info.concat("\r\n").concat(content));
            }
        }
    }

    public void fatal(String content, boolean isToCustom) {
        if (levelNumber >= LogConst.FATAL) {
            if (isConsole) {
                ToConsole.printConsole(logName, LogConst.FATAL_NAME, content);
            }
            if (isFile) {

                synchronized (LogConfig.getWriteLock(logName + "_" + LogConst.FATAL)) {
                    ToFile.writeFile(logName, LogConst.FATAL_NAME, errorPath, content);
                }
            }

            if (isToCustom && isEnabledCustom()) {
                ToCustom.add(logName, LogConst.FATAL_NAME, "", content);
            }
        }
    }


    public void error(boolean info, Exception e) {
        error(String.valueOf(info), e, true);
    }

    public void error(int info, Exception e) {
        error(String.valueOf(info), e, true);
    }

    public void error(long info, Exception e) {
        error(String.valueOf(info), e, true);
    }

    public void error(float info, Exception e) {
        error(String.valueOf(info), e, true);
    }

    public void error(double info, Exception e) {
        error(String.valueOf(info), e, true);
    }

    public void error(String info, Exception e) {
        error(info, e, true);
    }

    public void error(String info, Throwable e) {
        error(info, e, true);
    }

    public void error(String info, Object exceptionObj) {
        error(info, exceptionObj, true);
    }

    public void error(String info, String content) {
        error(info, content, true);
    }

    public void error(String info) {
        error(info, true);
    }

    public void error(String info, Object exceptionObj, boolean isToCustom) {
        if (levelNumber >= LogConst.ERROR) {

            String exceptionInfo = ContentService.getException(exceptionObj);

            if (isConsole) {
                ToConsole.printConsole(logName, LogConst.ERROR_NAME, info.concat("\r\n").concat(exceptionInfo));
            }

            if (isFile) {

                synchronized (LogConfig.getWriteLock(logName + "_" + LogConst.ERROR)) {
                    ToFile.writeFile(logName, LogConst.ERROR_NAME, errorPath, info.concat("\r\n").concat(exceptionInfo));
                }
            }


            if (isToCustom && isEnabledCustom()) {
                ToCustom.add(logName, LogConst.ERROR_NAME, info, info.concat("\r\n").concat(exceptionInfo));
            }
        }
    }

    public void error(String info, String content, boolean isToCustom) {
        if (levelNumber >= LogConst.ERROR) {
            if (isConsole) {
                ToConsole.printConsole(logName, LogConst.ERROR_NAME, info.concat("\r\n").concat(content));
            }
            if (isFile) {

                synchronized (LogConfig.getWriteLock(logName + "_" + LogConst.ERROR)) {
                    ToFile.writeFile(logName, LogConst.ERROR_NAME, errorPath, info.concat("\r\n").concat(content));
                }
            }

            if (isToCustom && isEnabledCustom()) {
                ToCustom.add(logName, LogConst.ERROR_NAME, info, info.concat("\r\n").concat(content));
            }
        }
    }

    public void error(String content, boolean isToCustom) {
        if (levelNumber >= LogConst.ERROR) {
            if (isConsole) {
                ToConsole.printConsole(logName, LogConst.ERROR_NAME, content);
            }
            if (isFile) {

                synchronized (LogConfig.getWriteLock(logName + "_" + LogConst.ERROR)) {
                    ToFile.writeFile(logName, LogConst.ERROR_NAME, errorPath, content);
                }
            }

            if (isToCustom && isEnabledCustom()) {
                ToCustom.add(logName, LogConst.ERROR_NAME, "", content);
            }
        }
    }


    public void error(String tmpl, Object... args) {
        String format = String.format(tmpl, args);
        error(format);
    }


    public void error(Exception e, String tmpl, Object... args) {
        String format = String.format(tmpl, args);
        error(format, e, false);
    }


    public void warn(boolean info) {
        warn(String.valueOf(info), true);
    }

    public void warn(int info) {
        warn(String.valueOf(info), true);
    }

    public void warn(long info) {
        warn(String.valueOf(info), true);
    }

    public void warn(float info) {
        warn(String.valueOf(info), true);
    }

    public void warn(double info) {
        warn(String.valueOf(info), true);
    }

    public void warn(String info) {
        warn(info, true);
    }

    public void warn(String info, boolean isToCustom) {
        if (levelNumber >= LogConst.WARN) {
            if (isConsole) {
                ToConsole.printConsole(logName, LogConst.WARN_NAME, info);
            }
            if (isFile) {

                synchronized (LogConfig.getWriteLock(logName + "_" + LogConst.WARN)) {
                    ToFile.writeFile(logName, LogConst.WARN_NAME, warnPath, info);
                }
            }

            if (isToCustom && isEnabledCustom()) {
                ToCustom.add(logName, LogConst.WARN_NAME, "", info);
            }
        }
    }


    public void warn(String tmpl, Object... args) {
        String format = String.format(tmpl, args);
        warn(format);
    }


    public void info(boolean info) {
        info(String.valueOf(info), true);
    }

    public void info(int info) {
        info(String.valueOf(info), true);
    }

    public void info(long info) {
        info(String.valueOf(info), true);
    }

    public void info(float info) {
        info(String.valueOf(info), true);
    }

    public void info(double info) {
        info(String.valueOf(info), true);
    }

    public void info(String info) {
        info(info, true);
    }

    public void info(String info, boolean isToCustom) {
        if (levelNumber >= LogConst.INFO) {
            if (isConsole) {
                ToConsole.printConsole(logName, LogConst.INFO_NAME, info);
            }
            if (isFile) {

                synchronized (LogConfig.getWriteLock(logName + "_" + LogConst.INFO)) {
                    ToFile.writeFile(logName, LogConst.INFO_NAME, infoPath, info);
                }
            }

            if (isToCustom && isEnabledCustom()) {
                ToCustom.add(logName, LogConst.INFO_NAME, "", info);
            }
        }
    }


    public void info(String tmpl, Object... args) {
        String format = String.format(tmpl, args);
        info(format);
    }


    public void debug(boolean info) {
        debug(String.valueOf(info), true);
    }

    public void debug(int info) {
        debug(String.valueOf(info), true);
    }

    public void debug(long info) {
        debug(String.valueOf(info), true);
    }

    public void debug(float info) {
        debug(String.valueOf(info), true);
    }

    public void debug(double info) {
        debug(String.valueOf(info), true);
    }

    public void debug(String info) {
        debug(String.valueOf(info), true);
    }

    public void debug(String info, boolean isToCustom) {
        if (levelNumber >= LogConst.DEBUG) {
            if (isConsole) {
                ToConsole.printConsole(logName, LogConst.DEBUG_NAME, info);
            }
            if (isFile) {

                synchronized (LogConfig.getWriteLock(logName + "_" + LogConst.DEBUG)) {
                    ToFile.writeFile(logName, LogConst.DEBUG_NAME, debugPath, info);
                }
            }


            if (isToCustom && isEnabledCustom()) {
                ToCustom.add(logName, LogConst.DEBUG_NAME, "", info);
            }
        }
    }


    public void debug(String tmpl, Object... args) {
        String format = String.format(tmpl, args);
        debug(format);
    }
}