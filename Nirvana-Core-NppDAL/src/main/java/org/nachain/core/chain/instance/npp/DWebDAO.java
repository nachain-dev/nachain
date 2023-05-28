package org.nachain.core.chain.instance.npp;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;

public class DWebDAO extends RocksDAO {


    public DWebDAO() throws RocksDBException, IOException {
        super("DWeb");
    }


    public DWebDAO(String dbName) throws RocksDBException, IOException {
        super(dbName);
    }


    public boolean add(DWeb dWeb) throws RocksDBException {
        String result = db.get(dWeb.getHash());

        if (result != null) {
            return false;
        }
        put(dWeb.getHash(), dWeb);
        return true;
    }


    public DWeb find(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, DWeb.class);
    }


    public List<DWeb> findAll() {
        List dWebList = db.findAll(DWeb.class);
        return dWebList;
    }
}

