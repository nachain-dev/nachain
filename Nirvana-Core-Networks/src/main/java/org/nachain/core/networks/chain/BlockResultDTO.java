package org.nachain.core.networks.chain;

import org.nachain.core.chain.block.Block;
import org.nachain.core.chain.block.BlockBody;
import org.nachain.core.chain.structure.instance.InstanceDetail;
import org.nachain.core.chain.transaction.Tx;

import java.util.List;


public class BlockResultDTO {


    long instance;


    long blockHeight;


    Block block;


    BlockBody blockBody;


    List<Tx> txList;


    InstanceDetail instanceDetail;


    long latestBlockHeight;

    public Block getBlock() {
        return block;
    }

    public BlockResultDTO setBlock(Block block) {
        this.block = block;
        return this;
    }

    public BlockBody getBlockBody() {
        return blockBody;
    }

    public BlockResultDTO setBlockBody(BlockBody blockBody) {
        this.blockBody = blockBody;
        return this;
    }

    public List<Tx> getTxList() {
        return txList;
    }

    public BlockResultDTO setTxList(List<Tx> txList) {
        this.txList = txList;
        return this;
    }

    public long getLatestBlockHeight() {
        return latestBlockHeight;
    }

    public BlockResultDTO setLatestBlockHeight(long latestBlockHeight) {
        this.latestBlockHeight = latestBlockHeight;
        return this;
    }

    public long getInstance() {
        return instance;
    }

    public BlockResultDTO setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public BlockResultDTO setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public InstanceDetail getInstanceDetail() {
        return instanceDetail;
    }

    public BlockResultDTO setInstanceDetail(InstanceDetail instanceDetail) {
        this.instanceDetail = instanceDetail;
        return this;
    }

    @Override
    public String toString() {
        return "BlockResultDTO{" +
                "instance=" + instance +
                ", blockHeight=" + blockHeight +
                ", block=" + block +
                ", blockBody=" + blockBody +
                ", txList=" + txList +
                ", instanceDetail=" + instanceDetail +
                ", latestBlockHeight=" + latestBlockHeight +
                '}';
    }
}
