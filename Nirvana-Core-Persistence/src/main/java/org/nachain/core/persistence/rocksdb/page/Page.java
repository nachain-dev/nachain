package org.nachain.core.persistence.rocksdb.page;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.nachain.core.util.JsonUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;


public class Page<T> {


    private SortEnum sortEnum;


    private long pageNum;


    private long pageSize;


    private long pageTotal;


    private long dataTotal;


    private List<T> dataList;


    private List<String> keyList;

    public Page() {
        this.dataList = new ArrayList<>();
    }

    public Page(SortEnum sortEnum, long pageNum, long pageSize) {
        this();
        this.sortEnum = sortEnum;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Page(SortEnum sortEnum, long pageNumber, long pageSize, long dataTotal) {
        this(sortEnum, pageNumber, pageSize);
        this.dataTotal = dataTotal;

        doCaclPage();
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;

        doCaclPage();
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;

        doCaclPage();
    }

    public long getDataTotal() {
        return dataTotal;
    }

    public void setDataTotal(long dataTotal) {
        this.dataTotal = dataTotal;

        doCaclPage();
    }

    public long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(long pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;

        dataTotal = dataList.size();

        doCaclPage();
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public Page<T> setKeyList(List<String> keyList) {
        this.keyList = keyList;
        return this;
    }


    private void doCaclPage() {
        if (pageSize != 0) {
            this.pageTotal = dataTotal / pageSize;

            if (dataTotal % pageSize != 0) {
                this.pageTotal++;
            }
        }
    }


    public String getNextSeek() {
        List<String> keys = Lists.newArrayList(keyList.iterator());

        Collections.sort(keys);


        String nextSeek = keys.size() > 0 ? keys.get(0) : "";

        return nextSeek;
    }


    public long getStartId() {

        if (sortEnum == SortEnum.ASC) {
            return this.getPageNum() * this.getPageSize() - this.getPageSize() + 1;
        } else {
            return dataTotal - (this.getPageNum() * this.getPageSize() - this.getPageSize());
        }
    }


    public long getEndId() {
        if (sortEnum == SortEnum.ASC) {
            return this.getPageNum() * this.getPageSize();
        } else {
            return dataTotal - this.getPageNum() * this.getPageSize() + 1;
        }
    }


    public long getBatchId(long startId, long BATCH_SIZE) {
        long batchId = startId / BATCH_SIZE;

        if (startId % BATCH_SIZE != 0) {
            batchId++;
        }
        if (batchId == 0) {
            batchId = 1;
        }
        return batchId;
    }


    public int getBatchStartIndex(long batchId, long BATCH_SIZE, long startId) {
        int startIndex = 0;


        startIndex = (int) (BATCH_SIZE - (batchId * BATCH_SIZE - startId));


        startIndex = startIndex - 1;

        return startIndex;
    }


    public Map<byte[], byte[]> getDataMap() {
        Map<byte[], byte[]> dataMap = Maps.newLinkedHashMap();

        ListIterator<T> listIterator = dataList.listIterator();
        for (String key : keyList) {
            byte[] value = JsonUtils.objectToJson(listIterator.next()).getBytes(StandardCharsets.UTF_8);
            dataMap.put(key.getBytes(StandardCharsets.UTF_8), value);
        }

        return dataMap;
    }

}