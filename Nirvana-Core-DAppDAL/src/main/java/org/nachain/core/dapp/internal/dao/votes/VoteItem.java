package org.nachain.core.dapp.internal.dao.votes;


public class VoteItem {


    String voteAddress;


    boolean voteValue;


    String fromTx;

    public String getVoteAddress() {
        return voteAddress;
    }

    public VoteItem setVoteAddress(String voteAddress) {
        this.voteAddress = voteAddress;
        return this;
    }

    public boolean isVoteValue() {
        return voteValue;
    }

    public VoteItem setVoteValue(boolean voteValue) {
        this.voteValue = voteValue;
        return this;
    }

    public String getFromTx() {
        return fromTx;
    }

    public VoteItem setFromTx(String fromTx) {
        this.fromTx = fromTx;
        return this;
    }

    @Override
    public String toString() {
        return "VoteItem{" +
                "voteAddress='" + voteAddress + '\'' +
                ", voteValue=" + voteValue +
                ", fromTx='" + fromTx + '\'' +
                '}';
    }
}
