package org.nachain.core.persistence.rocksdb;

import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;

public class SimpleDAO<T> extends RocksDAO {

    private Class<T> clazz;


    String simpleName;


    public SimpleDAO(Class clazz, long instance) throws RocksDBException, IOException {
        this.clazz = clazz;
        this.simpleName = clazz.getSimpleName();

        super.init(simpleName, instance);
    }

    public Class getClazz() {
        return clazz;
    }

    public String getSimpleName() {
        return simpleName;
    }


    public boolean add(String key, T t) throws RocksDBException {

        if (db.get(key) != null) {
            return false;
        }
        put(key, t);
        return true;
    }


    public boolean edit(String key, T t) throws RocksDBException {
        put(key, t);
        return true;
    }


    public T get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, clazz);
    }


    public List<T> findAll() {
        List dataList = db.findAll(clazz);
        return dataList;
    }

}
