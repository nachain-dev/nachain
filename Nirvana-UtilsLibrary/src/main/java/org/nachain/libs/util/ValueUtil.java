package org.nachain.libs.util;

import java.util.HashMap;
import java.util.Map;

public class ValueUtil {


    public static HashMap<String, String> analyzeParameter(String data) {
        return analyzeParameter(data, "&");
    }


    public static HashMap<String, String> analyzeParameter(String data, String splitWord) {
        HashMap<String, String> parameterMap = new HashMap<String, String>();

        if (data == null || data.equals(""))
            return parameterMap;


        String[] parameterParts = data.split(splitWord);
        for (int i = 0; i < parameterParts.length; i++) {
            String[] parameter = parameterParts[i].split("=");
            String key = null;
            String name = null;
            if (parameter.length >= 1) {
                key = parameter[0];
            }
            if (parameter.length >= 2) {
                name = parameter[1];
            }
            if (key != null && name != null) {
                parameterMap.put(key, name);
            }
        }

        return parameterMap;
    }


    public static String setParameter(String parameter, String key, String value) {
        return setParameter(parameter, key, value, "&");
    }


    public static String setParameter(String parameter, String key, String value, String splitWord) {

        String par = parameter;

        Map paraMap = analyzeParameter(par);
        paraMap.put(key, value);


        par = "";
        for (Object obj : paraMap.entrySet()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) obj;
            String k = entry.getKey();
            String v = entry.getValue();
            par += splitWord + k + "=" + v;
        }
        par = par.substring(1);

        return par;
    }


    public static String getParameter(String parameter, String key) {
        return getParameter(parameter, key, "&");
    }


    public static String getParameter(String parameter, String key, String splitWord) {

        Map paraMap = getParameterMap(parameter, splitWord);

        return CommUtil.null2String(paraMap.get(key));
    }


    public static Map getParameterMap(String parameter, String splitWord) {

        Map paraMap = analyzeParameter(parameter, splitWord);

        return paraMap;
    }


    public static boolean checkValueExist(String values, String value) {
        boolean returnValue = false;

        if (values != null)
            returnValue = values.contains(value);

        return returnValue;
    }


    public static String addValue(String values, String value) {
        String returnValue = values;


        if (!returnValue.contains(value)) {
            if (returnValue.equals("")) {
                returnValue = value;
            } else {
                returnValue = returnValue + "," + value;
            }
        }

        return returnValue;
    }


    public static String delValue(String values, String value) {
        String returnValue = values;


        if (returnValue.contains(value)) {
            if (returnValue.contains("," + value)) {
                returnValue = CommUtil.replace(returnValue, "," + value, "");
            } else {
                returnValue = CommUtil.replace(returnValue, value, "");
            }
        }

        return returnValue;
    }
}
