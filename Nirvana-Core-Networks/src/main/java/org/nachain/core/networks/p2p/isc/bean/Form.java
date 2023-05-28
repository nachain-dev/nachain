package org.nachain.core.networks.p2p.isc.bean;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.nachain.libs.util.BeanUtil;
import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.StringUtil;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


@Slf4j
public class Form {


    private Map<String, Object> textElement;

    public Form() {
    }


    public void clear() {
        textElement.clear();
    }

    public Map<String, Object> getTextElement() {
        return textElement;
    }


    public String encodeLtGt(String value) {
        String v = value;
        v = CommUtil.replace(v, "<", "&lt;");
        v = CommUtil.replace(v, ">", "&gt;");

        return v;
    }


    public String decodeLtGt(String value) {
        String v = value;
        v = CommUtil.replace(v, "&lt;", "<");
        v = CommUtil.replace(v, "&gt;", ">");

        return v;
    }


    public Form set(String key, Object value) {
        if (textElement.containsKey(key)) {

            if (value != null && value.getClass().equals(String.class)) {
                String newData = (String) value;

                if (newData.contains("<") || newData.contains(">")) {
                    newData = encodeLtGt(newData);
                }
                textElement.put(key, newData);
            } else {
                textElement.put(key, value);
            }
        } else {

            log.debug("It's not in the form " + key + " property! Can't set!");
        }

        return this;
    }


    public void setTextElement(Map<String, Object> textElement) {
        this.textElement = textElement;


        Iterator<?> iter = this.textElement.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<?, ?> entry = (Entry<?, ?>) iter.next();
            Object value = entry.getValue();
            if (value != null && value.getClass().equals(String.class)) {
                String newData = (String) value;

                if (newData.contains("<") || newData.contains(">")) {
                    newData = encodeLtGt(newData);

                    String key = (String) entry.getKey();

                    this.textElement.put(key, newData);
                }
            }
        }
    }


    public Object get(String key) {
        if (textElement.containsKey(key)) {
            return textElement.get(key);
        } else {

            log.debug("It's not in the form " + key + " property! Can't set!");
            return null;
        }
    }


    public String getString(String key) {
        String value = CommUtil.null2String(get(key));

        return value;
    }


    public String getStringByHtml(String key) {
        String value = CommUtil.null2String(get(key));


        value = decodeLtGt(value);

        return value;
    }

    public String getString(String key, String defaultValue) {
        return CommUtil.null2String(getString(key), defaultValue);
    }


    public String getStringBy_09azAZ(String key) {
        return StringUtil.clearNot_09azAZ(CommUtil.null2String(get(key)));
    }


    public boolean getBoolean(String key) {
        return CommUtil.null2Boolean(get(key));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return CommUtil.null2Boolean(get(key), defaultValue);
    }


    public int getInt(String key) {
        return CommUtil.null2Int(get(key));
    }

    public int getInt(String key, int defaultValue) {
        return CommUtil.null2Int(get(key), defaultValue);
    }


    public long getLong(String key) {
        return CommUtil.null2Long(get(key));
    }

    public long getLong(String key, long defaultValue) {
        return CommUtil.null2Long(get(key), defaultValue);
    }


    public float getFloat(String key) {
        return CommUtil.null2Float(get(key));
    }

    public float getFloat(String key, float defaultValue) {
        return CommUtil.null2Float(get(key), defaultValue);
    }


    public double getDouble(String key) {
        return CommUtil.null2Double(get(key));
    }

    public double getDouble(String key, double defaultValue) {
        return CommUtil.null2Double(get(key), defaultValue);
    }


    public Date getDate(String key) {
        return CommUtil.null2Date(get(key));
    }


    public Date getDateTime(String key) {
        return CommUtil.null2DateTime(get(key));
    }

    public <T> T getModel(Class<T> clazz) throws Exception {
        return (T) BeanUtil.value2Object(clazz, textElement, false, new String[]{});
    }

    public <T> T getObject(Class<?> clazz) throws Exception {
        return (T) BeanUtil.value2Object(clazz, textElement, false, new String[]{});
    }


    public Object getObjectButIgnore(Class<?> clazz, String[] ignoreFields) throws Exception {
        return BeanUtil.value2Object(clazz, textElement, false, ignoreFields);
    }


    public Object gets(String key) {
        Object obj = null;
        if (textElement.containsKey(key)) {
            obj = textElement.get(key);
            if (obj instanceof String) {
                String[] array = {obj.toString()};
                obj = array;
            }
        } else {

            log.debug("It's not in the form {} property! Can't set!", key);
        }
        return obj;
    }


    public String getsString(String key) {
        return CommUtil.array2String(gets(key), ",");
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
