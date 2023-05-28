package org.nachain.core.dapp.internal.dao.lock;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;

public class FreeLockDAO extends RocksDAO {


    public FreeLockDAO() {
        super("FreeLock", CoreInstanceEnum.FreeLock.id);
    }


    public boolean add(FreeLock freeLock) throws RocksDBException {
        String hash = freeLock.getHash();

        if (get(hash) != null) {
            return false;
        }
        put(hash, freeLock);
        return true;
    }


    public boolean put(FreeLock freeLock) throws RocksDBException {
        put(freeLock.getHash(), freeLock);
        return true;
    }


    public FreeLock get(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, FreeLock.class);
    }


    public List findAll() {
        return db.findAll(FreeLock.class);
    }


}
