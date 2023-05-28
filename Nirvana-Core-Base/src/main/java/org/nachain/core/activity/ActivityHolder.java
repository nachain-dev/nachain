package org.nachain.core.activity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ActivityHolder {


    private static Map<String, AbstractActivity> holder = new ConcurrentHashMap<>();


    public static Map<String, AbstractActivity> getHolder() {
        return holder;
    }


    public static AbstractActivity get(Class activityClass) {
        return get(activityClass.getName());
    }


    public static void register(String activityName, AbstractActivity abstractActivity) {
        holder.put(activityName, abstractActivity);
    }


    public static AbstractActivity get(String serverName) {
        return holder.get(serverName);
    }


    public static int count() {
        return holder.size();
    }

}
