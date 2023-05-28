package org.nachain.core.networks.chain;

import java.util.List;


public class BlocksResultDTO {


    long instance;


    long blockHeight;


    long endHeight;


    List<BlockResultDTO> blockResultDTOList;

    public long getInstance() {
        return instance;
    }

    public void setInstance(long instance) {
        this.instance = instance;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public long getEndHeight() {
        return endHeight;
    }

    public void setEndHeight(long endHeight) {
        this.endHeight = endHeight;
    }

    public List<BlockResultDTO> getBlockResultDTOList() {
        return blockResultDTOList;
    }

    public void setBlockResultDTOList(List<BlockResultDTO> blockResultDTOList) {
        this.blockResultDTOList = blockResultDTOList;
    }

    @Override
    public String toString() {
        return "BlocksResultDTO{" +
                "instance=" + instance +
                ", blockHeight=" + blockHeight +
                ", endHeight=" + endHeight +
                ", blockResultDTOList=" + blockResultDTOList +
                '}';
    }


    public void addBlockResultDTO(BlockResultDTO blockResultDTO) {
        this.blockResultDTOList.add(blockResultDTO);
    }
}
