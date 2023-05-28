package org.nachain.libs.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;


public final class CollUtil {

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
}