package org.nachain.libs.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.nachain.libs.util.CommUtil;

public class Field implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 3364316714496512391L;

    private Object name;

    private Object value;

    public Field() {
    }


    public Field(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Field(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Field(String name, long value) {
        this.name = name;
        this.value = value;
    }

    public Field(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public Field(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public Field(String name, Object value) {
        this.name = name;
        this.value = value;
    }


    public Field(int name, String value) {
        this.name = name;
        this.value = value;
    }

    public Field(int name, int value) {
        this.name = name;
        this.value = value;
    }

    public Field(int name, long value) {
        this.name = name;
        this.value = value;
    }

    public Field(int name, double value) {
        this.name = name;
        this.value = value;
    }

    public Field(int name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public Field(int name, Object value) {
        this.name = name;
        this.value = value;
    }


    public Field(long name, String value) {
        this.name = name;
        this.value = value;
    }

    public Field(long name, int value) {
        this.name = name;
        this.value = value;
    }

    public Field(long name, long value) {
        this.name = name;
        this.value = value;
    }

    public Field(long name, double value) {
        this.name = name;
        this.value = value;
    }

    public Field(long name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public Field(long name, Object value) {
        this.name = name;
        this.value = value;
    }


    public Field(double name, String value) {
        this.name = name;
        this.value = value;
    }

    public Field(double name, int value) {
        this.name = name;
        this.value = value;
    }

    public Field(double name, long value) {
        this.name = name;
        this.value = value;
    }

    public Field(double name, double value) {
        this.name = name;
        this.value = value;
    }

    public Field(double name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public Field(double name, Object value) {
        this.name = name;
        this.value = value;
    }


    public Field(boolean name, String value) {
        this.name = name;
        this.value = value;
    }

    public Field(boolean name, int value) {
        this.name = name;
        this.value = value;
    }

    public Field(boolean name, long value) {
        this.name = name;
        this.value = value;
    }

    public Field(boolean name, double value) {
        this.name = name;
        this.value = value;
    }

    public Field(boolean name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public Field(boolean name, Object value) {
        this.name = name;
        this.value = value;
    }


    public void setName(Object name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public Object getName() {
        return name;
    }

    public String getStringName() {
        return CommUtil.null2String(name);
    }

    public int getIntName() {
        return CommUtil.null2Int(name);
    }

    public long getLongName() {
        return CommUtil.null2Long(name);
    }

    public double getDoubleName() {
        return CommUtil.null2Double(name);
    }

    public boolean getBooleanName() {
        return CommUtil.null2Boolean(name);
    }


    public Object getValue() {
        return value;
    }

    public String getStringValue() {
        return CommUtil.null2String(value);
    }

    public int getIntValue() {
        return CommUtil.null2Int(value);
    }

    public long getLongValue() {
        return CommUtil.null2Long(value);
    }

    public double getDoubleValue() {
        return CommUtil.null2Double(value);
    }

    public boolean getBooleanValue() {
        return CommUtil.null2Boolean(value);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}