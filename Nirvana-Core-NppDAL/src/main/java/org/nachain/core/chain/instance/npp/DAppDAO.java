package org.nachain.core.chain.instance.npp;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;

public class DAppDAO extends RocksDAO {


    public DAppDAO() throws RocksDBException, IOException {
        super("DApp");
    }


    public DAppDAO(String dbName) throws RocksDBException, IOException {
        super(dbName);
    }


    public boolean add(DApp dApp) throws RocksDBException {
        String result = db.get(dApp.getHash());

        if (result != null) {
            return false;
        }
        put(dApp.getHash(), dApp);
        return true;
    }


    public DApp find(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, DApp.class);
    }


    public List<DApp> findAll() {
        List dAppList = db.findAll(DApp.class);
        return dAppList;
    }
}

