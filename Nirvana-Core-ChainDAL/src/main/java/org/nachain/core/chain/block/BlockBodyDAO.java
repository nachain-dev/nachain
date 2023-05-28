package org.nachain.core.chain.block;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class BlockBodyDAO extends RocksDAO {


    public BlockBodyDAO(long instance) throws IOException {
        super("BlockBody", instance);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<Long, Optional<BlockBody>>() {
            public Optional<BlockBody> load(Long blockHeight) throws RocksDBException {
                return Optional.ofNullable(get(blockHeight));
            }
        };
    }


    public BlockBody find(long blockHeight) throws RocksDBException, ExecutionException {
        return (BlockBody) cache.get(blockHeight).orElse(null);
    }


    public boolean add(BlockBody blockBody) throws RocksDBException, ExecutionException {

        if (find(blockBody.getHeight()) != null) {
            return false;
        }
        put(blockBody.getHeight(), blockBody);
        return true;
    }


    public boolean edit(BlockBody blockBody) throws RocksDBException, ExecutionException {

        if (find(blockBody.getHeight()) == null) {
            return false;
        }
        put(blockBody.getHeight(), blockBody);
        return true;
    }


    public BlockBody get(long blockHeight) throws RocksDBException {
        String result = db.get(blockHeight);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, BlockBody.class);
    }


}
