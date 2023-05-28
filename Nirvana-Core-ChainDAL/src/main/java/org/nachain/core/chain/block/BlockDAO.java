package org.nachain.core.chain.block;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class BlockDAO extends RocksDAO {


    public BlockDAO(long instance) throws RocksDBException, IOException {
        super("Block", instance);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<Long, Optional<Block>>() {
            public Optional<Block> load(Long blockHeight) throws RocksDBException {
                return Optional.ofNullable(get(blockHeight));
            }
        };
    }


    public Block find(long blockHeight) throws ExecutionException {
        return (Block) cache.get(blockHeight).orElse(null);
    }


    public boolean add(Block block) throws RocksDBException {

        if (get(block.getHeight()) != null) {
            return false;
        }

        put(block.getHeight(), block);
        return true;
    }


    public boolean edit(Block block) throws RocksDBException {

        put(block.getHeight(), block);
        return true;
    }


    public Block get(long blockHeight) throws RocksDBException {
        String result = db.get(blockHeight);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Block.class);
    }


    public Block getRand(int limit) {
        Block block = (Block) getRand(Block.class, limit);
        return block;
    }


    public List<Block> gets(long startHeight, long endHeight) throws RocksDBException {
        List<Block> blockList = db.gets(startHeight, endHeight, Block.class);

        return blockList;
    }
}
