package org.nachain.core.chain.instance.npp;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;

public class CoreDAO extends RocksDAO {


    public CoreDAO() throws RocksDBException, IOException {
        super("Core");
    }


    public CoreDAO(String dbName) throws RocksDBException, IOException {
        super(dbName);
    }


    public boolean add(Core core) throws RocksDBException {
        String result = db.get(core.getHash());

        if (result != null) {
            return false;
        }
        put(core.getHash(), core);
        return true;
    }


    public Core find(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Core.class);
    }


    public List<Core> findAll() {
        List coreList = db.findAll(Core.class);
        return coreList;
    }
}

