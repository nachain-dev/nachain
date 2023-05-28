package org.nachain.core.persistence.page;

import java.util.List;


public class Page {


    String pageClass;


    String pageKey;


    long pageNum;


    List<String> items;

    public String getPageClass() {
        return pageClass;
    }

    public Page setPageClass(String pageClass) {
        this.pageClass = pageClass;
        return this;
    }

    public String getPageKey() {
        return pageKey;
    }

    public Page setPageKey(String pageKey) {
        this.pageKey = pageKey;
        return this;
    }

    public long getPageNum() {
        return pageNum;
    }

    public Page setPageNum(long pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public List<String> getItems() {
        return items;
    }

    public Page setItems(List<String> items) {
        this.items = items;
        return this;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageClass='" + pageClass + '\'' +
                ", pageKey='" + pageKey + '\'' +
                ", pageNum=" + pageNum +
                ", items=" + items +
                '}';
    }


    public String toKey() {
        return pageClass + "_" + pageKey + "_" + pageNum;
    }


    public Page setPageClass(Class pageClass) {
        this.pageClass = pageClass.getSimpleName();
        return this;
    }


    public Page addItem(String item) {
        items.add(item);
        return this;
    }

}
