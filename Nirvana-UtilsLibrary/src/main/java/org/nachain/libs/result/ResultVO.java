package org.nachain.libs.result;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ResultVO<T> {

    private List<T> dataList = new ArrayList<T>();

    private ListIterator<T> listIter;

    public ResultVO() {
        listIter = dataList.listIterator();
    }


    public void resetListIter() {
        listIter = dataList.listIterator();
    }


    public List<T> getDataList() {
        return dataList;
    }


    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }


    public T next() {
        return listIter.next();
    }


    public boolean hasNext() {
        return listIter.hasNext();
    }


    public T previous() {
        return listIter.previous();
    }


    public boolean hasPrevious() {

        return listIter.hasPrevious();
    }


    public int nextIndex() {
        return listIter.nextIndex();
    }


    public int previousIndex() {
        return listIter.previousIndex();
    }

}