package org.nachain.libs.httpclient;

import org.nachain.libs.beans.Field;
import org.nachain.libs.util.MapUtil;
import org.nachain.libs.util.ValueUtil;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

public class HeaderHandle {


    public static void setHeaderFileds(List<Field> headerFields, String name, String value) {

        if (isExist(headerFields, name)) {

            ListIterator<Field> listIter = headerFields.listIterator();
            while (listIter.hasNext()) {
                Field field = (Field) listIter.next();
                if (field.getStringName().toLowerCase().equals(name.toLowerCase())) {
                    field.setValue(value);

                    listIter.set(field);
                    break;
                }
            }
        } else {
            headerFields.add(new Field(name, value));
        }
    }


    public static void addHeaderFileds(List<Field> headerFields, String name, String value) {

        headerFields.add(new Field(name, value));
    }


    public static Field getField(List<Field> headerFields, String name) {
        Field returnValue = null;

        for (Field field : headerFields) {
            if (field.getStringName().toLowerCase().equals(name.toLowerCase())) {
                returnValue = field;
                break;
            }
        }

        return returnValue;
    }


    public static boolean isExist(List<Field> headerFields, String name) {
        boolean returnValue = false;

        if (headerFields != null && getField(headerFields, name) != null) {
            returnValue = true;
        }

        return returnValue;
    }


    public static void delHeader(List<Field> headerFields, String name) {
        ListIterator<Field> listIter = (ListIterator<Field>) headerFields.listIterator();
        while (listIter.hasNext()) {
            Field field = (Field) listIter.next();
            if (field.getStringName().toLowerCase().equals(name.toLowerCase())) {
                listIter.remove();
                break;
            }
        }
    }


    public static String clearUselessCookies(String setCookie) {
        String returnValue = "";


        HashMap<String, String> parameterMap = ValueUtil.analyzeParameter(setCookie, "; ");
        List<Entry<?, ?>> itemList = MapUtil.map2List(parameterMap);
        for (Entry<?, ?> e : itemList) {
            String key = (String) e.getKey();
            String value = (String) e.getValue();
            if (!key.equals("expires") && !key.equals("path")) {
                returnValue += key + "=" + value + "; ";
            }
        }

        return returnValue;
    }

}