package org.nachain.core.oracle;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;


public class UnverifiedPredictionDAO extends RocksDAO {


    public UnverifiedPredictionDAO(long instance) {
        super("UnverifiedPrediction", instance);
    }


    public boolean add(Prediction pe) throws RocksDBException {
        String result = db.get(pe.getHash());

        if (result != null) {
            return false;
        }
        put(pe.getHash(), pe);
        return true;
    }


    public Prediction get(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Prediction.class);
    }


    public boolean delete(String hash) throws RocksDBException {
        return super.delete(hash);
    }

}
