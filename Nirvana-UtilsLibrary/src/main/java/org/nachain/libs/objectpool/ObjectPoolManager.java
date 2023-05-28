package org.nachain.libs.objectpool;

import java.util.HashMap;


public final class ObjectPoolManager {


    private static HashMap<Class<?>, ObjectPool> objectPools = new HashMap<Class<?>, ObjectPool>();


    private ObjectPoolManager() {
    }


    private static synchronized ObjectPool getPool(Class<?> objClass) {
        ObjectPool pool = (ObjectPool) objectPools.get(objClass);

        if (pool == null) {
            pool = new ObjectPool(objClass);
            objectPools.put(objClass, pool);
        }

        return pool;
    }


    public static synchronized Object getInstance(Class<?> clazz) {
        return getPool(clazz).getInstance();
    }


    public static synchronized void freeInstance(Object obj) {
        getPool(obj.getClass()).freeInstance(obj);
    }


}