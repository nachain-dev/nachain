package org.nachain.libs.util.sort;

import org.nachain.libs.beans.Field;

import java.util.Collections;
import java.util.List;

public class SortUtil {


    public static void up(List<?> dataList) {
        Collections.sort(dataList, new Sort(Sort.UP));
    }


    public static void down(List<?> dataList) {
        Collections.sort(dataList, new Sort(Sort.DOWM));
    }


    public static void fieldUp(List<Field> dataList) {
        Collections.sort(dataList, new FieldSort(FieldSort.UP));
    }

    public static void fieldUpByName(List<Field> dataList) {
        Collections.sort(dataList, new FieldSort(FieldSort.UP, FieldSort.SortType_Name));
    }

    public static void fieldUpByValue(List<Field> dataList) {
        Collections.sort(dataList, new FieldSort(FieldSort.UP, FieldSort.SortType_Value));
    }


    public static void fieldDown(List<Field> dataList) {
        Collections.sort(dataList, new FieldSort(FieldSort.DOWM));
    }

    public static void fieldDownByName(List<Field> dataList) {
        Collections.sort(dataList, new FieldSort(FieldSort.DOWM, FieldSort.SortType_Name));
    }

    public static void fieldDownByValue(List<Field> dataList) {
        Collections.sort(dataList, new FieldSort(FieldSort.DOWM, FieldSort.SortType_Value));
    }

}