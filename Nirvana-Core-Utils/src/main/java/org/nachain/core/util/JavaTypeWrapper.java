package org.nachain.core.util;

public class JavaTypeWrapper<T> {

    private T value;

    public JavaTypeWrapper() {
    }

    public JavaTypeWrapper(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
