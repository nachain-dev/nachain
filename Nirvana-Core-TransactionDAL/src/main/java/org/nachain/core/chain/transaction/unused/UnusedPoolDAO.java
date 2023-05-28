package org.nachain.core.chain.transaction.unused;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.Optional;
import java.util.concurrent.ExecutionException;


public class UnusedPoolDAO extends RocksDAO {


    public UnusedPoolDAO(long instance) {
        super("UnusedPool", instance);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<UnusedPool>>() {
            public Optional<UnusedPool> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public UnusedPool find(String walletAddress) throws ExecutionException {
        return (UnusedPool) cache.get(walletAddress).orElse(null);
    }


    public boolean put(UnusedPool unusedPool) throws RocksDBException {
        String key = unusedPool.getWalletAddress();
        put(key, unusedPool);
        return true;
    }


    public UnusedPool get(String walletAddress) throws RocksDBException {
        String result = db.get(walletAddress);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, UnusedPool.class);
    }

}
