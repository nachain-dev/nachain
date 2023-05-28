package org.nachain.core.subscribe.client.queue;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;


public class BlockQueueDAO extends RocksDAO {


    public BlockQueueDAO(long instance) {
        super("BlockQueue", instance);
    }


    public boolean add(BlockQueue blockQueue) throws RocksDBException {
        String result = db.get(blockQueue.getBlockHeight());

        if (result != null) {
            return false;
        }
        put(blockQueue.getBlockHeight(), blockQueue);

        return true;
    }


    public boolean edit(BlockQueue blockQueue) throws RocksDBException {
        put(blockQueue.getBlockHeight(), blockQueue);
        return true;
    }


    public BlockQueue get(String blockHeight) throws RocksDBException {
        String result = db.get(blockHeight);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, BlockQueue.class);
    }


    public List<BlockQueue> findAll() {
        List all = db.findAll(BlockQueue.class);
        return all;
    }

}
