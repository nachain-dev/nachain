package org.nachain.libs.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public final class Colls {

    public static boolean isEmpty(Collection coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    public static boolean isContainsButInsensitive(List list, String input) {
        if (isEmpty(list)) {
            return false;
        }

        if (StringUtils.isBlank(input)) {
            return false;
        }

        for (Object item : list) {
            String eachOne = String.valueOf(item);
            if (eachOne.equalsIgnoreCase(input)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNotContainsButInsensitive(List list, String input) {
        return !isContainsButInsensitive(list, input);
    }


    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {

        int listSize = list.size();
        int page = (listSize + (pageSize - 1)) / pageSize;

        List<List<T>> listArray = new ArrayList<List<T>>();
        for (int i = 0; i < page; i++) {
            List<T> subList = new ArrayList<T>();
            for (int j = 0; j < listSize; j++) {
                int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;
                if (pageIndex == (i + 1)) {
                    subList.add(list.get(j));
                }
                if ((j + 1) == ((i + 1) * pageSize)) {
                    break;
                }
            }
            listArray.add(subList);
        }
        return listArray;
    }
}