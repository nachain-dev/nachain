package org.nachain.libs.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class List<E> implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = -1163766436173032444L;
    private ArrayList<E> data;

    public List() {
        data = new ArrayList<E>();
    }

    public void add(E obj) {
        data.add(obj);
    }

    public Object get(int index) {
        return data.get(index);
    }

    public void clear() {
        data.clear();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public String toString() {
        return data.toString();
    }

    public void del(int index) {
        data.remove(index);
    }

    public Iterator<E> iterator() {
        return data.iterator();
    }

    public ListIterator<E> listIterator() {
        return data.listIterator();
    }

    public void set(int index, E obj) {
        data.set(index, obj);
    }

}