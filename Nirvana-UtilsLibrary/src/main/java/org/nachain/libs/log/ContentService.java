package org.nachain.libs.log;

import org.nachain.libs.exception.ExceptionHandle;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ContentService {

    private static String newline = System.getProperty("line.separator");


    public static String getContent(String logName, String loggerLevel, String info) {
        StringBuffer content = new StringBuffer();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String currentTime = sdf.format(new Date(System.currentTimeMillis()));

        content.append("※※※※※ [" + logName + "." + loggerLevel + "] " + currentTime + " ※※※※※");
        content.append(newline);
        content.append(info);
        content.append(newline);

        return content.toString();
    }


    public static String getException(Object e) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String currentTime = sdf.format(new Date(System.currentTimeMillis()));

        StringBuffer exceptionInfo = new StringBuffer();
        exceptionInfo.append("Throw Time: " + currentTime + "\r\n");

        if (e.getClass().equals(Throwable.class)) {
            exceptionInfo.append(ExceptionHandle.getException((Throwable) e));
        } else {
            exceptionInfo.append(ExceptionHandle.getException((Exception) e));
        }

        return exceptionInfo.toString();
    }


}