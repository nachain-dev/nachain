package org.nachain.core.networks.chain;

public class BlockDTO {


    long instance;


    long blockHeight;

    public long getInstance() {
        return instance;
    }

    public BlockDTO setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public BlockDTO setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    @Override
    public String toString() {
        return "BlockDTO{" +
                "instance=" + instance +
                ", blockHeight=" + blockHeight +
                '}';
    }
}
