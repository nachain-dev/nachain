package org.nachain.core.oracle;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;


public class PredictedResultDAO extends RocksDAO {


    public PredictedResultDAO(long instance) throws RocksDBException, IOException {
        super("PredictedResult", instance);
    }


    public boolean add(PredictedResult pe) throws RocksDBException {
        String result = db.get(pe.getBlockHeight());

        if (result != null) {
            return false;
        }
        put(pe.getBlockHeight(), pe);
        return true;
    }


    public boolean put(PredictedResult pe) throws RocksDBException {
        put(pe.getBlockHeight(), pe);
        return true;
    }


    public PredictedResult get(long blockHeight) throws RocksDBException {
        String result = db.get(blockHeight);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, PredictedResult.class);
    }


}
