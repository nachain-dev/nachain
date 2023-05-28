package org.nachain.libs.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtil {


    public static List<Object> map2KeyList(Map<?, ?> map) {

        ArrayList<Object> keyList = new ArrayList<Object>();

        Iterator<?> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<?, ?> entry = (Entry<?, ?>) iter.next();
            Object key = entry.getKey();
            keyList.add(key);
        }

        return keyList;
    }


    public static List<Object> map2ValueList(Map<?, ?> map) {
        ArrayList<Object> returnValue = new ArrayList<Object>();

        Iterator<?> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<?, ?> entry = (Entry<?, ?>) iter.next();
            Object val = entry.getValue();
            returnValue.add(val);
        }

        return returnValue;
    }


    public static List<Entry<?, ?>> map2List(Map<?, ?> map) {
        ArrayList<Entry<?, ?>> returnValue = new ArrayList<Entry<?, ?>>();

        Iterator<?> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<?, ?> entry = (Entry<?, ?>) iter.next();
            returnValue.add(entry);
        }

        return returnValue;
    }

}