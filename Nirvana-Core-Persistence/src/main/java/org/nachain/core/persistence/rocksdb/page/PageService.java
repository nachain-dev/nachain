package org.nachain.core.persistence.rocksdb.page;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.persistence.rocksdb.SimpleDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


@Slf4j
public class PageService<T> {


    private long instanceId;

    private Class clazz;

    private SimpleDAO<T> simpleDAO;

    public PageService(Class clazz, long instanceId) {
        this.clazz = clazz;
        this.instanceId = instanceId;


        try {
            simpleDAO = new SimpleDAO(clazz, instanceId);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Page<T> findPage(SortEnum sortEnum, long pageNum, long pageSize, String seekKey) {
        return findPage(sortEnum, pageNum, pageSize, seekKey, null);
    }

    public Page<T> findPage(SortEnum sortEnum, long pageNum, long pageSize, PageCallback pageCallback) {
        return findPage(sortEnum, pageNum, pageSize, null, pageCallback);
    }


    public Page<T> findPage(SortEnum sortEnum, long pageNum, long pageSize, String seekKey, PageCallback pageCallback) {
        if (pageNum <= 0) {
            pageNum = 1;
        }


        long dataTotal = simpleDAO.count();


        Page<T> page = new Page<T>(sortEnum, pageNum, pageSize, dataTotal);


        if (pageCallback != null) {
            page = pageCallback.gettingData(page);
        } else {

            Map<byte[], byte[]> dataMap;

            if (pageNum == 1) {

                dataMap = simpleDAO.db().getList(pageSize, sortEnum);
            } else {

                dataMap = simpleDAO.db().getList(seekKey.getBytes(StandardCharsets.UTF_8), pageSize, sortEnum);
            }


            List<String> keyList = Lists.newArrayList();
            List<T> dataList = Lists.newArrayList();
            if (dataMap != null) {
                for (Map.Entry<byte[], byte[]> entry : dataMap.entrySet()) {
                    Object key = entry.getKey();
                    Object value = entry.getValue();

                    String keyString = new String((byte[]) key);
                    String jsonValue = new String((byte[]) value);
                    T data = (T) JsonUtils.jsonToPojo(jsonValue, simpleDAO.getClazz());

                    keyList.add(keyString);
                    dataList.add(data);
                }

                page.setDataList(dataList);
                page.setKeyList(keyList);
            }
        }


        return page;
    }


    public T getData(long instanceId, long key) throws RocksDBException, IOException {
        SimpleDAO dao = new SimpleDAO<T>(clazz, instanceId);
        T data = (T) dao.get(String.valueOf(key));
        return data;
    }


    public T getData(long instanceId, String key) throws RocksDBException, IOException {
        SimpleDAO dao = new SimpleDAO<T>(clazz, instanceId);
        T data = (T) dao.get(key);
        return data;
    }

}
