package org.nachain.libs.util;

import java.util.Map;


public class ThreadUtil {


    public static String invokeStackTraceText() {
        StringBuilder stringBuilder = new StringBuilder();

        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();

        StackTraceElement[] stackTraceElements = allStackTraces.get(Thread.currentThread());
        if (stackTraceElements != null && stackTraceElements.length > 0) {
            for (StackTraceElement item : stackTraceElements) {
                String itemInfo = item.toString();


                stringBuilder.append(item.toString());
                stringBuilder.append("\n");

            }
        }

        return stringBuilder.toString();
    }

}