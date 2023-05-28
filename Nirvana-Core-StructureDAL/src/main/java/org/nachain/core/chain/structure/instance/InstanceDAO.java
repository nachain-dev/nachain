package org.nachain.core.chain.structure.instance;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;

public class InstanceDAO extends RocksDAO {


    public InstanceDAO() {
        super("Instance");
    }


    public InstanceDAO(String dbName) {
        super(dbName);
    }


    public boolean add(Instance instance) throws RocksDBException {
        String result = db.get(instance.getHash());

        if (result != null) {
            return false;
        }
        put(instance.getHash(), instance);
        return true;
    }


    public boolean put(Instance instance) throws RocksDBException {
        put(instance.getHash(), instance);
        return true;
    }


    public Instance find(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Instance.class);
    }


    public List<Instance> findAll() {
        List instanceList = db.findAll(Instance.class);
        return instanceList;
    }


    public long count() {
        return db.count();
    }


}
