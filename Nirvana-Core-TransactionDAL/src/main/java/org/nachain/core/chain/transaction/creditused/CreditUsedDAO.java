package org.nachain.core.chain.transaction.creditused;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


public class CreditUsedDAO extends RocksDAO {


    public CreditUsedDAO(long instance) {
        super("CreditUsed", instance);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<CreditUsed>>() {
            public Optional<CreditUsed> load(String creditSourceTx) throws RocksDBException {
                return Optional.ofNullable(get(creditSourceTx));
            }
        };
    }


    public CreditUsed find(String creditSourceTx) throws RocksDBException, ExecutionException {
        return (CreditUsed) cache.get(creditSourceTx).orElse(null);
    }


    public boolean add(CreditUsed creditUsed) throws RocksDBException {
        put(creditUsed.getCreditSourceTx(), creditUsed);
        return true;
    }


    public boolean delete(String txHash) throws RocksDBException {
        return super.delete(txHash);
    }


    public CreditUsed get(String creditSourceTx) throws RocksDBException {
        String result = db.get(creditSourceTx);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, CreditUsed.class);
    }


    public List findAll(Class clazz) {
        return db.findAll(clazz);
    }
}
