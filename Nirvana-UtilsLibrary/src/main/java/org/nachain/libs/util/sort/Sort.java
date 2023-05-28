package org.nachain.libs.util.sort;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.util.CommUtil;

import java.util.Comparator;

@Slf4j
public class Sort implements Comparator<Object> {


    public final static int UP = 1;
    public final static int DOWM = -1;
    public final static int Small2Big = UP;
    public final static int Big2Small = DOWM;

    private int mode;

    private Sort() {
    }

    public Sort(int mode) {
        this.mode = mode;
    }

    public int compare(Object o1, Object o2) {
        if (o1 instanceof String) {
            return compare(CommUtil.null2String(o1), CommUtil.null2String(o2));
        } else if (o1 instanceof Integer) {
            return compare(CommUtil.null2Int(o1), CommUtil.null2Int(o2));
        } else {
            log.error("No suitable comparator was found, please customize one.");
            return 0;
        }
    }


    public int compare(String o1, String o2) {
        int flag = 0;
        if (mode == Sort.DOWM) {
            flag = sortDown(o1, o2);
        } else if (mode == Sort.UP) {
            flag = sortUp(o1, o2);
        }
        return flag;
    }

    private int sortUp(String o1, String o2) {
        if (o1.compareTo(o2) < 0) {
            return -1;
        } else if (o1.compareTo(o2) > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private int sortDown(String o1, String o2) {
        if (o1.compareTo(o2) > 0) {
            return -1;
        } else if (o1.compareTo(o2) < 0) {
            return 1;
        } else {
            return 0;
        }
    }


    public int compare(int o1, int o2) {
        int flag = 0;
        if (mode == Sort.DOWM) {
            flag = sortDown(o1, o2);
        } else if (mode == Sort.UP) {
            flag = sortUp(o1, o2);
        }
        return flag;
    }

    private int sortUp(int o1, int o2) {
        if (o1 < o2) {
            return -1;
        } else if (o1 > o2) {
            return 1;
        } else {
            return 0;
        }
    }

    private int sortDown(int o1, int o2) {
        if (o1 > o2) {
            return -1;
        } else if (o1 < o2) {
            return 1;
        } else {
            return 0;
        }
    }


}