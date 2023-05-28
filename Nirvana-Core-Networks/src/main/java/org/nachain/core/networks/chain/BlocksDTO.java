package org.nachain.core.networks.chain;


public class BlocksDTO {


    long instance;


    long blockHeight;


    long endHeight;

    public long getInstance() {
        return instance;
    }

    public BlocksDTO setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public BlocksDTO setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public long getEndHeight() {
        return endHeight;
    }

    public void setEndHeight(long endHeight) {
        this.endHeight = endHeight;
    }

    @Override
    public String toString() {
        return "BlocksDTO{" +
                "instance=" + instance +
                ", blockHeight=" + blockHeight +
                ", endHeight=" + endHeight +
                '}';
    }
}
