package org.nachain.libs.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Slf4j
public class BeanUtil {


    private static Map<Class<?>, List<Field>> fieldsCache = new HashMap<Class<?>, List<Field>>();


    private static List<Field> getInheritedPrivateFields(Class<?> type) {
        List<Field> result = new ArrayList<Field>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            Field[] declaredFields = i.getDeclaredFields();
            Collections.addAll(result, declaredFields);
            i = i.getSuperclass();
        }

        return result;
    }


    public static PropertyDescriptor getPropertyDescriptor(Class beanClass, String propertyName) {
        for (PropertyDescriptor pd : getPropertyDescriptors(beanClass)) {
            if (pd.getName().equals(propertyName)) {
                return pd;
            }
        }
        return null;
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class beanClass) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(beanClass);
        } catch (IntrospectionException e) {
            return (new PropertyDescriptor[0]);
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        if (descriptors == null) {
            descriptors = new PropertyDescriptor[0];
        }
        return descriptors;
    }


    public static void copyProperties(Object target, Object source) {
        copyProperties(target, source, null);
    }

    public static void copyProperties(Object target, Object source, String... ignoreProperties) {
        if (target instanceof Map) {
            throw new UnsupportedOperationException("target is Map unsupported");
        }

        PropertyDescriptor[] targetPds = getPropertyDescriptors(target.getClass());
        List ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;

        for (int i = 0; i < targetPds.length; i++) {
            PropertyDescriptor targetPd = targetPds[i];
            String targetPdName = targetPd.getName();


            if (targetPd.getWriteMethod() != null && (ignoreProperties == null || (Colls.isNotContainsButInsensitive(ignoreList, targetPdName)))) {
                try {
                    if (source instanceof Map) {
                        Map map = (Map) source;
                        if (map.containsKey(targetPdName)) {
                            Object value = map.get(targetPdName);
                            setProperty(target, targetPd, value);
                        }
                    } else {
                        PropertyDescriptor sourcePd = getPropertyDescriptors(source.getClass(), targetPdName);
                        if (sourcePd != null && sourcePd.getReadMethod() != null) {
                            Object value = getProperty(source, sourcePd);
                            if (value != null) {
                                setProperty(target, targetPd, value);
                            }
                        }
                    }
                } catch (Throwable ex) {
                    throw new IllegalArgumentException("Could not copy properties on:" + targetPd.getDisplayName(), ex);
                }
            }
        }
    }

    public static PropertyDescriptor getPropertyDescriptors(Class beanClass, String name) {
        for (PropertyDescriptor pd : getPropertyDescriptors(beanClass)) {
            if (pd.getName().equals(name)) {
                return pd;
            }
        }
        return null;
    }

    private static Object getProperty(Object source, PropertyDescriptor sourcePd) throws IllegalAccessException, InvocationTargetException {
        Method readMethod = sourcePd.getReadMethod();
        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
            readMethod.setAccessible(true);
        }
        Object value = readMethod.invoke(source, new Object[0]);
        return value;
    }


    public static void setProperty(Object target, String propertyName, Object value) {
        PropertyDescriptor pd = getPropertyDescriptor(target.getClass(), propertyName);
        if (pd == null)
            throw new IllegalArgumentException("not found property:" + propertyName + " on class:" + target.getClass());
        setProperty(target, pd, value);
    }

    private static void setProperty(Object target, PropertyDescriptor targetPd, Object value) {
        Method writeMethod = targetPd.getWriteMethod();
        if (!Modifier.isPublic(writeMethod.getDeclaringClass()
                .getModifiers())) {
            writeMethod.setAccessible(true);
        }
        try {
            writeMethod.invoke(target, new Object[]{convert(value, writeMethod.getParameterTypes()[0])});
        } catch (Exception e) {
            throw new RuntimeException("error set property:" + targetPd.getName() + " on class:" + target.getClass(), e);
        }
    }

    private static Object convert(Object value, Class<?> targetType) {
        if (value == null) return null;
        if (targetType == String.class) {
            return value.toString();
        } else if (targetType == Date.class) {
            return value;
        } else if (targetType == java.sql.Date.class) {
            return value;
        } else {
            return convert(value.toString(), targetType);
        }
    }


    public static Object value2Object(Class<?> clazz, Map<String, Object> valueMap) throws Exception {


        Object newobj = clazz.newInstance();

        List<Field> fields = fieldsCache.get(clazz);
        if (fields == null) {
            fields = getInheritedPrivateFields(clazz);
            fieldsCache.remove(clazz);
            fieldsCache.put(clazz, fields);
        }
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);


            String fieldName = field.getName();

            Class<?> fieldType = field.getType();

            String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method method = clazz.getMethod(setMethodName, new Class[]{fieldType});

            Object oo = getCompatibleValue(valueMap.get(fieldName), fieldType);

            method.invoke(newobj, oo);
        }
        return newobj;
    }


    public static Object value2Object(Class<?> clazz, Map<String, Object> valueMap, boolean includeStaticFields) throws Exception {
        return value2Object(clazz, valueMap, includeStaticFields, null);
    }


    public static Object value2Object(Class<?> clazz, Map<String, Object> valueMap, boolean includeStaticFields, String[] ignoreFields) throws Exception {

        List<String> ignoreFieldsList = Arrays.asList(ignoreFields);

        if (ignoreFields != null) {
            ignoreFieldsList = Arrays.asList(ignoreFields);
        } else {
            ignoreFieldsList = new ArrayList<String>();
        }


        Object newobj = clazz.newInstance();

        List<Field> fields = fieldsCache.get(clazz);
        if (fields == null) {
            fields = getInheritedPrivateFields(clazz);
            fieldsCache.remove(clazz);
            fieldsCache.put(clazz, fields);
        }
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            int modifiers = field.getModifiers();


            boolean aStatic = Modifier.isStatic(modifiers);
            if (!includeStaticFields && aStatic) {
                continue;
            }


            String fieldName = field.getName();


            if (ignoreFieldsList.contains(fieldName)) {
                continue;
            }


            Class<?> fieldType = field.getType();

            String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method method = clazz.getMethod(setMethodName, new Class[]{fieldType});

            Object oo = getCompatibleValue(valueMap.get(fieldName), fieldType);

            method.invoke(newobj, oo);
        }
        return newobj;
    }


    public static Map<String, Object> objectToMap(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();

        Class<? extends Object> objClass = object.getClass();


        Map<String, String> fieldsMap = new HashMap<String, String>();

        List<Field> fields = fieldsCache.get(objClass);
        if (fields == null) {
            fields = getInheritedPrivateFields(objClass);
            fieldsCache.remove(objClass);
            fieldsCache.put(objClass, fields);
        }
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);


            String fieldName = field.getName();

            fieldsMap.put(fieldName, fieldName);
        }


        Method[] methods = objClass.getDeclaredMethods();
        try {
            for (Method m : methods) {
                String methodName = m.getName();
                if (methodName.startsWith("get")) {

                    String fieldName = methodName.substring(3);

                    if (fieldsMap.containsKey(fieldName)) {

                        map.put(fieldName, m.invoke(object, new Object[0]));
                    }
                }
            }
        } catch (InvocationTargetException e) {
            log.error("ResultSetToObject error:An error occurred while calling the object", e);
        } catch (IllegalArgumentException e) {
            log.error("ResultSetToObject error:Parameter types do not match", e);
        } catch (IllegalAccessException e) {
            log.error("ResultSetToObject error:Access Violation", e);
        }

        return map;
    }


    public static Object getCompatibleValue(Object value, Class<?> type) throws ParseException {
        Object desValue = null;

        if (type.equals(int.class) || type.equals(Integer.class)) {
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                desValue = new Integer(value.toString());
            } else {

                if (type.equals(Integer.class)) {
                    desValue = null;
                } else {
                    desValue = 0;
                }
            }
        } else if (type.equals(Date.class)) {
            if (value != null) {
                String _value = value.toString();

                if (_value.indexOf(":") != -1) {


                    if (_value.indexOf("T") != -1 && _value.indexOf("Z") != -1 && _value.contains(".")) {
                        return DateUtil.parseByUTCDateTimeSSS(_value);
                    }


                    if (_value.contains(".")) {
                        desValue = DateUtil.parseByDateTimeSSS(_value);
                    } else {
                        desValue = DateUtil.parseByDateTime(_value);
                    }
                } else {
                    desValue = DateUtil.parseByDate(_value);
                }
            } else {
                desValue = null;
            }
        } else if (type.equals(java.sql.Time.class)) {
            if (value != null) {
                desValue = DateUtil.parseSqlTime(String.valueOf(value));
            } else {
                desValue = null;
            }
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                desValue = new Double(value.toString());
            } else {

                if (type.equals(Double.class)) {
                    desValue = null;
                } else {
                    desValue = 0;
                }

            }
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                desValue = new Long(value.toString());
            } else {
                if (type.equals(Long.class)) {
                    desValue = null;
                } else {
                    desValue = 0;
                }
            }
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                desValue = new Float(value.toString());
            } else {
                if (type.equals(Float.class)) {
                    desValue = null;
                } else {
                    desValue = 0;
                }
            }
        } else if (type.equals(Float.class) || type.equals(short.class)) {
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                desValue = new Short(value.toString());
            } else {
                if (type.equals(Short.class)) {
                    desValue = null;
                } else {
                    desValue = 0;
                }
            }
        } else if (type.equals(Byte.class) || type.equals(byte.class)) {
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                desValue = new Byte(value.toString());
            } else {
                if (type.equals(Byte.class)) {
                    desValue = null;
                } else {
                    desValue = 0;
                }
            }
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            if (value != null) {
                desValue = new Boolean(value.toString());
            } else {
                if (type.equals(Boolean.class)) {
                    desValue = null;
                } else {
                    desValue = false;
                }
            }
        } else if (type.equals(BigDecimal.class)) {
            if (StringUtils.isNotBlank((String) value)) {
                desValue = new BigDecimal(value.toString());
            } else {
                desValue = BigDecimal.ZERO;
            }
        } else {
            desValue = value;
        }

        return desValue;
    }


}