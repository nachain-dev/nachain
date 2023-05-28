package org.nachain.core.oracle;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;

public class PredictionDAO extends RocksDAO {


    public PredictionDAO(long instance) throws RocksDBException, IOException {
        super("Prediction", instance);
    }


    public boolean add(Prediction pe) throws RocksDBException {
        String result = db.get(pe.getHash());

        if (result != null) {
            return false;
        }
        put(pe.getHash(), pe);
        return true;
    }


    public boolean put(Prediction pe) throws RocksDBException {
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


}
