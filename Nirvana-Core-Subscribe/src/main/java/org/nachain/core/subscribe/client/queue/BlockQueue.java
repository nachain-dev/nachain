package org.nachain.core.subscribe.client.queue;

import org.nachain.core.networks.chain.BlockResultDTO;


public class BlockQueue {


    private long blockHeight;


    private BlockResultDTO blockResultDTO;

    public long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public BlockResultDTO getBlockResultDTO() {
        return blockResultDTO;
    }

    public void setBlockResultDTO(BlockResultDTO blockResultDTO) {
        this.blockResultDTO = blockResultDTO;
    }

    @Override
    public String toString() {
        return "BlockQueue{" +
                "blockHeight=" + blockHeight +
                ", blockResultDTO=" + blockResultDTO +
                '}';
    }
}
