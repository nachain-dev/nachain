package org.nachain.core.miner.schedule;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

public class ArbitramentDAO extends RocksDAO {


    public ArbitramentDAO(long instance) {
        super("Arbitrament", instance);
    }


    public static String key(Arbitrament arbitrament) {
        return arbitrament.getInstance() + "." + arbitrament.getHeight() + "." + arbitrament.getArbitratorAddress();
    }


    public boolean put(Arbitrament arbitrament) throws RocksDBException {
        put(key(arbitrament), arbitrament);
        return true;
    }


    public Arbitrament get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Arbitrament.class);
    }


}
