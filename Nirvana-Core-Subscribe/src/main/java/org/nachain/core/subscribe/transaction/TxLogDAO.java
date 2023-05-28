package org.nachain.core.subscribe.transaction;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;


public class TxLogDAO extends RocksDAO {


    public TxLogDAO() {
        super("TxLog");
    }


    public boolean add(TxLog txLog) throws RocksDBException {
        String result = db.get(txLog.getTxHash());

        if (result != null) {
            return false;
        }
        put(txLog.getTxHash(), txLog);

        return true;
    }


    public boolean edit(TxLog txLog) throws RocksDBException {
        put(txLog.getTxHash(), txLog);
        return true;
    }


    public TxLog get(String txHash) throws RocksDBException {
        String result = db.get(txHash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, TxLog.class);
    }


    public List<TxLog> findAll() {
        List all = db.findAll(TxLog.class);
        return all;
    }

}
