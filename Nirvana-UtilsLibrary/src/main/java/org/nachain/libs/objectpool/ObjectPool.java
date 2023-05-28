package org.nachain.libs.objectpool;

import java.util.Vector;

public class ObjectPool implements java.io.Serializable {

    private static final long serialVersionUID = -7107494714758243906L;

    private final Class<?> objClass;

    private final Vector<Object> freeStack;

    public ObjectPool() {
        objClass = null;
        freeStack = new Vector<Object>();
    }

    public ObjectPool(Class<?> objClass) {
        this.objClass = objClass;
        freeStack = new Vector<Object>();
    }

    public ObjectPool(String className) {
        try {
            objClass = Class.forName(className);
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe);
        }
        freeStack = new Vector<Object>();
    }

    public ObjectPool(Class<?> type, int size) {
        objClass = type;
        freeStack = new Vector<Object>(size);
    }


    public synchronized Object getInstanceIfFree() {


        if (!freeStack.isEmpty()) {

            Object result = freeStack.lastElement();
            freeStack.setSize(freeStack.size() - 1);

            return result;
        }

        return null;
    }


    public synchronized Object getInstance() {


        if (freeStack.isEmpty()) {

            try {
                return objClass.newInstance();
            } catch (InstantiationException e) {

                throw new RuntimeException("Error creating new object to pool: instance exception", e);
            } catch (IllegalAccessException e) {

                throw new RuntimeException("Error creating new object to pool: illegal entry exception", e);
            }
        } else {

            Object result = freeStack.lastElement();

            freeStack.setSize(freeStack.size() - 1);

            return result;
        }
    }


    public synchronized void freeInstance(Object obj) {

        freeStack.addElement(obj);


    }
}