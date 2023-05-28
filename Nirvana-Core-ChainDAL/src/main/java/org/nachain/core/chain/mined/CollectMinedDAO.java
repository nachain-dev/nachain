package org.nachain.core.chain.mined;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class CollectMinedDAO extends RocksDAO {


    public CollectMinedDAO(long instance) {
        super("CollectMined", instance);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<Long, Optional<CollectMined>>() {
            public Optional<CollectMined> load(Long key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public CollectMined find(long blockHeight) throws ExecutionException {
        return (CollectMined) cache.get(blockHeight).orElse(null);
    }


    public boolean put(CollectMined collectMined) throws RocksDBException {
        put(collectMined.getBlockHeight(), collectMined);
        return true;
    }


    public CollectMined get(long blockHeight) throws RocksDBException {
        String result = db.get(String.valueOf(blockHeight));
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, CollectMined.class);
    }

}
