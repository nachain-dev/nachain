package org.nachain.core.chain.structure.instance;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.Optional;
import java.util.concurrent.ExecutionException;


public class InstanceDetailDAO extends RocksDAO {


    public InstanceDetailDAO() {
        super("InstanceDetail");
    }


    public InstanceDetailDAO(String dbName) {
        super(dbName);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<Long, Optional<InstanceDetail>>() {
            public Optional<InstanceDetail> load(Long instance) throws RocksDBException {
                return Optional.ofNullable(get(instance));
            }
        };
    }


    public InstanceDetail find(long instance) throws ExecutionException {
        return (InstanceDetail) cache.get(instance).orElse(null);
    }


    public boolean put(InstanceDetail detail) throws RocksDBException {
        put(detail.getId(), detail);
        return true;
    }


    public InstanceDetail get(long instance) throws RocksDBException {
        String result = db.get(instance);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, InstanceDetail.class);
    }


}
