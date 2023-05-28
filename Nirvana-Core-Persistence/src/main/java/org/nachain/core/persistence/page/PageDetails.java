package org.nachain.core.persistence.page;


public class PageDetails {


    String pageClass;


    String pageKey;


    long pageTotal;


    long pageSize;


    long itemTotal;

    public String getPageClass() {
        return pageClass;
    }

    public PageDetails setPageClass(String pageClass) {
        this.pageClass = pageClass;
        return this;
    }

    public long getPageTotal() {
        return pageTotal;
    }

    public PageDetails setPageTotal(long pageTotal) {
        this.pageTotal = pageTotal;
        return this;
    }

    public long getPageSize() {
        return pageSize;
    }

    public PageDetails setPageSize(long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getPageKey() {
        return pageKey;
    }

    public PageDetails setPageKey(String pageKey) {
        this.pageKey = pageKey;
        return this;
    }

    public long getItemTotal() {
        return itemTotal;
    }

    public PageDetails setItemTotal(long itemTotal) {
        this.itemTotal = itemTotal;
        return this;
    }

    @Override
    public String toString() {
        return "PageDetails{" +
                "pageClass='" + pageClass + '\'' +
                ", pageKey='" + pageKey + '\'' +
                ", pageTotal=" + pageTotal +
                ", pageSize=" + pageSize +
                ", itemTotal=" + itemTotal +
                '}';
    }


    public PageDetails setPageClass(Class pageClazz) {
        this.pageClass = pageClazz.getSimpleName();
        return this;
    }


    public String toKey() {
        return pageClass + "_" + pageKey;
    }


    public void addPageTotal() {
        pageTotal += 1;
    }


    public void addItemTotal() {
        itemTotal += 1;
    }

}
