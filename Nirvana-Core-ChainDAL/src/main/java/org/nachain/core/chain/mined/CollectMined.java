package org.nachain.core.chain.mined;

import java.math.BigInteger;


public class CollectMined {


    private long blockHeight;


    private BigInteger value;


    private BigInteger nodeValue;

    public CollectMined() {
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public CollectMined setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public BigInteger getValue() {
        return value;
    }

    public CollectMined setValue(BigInteger value) {
        this.value = value;
        return this;
    }

    public BigInteger getNodeValue() {
        return nodeValue;
    }

    public CollectMined setNodeValue(BigInteger nodeValue) {
        this.nodeValue = nodeValue;
        return this;
    }

    @Override
    public String toString() {
        return "CollectMined{" +
                "blockHeight=" + blockHeight +
                ", value=" + value +
                ", nodeValue=" + nodeValue +
                '}';
    }
}
