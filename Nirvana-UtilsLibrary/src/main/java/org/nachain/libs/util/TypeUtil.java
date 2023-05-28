package org.nachain.libs.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


public final class TypeUtil {

    public static boolean isInt(Field field) {
        if (field == null) {
            return false;
        }

        Class<?> type = field.getType();

        return type == int.class || type == Integer.class;
    }

    public static boolean isInt(Class<?> clazz) {
        return clazz == int.class || clazz == Integer.class;
    }

    public static boolean isLong(Field field) {
        if (field == null) {
            return false;
        }

        Class<?> type = field.getType();

        return type == long.class || type == Long.class;
    }

    public static boolean isLong(Class<?> clazz) {
        return clazz == long.class || clazz == Long.class;
    }

    public static boolean isBoolean(Field field) {
        if (field == null) {
            return false;
        }
        Class<?> type = field.getType();

        return type == boolean.class || type == Boolean.class;
    }

    public static boolean isBoolean(Class<?> clazz) {
        return clazz == boolean.class || clazz == Boolean.class;
    }

    public static boolean isDate(Field field) {
        if (field == null) {
            return false;
        }
        return field.getType() == Date.class;
    }

    public static boolean isDate(Class<?> clazz) {
        return clazz == Date.class;
    }

    public static boolean isFloat(Field field) {
        if (field == null) {
            return false;
        }

        Class<?> type = field.getType();

        return type == float.class || type == Float.class;
    }


    public static boolean isTimestamp(Class<?> clazz) {
        return clazz == Timestamp.class;
    }

    public static boolean isTimestamp(Field field) {
        if (field == null) {
            return false;
        }
        Class<?> type = field.getType();
        return type == Timestamp.class;
    }

    public static boolean isFloat(Class<?> clazz) {
        return clazz == float.class || clazz == Float.class;
    }

    public static boolean isDouble(Field field) {
        if (field == null) {
            return false;
        }

        Class<?> type = field.getType();

        return type == double.class || type == Double.class;
    }

    public static boolean isDouble(Class<?> clazz) {
        return clazz == double.class || clazz == Double.class;
    }

    public static boolean isString(Field field) {
        if (field == null) {
            return false;
        }
        return field.getType() == String.class;
    }

    public static boolean isString(Class<?> clazz) {
        return clazz == String.class;
    }

    public static boolean isBigDecimal(Field field) {
        if (field == null) {
            return false;
        }
        return field.getType() == BigDecimal.class;
    }

    public static boolean isBigDecimal(Class<?> clazz) {
        return clazz == BigDecimal.class;
    }

    public static boolean isByte(Field field) {
        if (field == null) {
            return false;
        }

        Class<?> type = field.getType();

        return type == Byte.class || type == byte.class;
    }

    public static boolean isByte(Class<?> clazz) {
        return clazz == Byte.class || clazz == byte.class;
    }

}