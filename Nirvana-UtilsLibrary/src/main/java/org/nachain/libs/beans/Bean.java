package org.nachain.libs.beans;

import org.nachain.libs.util.CommUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Bean implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 4352330762019408884L;

    private Map<Object, Object> data = new HashMap<Object, Object>();

    public Bean() {
    }

    public Bean(Object key, Object value) {
        data.put(key, value);
    }


    public int getSize() {
        return data.size();
    }


    public Bean set(String key, Object value) {
        data.put(key, value);

        return this;
    }


    public int getInt(String key) {
        return CommUtil.null2Int(data.get(key));
    }


    public double getDouble(String key) {
        return CommUtil.null2Double(data.get(key));
    }


    public String getString(String key) {
        return CommUtil.null2String(data.get(key));
    }


    public Object get(String key) {
        return data.get(key);
    }


    public Date getDate(String key) {
        return CommUtil.null2Date(data.get(key));
    }


    public Date getDateTime(String key) {
        return CommUtil.null2DateTime(data.get(key));
    }
}