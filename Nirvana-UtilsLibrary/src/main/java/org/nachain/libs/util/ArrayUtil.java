package org.nachain.libs.util;

import java.lang.reflect.Array;

public class ArrayUtil {


    public static Object arrayAddLength(Object oldArray, int addLength) {
        Class c = oldArray.getClass();
        if (!c.isArray())
            return null;
        Class componentType = c.getComponentType();
        int length = Array.getLength(oldArray);
        int newLength = length + addLength;
        Object newArray = Array.newInstance(componentType, newLength);
        System.arraycopy(oldArray, 0, newArray, 0, length);
        return newArray;
    }


    public static Object arrayReduceLength(Object oldArray, int reduceLength) {
        Class c = oldArray.getClass();
        if (!c.isArray())
            return null;
        Class componentType = c.getComponentType();
        int length = Array.getLength(oldArray);
        int newLength = length - reduceLength;
        Object newArray = Array.newInstance(componentType, newLength);
        System.arraycopy(oldArray, 0, newArray, 0, newLength);
        return newArray;
    }
}