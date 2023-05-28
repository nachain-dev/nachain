package org.nachain.libs.coll;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class MapWrap<K, V> extends LinkedHashMap<K, V> {


    public MapMapString<String, String> asStringMap() {
        MapMapString<String, String> retVal = new MapMapString<String, String>();
        Set<Map.Entry<K, V>> entries = this.entrySet();
        for (Map.Entry<K, V> item : entries) {
            K key = item.getKey();
            V value = item.getValue();
            String keyString = String.valueOf(key);
            String valueString = String.valueOf(value);
            retVal.put(keyString, valueString);
        }

        return retVal;
    }

}