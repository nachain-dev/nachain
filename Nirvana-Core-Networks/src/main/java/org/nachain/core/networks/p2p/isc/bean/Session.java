package org.nachain.core.networks.p2p.isc.bean;

import com.google.common.collect.Maps;

import java.util.Map;


public class Session {

    private Map<Object, Object> sessionMap = Maps.newHashMap();

    public void setAttribute(Object key, Object value) {
        sessionMap.put(key, value);
    }


    public Object getAttribute(Object key) {
        return sessionMap.get(key);
    }


    public void removeAttribute(String key) {
        sessionMap.remove(key);
    }
}