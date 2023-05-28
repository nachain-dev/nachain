package org.nachain.core.subscribe.client.queue;

import org.nachain.core.networks.chain.BlockResultDTO;
import org.rocksdb.RocksDBException;

import java.util.List;


public class BlockQueueService {


    public static BlockQueue newBlockQueue(BlockResultDTO blockResultDTO) {
        BlockQueue blockQueue = new BlockQueue();
        blockQueue.setBlockHeight(blockResultDTO.getBlockHeight());
        blockQueue.setBlockResultDTO(blockResultDTO);
        return blockQueue;
    }


    public static void addQueue(BlockQueue blockQueue) {
        BlockQueueDAO blockQueueDAO = new BlockQueueDAO(blockQueue.getBlockResultDTO().getInstance());
        try {
            blockQueueDAO.add(blockQueue);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static void delQueue(BlockQueue blockQueue) {
        BlockQueueDAO blockQueueDAO = new BlockQueueDAO(blockQueue.getBlockResultDTO().getInstance());
        try {
            blockQueueDAO.delete(blockQueue.getBlockHeight());
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static void executeQueue(long instance) {
        BlockQueueDAO blockQueueDAO = new BlockQueueDAO(instance);

        if (blockQueueDAO.count() == 0) {
            return;
        }


        List<BlockQueue> blockQueueList = blockQueueDAO.findAll();
        for (BlockQueue blockQueue : blockQueueList) {

        }

    }


}
