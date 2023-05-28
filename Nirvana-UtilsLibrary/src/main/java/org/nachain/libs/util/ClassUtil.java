package org.nachain.libs.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
public class ClassUtil {


    public static Object getProperty(Object owner, String fieldName) throws Exception {

        Class ownerClass = owner.getClass();

        Field field = ownerClass.getField(fieldName);

        Object property = field.get(owner);

        return property;
    }


    public static void setProperty(Object owner, String fieldName, Object fieldValue) throws Exception {

        Class ownerClass = owner.getClass();

        Field field = ownerClass.getField(fieldName);

        field.set(owner, fieldValue);
    }


    public static Object getStaticProperty(String className, String fieldName) throws Exception {

        Class ownerClass = Class.forName(className);

        Field field = ownerClass.getField(fieldName);

        Object property = field.get(ownerClass);

        return property;
    }


    public static void setStaticProperty(String className, String fieldName, Object fieldValue) throws Exception {

        Class ownerClass = Class.forName(className);

        Field field = ownerClass.getField(fieldName);

        field.set(ownerClass, fieldValue);
    }


    public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
        Object returnValue;


        Class ownerClass = owner.getClass();

        Method method;
        if (args != null) {

            Class[] argsClass = new Class[args.length];
            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }

            method = ownerClass.getMethod(methodName, argsClass);
        } else {
            method = ownerClass.getMethod(methodName);
        }

        returnValue = method.invoke(owner, args);

        return returnValue;
    }


    public static Object invokeStaticMethod(String className, String methodName, Object[] args) throws Exception {
        Class ownerClass = Class.forName(className);
        Class[] argsClass = new Class[args.length];

        for (int i = 0, j = args.length; i < j; i++) {

            argsClass[i] = args[i].getClass();
        }

        Method method = ownerClass.getMethod(methodName, argsClass);

        return method.invoke(null, args);
    }


    public static Object newInstance(String className, Object[] args) throws Exception {
        Class newoneClass = Class.forName(className);
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Constructor cons = newoneClass.getConstructor(argsClass);

        return cons.newInstance(args);

    }


    public static Object newInstance(String className) throws Exception {
        Object obj = null;

        if (className != null && (!className.equals(""))) {
            obj = (Object) Class.forName(className).newInstance();
        }

        return obj;
    }


    public static boolean isInstance(Object obj, Class cls) {
        return cls.isInstance(obj);
    }


    public static String getMethodName(String methodType, String column) {
        if (column == null)
            return null;

        return methodType + StringUtil.first2UpperCase(column);
    }


    public static Method getMethodByColumn(Class<?> objClass, String methodType, String column) {
        Method method = null;

        String methodName = getMethodName(methodType, column);
        method = getMethodByColumn(objClass, methodName);

        return method;
    }


    public static Method getMethodByColumn(Class<?> objClass, String methodName) {
        Method method = null;

        Method[] methods = objClass.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                method = m;
                break;
            }
        }

        return method;
    }


    public static Class[] getParameterTypeByColumn(Class<?> objClass, String methodName) {
        Class[] typeClass = null;

        Method[] methods = objClass.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                typeClass = m.getParameterTypes();
                break;
            }
        }

        return typeClass;
    }

}