package org.nachain.libs.util.sort;

import org.nachain.libs.beans.Field;

import java.util.Comparator;

public class FieldSort implements Comparator<Object> {

    public final static int UP = 1;
    public final static int DOWM = -1;
    public final static int Small2Big = UP;
    public final static int Big2Small = DOWM;

    public final static String SortType_Name = "Name";
    public final static String SortType_Value = "Value";


    private int mode;

    private String sortType = SortType_Value;

    private FieldSort() {
    }

    public FieldSort(int mode) {
        this.mode = mode;
        this.sortType = SortType_Value;
    }

    public FieldSort(int mode, String sortType) {
        this.mode = mode;
        this.sortType = sortType;
    }


    public int compare(Object arg0, Object arg1) {
        Field o1 = (Field) arg0;
        Field o2 = (Field) arg1;
        int flag = 0;
        if (this.sortType.equals(SortType_Name)) {
            if (mode == FieldSort.DOWM) {
                flag = sortDownByName(o1, o2);
            } else if (mode == FieldSort.UP) {
                flag = sortUpByName(o1, o2);
            }
        } else if (this.sortType.equals(SortType_Value)) {
            if (mode == FieldSort.DOWM) {
                flag = sortDownByValue(o1, o2);
            } else if (mode == FieldSort.UP) {
                flag = sortUpByValue(o1, o2);
            }
        }

        return flag;
    }


    private int sortUpByName(Field o1, Field o2) {
        String name1 = o1.getStringName();
        String name2 = o2.getStringName();
        if (name1.compareTo(name2) < 0) {
            return -1;
        } else if (name1.compareTo(name2) > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private int sortDownByName(Field o1, Field o2) {
        String name1 = o1.getStringName();
        String name2 = o2.getStringName();
        if (name1.compareTo(name2) > 0) {
            return -1;
        } else if (name1.compareTo(name2) < 0) {
            return 1;
        } else {
            return 0;
        }
    }


    private int sortUpByValue(Field o1, Field o2) {
        int value1 = o1.getIntValue();
        int value2 = o2.getIntValue();
        if (value1 < value2) {
            return -1;
        } else if (value1 > value2) {
            return 1;
        } else {
            return 0;
        }
    }

    private int sortDownByValue(Field o1, Field o2) {
        int value1 = o1.getIntValue();
        int value2 = o2.getIntValue();
        if (value1 > value2) {
            return -1;
        } else if (value1 < value2) {
            return 1;
        } else {
            return 0;
        }
    }


}