package org.nachain.core.miner.member;

import org.nachain.core.nodes.NodeRole;

import java.math.BigInteger;


public class Maintain {


    private long instance;


    private NodeRole nodeRole;


    private BigInteger voteTotal;

    public long getInstance() {
        return instance;
    }

    public Maintain setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public NodeRole getNodeRole() {
        return nodeRole;
    }

    public Maintain setNodeRole(NodeRole nodeRole) {
        this.nodeRole = nodeRole;
        return this;
    }

    public BigInteger getVoteTotal() {
        return voteTotal;
    }

    @Override
    public String toString() {
        return "Maintain{" +
                "instance=" + instance +
                ", nodeRole=" + nodeRole +
                ", voteTotal=" + voteTotal +
                '}';
    }

    public Maintain setVoteTotal(BigInteger voteTotal) {
        this.voteTotal = voteTotal;
        return this;
    }


    public void addVote(BigInteger amount) {
        voteTotal = voteTotal.add(amount);
    }
}
