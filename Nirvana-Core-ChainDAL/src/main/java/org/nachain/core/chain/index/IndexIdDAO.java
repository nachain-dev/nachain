package org.nachain.core.chain.index;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class IndexIdDAO extends RocksDAO {


    public IndexIdDAO(long instance) {
        super("IndexId", instance);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<IndexId>>() {
            public Optional<IndexId> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public IndexId find(String key) throws ExecutionException {
        return (IndexId) cache.get(key).orElse(null);
    }


    public IndexId find(String indexName, long indexId) throws ExecutionException {
        return (IndexId) cache.get(String.format("%s_%d", indexName, indexId)).orElse(null);
    }


    public IndexId find(String indexName, BigInteger indexId) throws ExecutionException {
        return (IndexId) cache.get(String.format("%s_%s", indexName, indexId)).orElse(null);
    }


    public boolean add(String indexName, long indexId, String value) throws RocksDBException {
        return add(new IndexId().setIndexId(String.format("%s_%d", indexName, indexId)).setIndexValue(value));
    }


    public boolean add(String indexName, BigInteger indexId, String value) throws RocksDBException {
        return add(new IndexId().setIndexId(String.format("%s_%s", indexName, indexId.toString())).setIndexValue(value));
    }


    public boolean add(IndexId txIndex) throws RocksDBException {

        if (get(txIndex.getIndexId()) != null) {
            return false;
        }
        put(txIndex.getIndexId(), txIndex);
        return true;
    }


    public IndexId get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, IndexId.class);
    }


    public long count() {
        return db.count();
    }

}
