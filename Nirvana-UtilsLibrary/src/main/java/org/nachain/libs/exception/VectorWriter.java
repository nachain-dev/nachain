package org.nachain.libs.exception;

import java.io.PrintWriter;
import java.util.Vector;

public class VectorWriter extends PrintWriter {
    private Vector<String> v;

    VectorWriter() {
        super(new NullWriter());
        v = new Vector<String>();
    }

    public void print(Object o) {
        v.addElement(o.toString());
    }

    public void print(char[] chars) {
        v.addElement(new String(chars));
    }

    public void print(String s) {
        v.addElement(s);
    }

    public void println(Object o) {
        v.addElement(String.valueOf(o));
    }


    public void println(char[] chars) {
        v.addElement(new String(chars));
    }

    public void println(String s) {
        v.addElement(s);
    }

    public void write(char[] chars) {
        v.addElement(new String(chars));
    }

    public void write(char[] chars, int off, int len) {
        v.addElement(new String(chars, off, len));
    }

    public void write(String s, int off, int len) {
        v.addElement(s.substring(off, off + len));
    }

    public void write(String s) {
        v.addElement(s);
    }

    public String[] toStringArray() {
        int len = v.size();
        String[] sa = new String[len];

        for (int i = 0; i < len; i++) {
            sa[i] = (String) v.elementAt(i) + "\r\n";
        }

        return sa;
    }

    public String toString() {
        StringBuffer returnValue = new StringBuffer();

        int len = v.size();
        for (int i = 0; i < len; i++) {
            returnValue.append((String) v.elementAt(i) + "\r\n");
        }

        return returnValue.toString();
    }
}