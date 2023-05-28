package org.nachain.libs.coll;

import java.util.Collection;


public class CollUtils {

    public static boolean isEmpty(Collection coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    public static int size(Collection coll) {
        if (coll == null) {
            return 0;
        }
        return coll.size();
    }

}