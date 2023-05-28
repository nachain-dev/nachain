package org.nachain.core.persistence;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;


public class KvDTO {


    private List<byte[]> keyList = Lists.newArrayList();


    private List<byte[]> valueList = Lists.newArrayList();


    private HashMap<byte[], Integer> indexMap = new HashMap<>();

    public List<byte[]> getKeyList() {
        return keyList;
    }

    public List<byte[]> getValueList() {
        return valueList;
    }


    public byte[] get(byte[] key) {
        int index = indexMap.get(key);
        if (index != -1) {
            return valueList.get(index);
        }

        return null;
    }


    public void add(byte[] key, byte[] value) {

        int index = keyList.size();


        keyList.add(key);
        valueList.add(value);
        indexMap.put(key, index);
    }

}
