package org.nachain.libs.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class IPUtil {

    public static long ipToLong(String strIp) {
        String[] ip = strIp.split("\\.");
        return (Long.parseLong(ip[0]) << 24) + (Long.parseLong(ip[1]) << 16) + (Long.parseLong(ip[2]) << 8) + Long.parseLong(ip[3]);
    }


    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");

        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");

        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");

        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");

        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }


    public static String getRealIP(String ipString) {
        String ip = "";

        String[] items = ipString.split(";");

        Map<String, String> itemMap = new HashMap<String, String>();
        for (String s : items) {
            String key = s.substring(0, s.indexOf(":"));
            String value = s.substring(s.indexOf(":") + 1);
            itemMap.put(key, value);
        }
        String X_Real_IP = itemMap.get("X-Real-IP");
        String X_Forwarded_For = itemMap.get("X-Forwarded-For");
        if (X_Forwarded_For.contains(",")) {
            ip = X_Forwarded_For.substring(0, X_Forwarded_For.indexOf(","));
        } else {
            ip = X_Real_IP;
        }

        ip = ip.trim();

        return ip;
    }


    public static boolean isIP(String str) {
        Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        return pattern.matcher(str).matches();
    }


}