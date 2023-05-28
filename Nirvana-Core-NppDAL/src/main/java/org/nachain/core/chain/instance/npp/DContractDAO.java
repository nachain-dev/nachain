package org.nachain.core.chain.instance.npp;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;

public class DContractDAO extends RocksDAO {


    public DContractDAO() throws RocksDBException, IOException {
        super("DContract");
    }


    public DContractDAO(String dbName) throws RocksDBException, IOException {
        super(dbName);
    }


    public boolean add(DContract dContract) throws RocksDBException {
        String result = db.get(dContract.getHash());

        if (result != null) {
            return false;
        }
        put(dContract.getHash(), dContract);
        return true;
    }


    public DContract find(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, DContract.class);
    }


    public List<DContract> findAll() {
        List dContractList = db.findAll(DContract.class);
        return dContractList;
    }
}

