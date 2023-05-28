package org.nachain.core.dapp.internal.supernode;

import java.math.BigInteger;


public class SuperNodeCandidate {


    private String candidateAddress;


    private BigInteger pledgeAmount;


    private long blockHeight;


    private long lockedBlockHeight;


    private String fromTx;

    public String getCandidateAddress() {
        return candidateAddress;
    }

    public SuperNodeCandidate setCandidateAddress(String candidateAddress) {
        this.candidateAddress = candidateAddress;
        return this;
    }

    public BigInteger getPledgeAmount() {
        return pledgeAmount;
    }

    public SuperNodeCandidate setPledgeAmount(BigInteger pledgeAmount) {
        this.pledgeAmount = pledgeAmount;
        return this;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public SuperNodeCandidate setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public long getLockedBlockHeight() {
        return lockedBlockHeight;
    }

    public SuperNodeCandidate setLockedBlockHeight(long lockedBlockHeight) {
        this.lockedBlockHeight = lockedBlockHeight;
        return this;
    }

    public String getFromTx() {
        return fromTx;
    }

    public SuperNodeCandidate setFromTx(String fromTx) {
        this.fromTx = fromTx;
        return this;
    }

    @Override
    public String toString() {
        return "SuperNodeCandidate{" +
                "candidateAddress='" + candidateAddress + '\'' +
                ", pledgeAmount=" + pledgeAmount +
                ", blockHeight=" + blockHeight +
                ", lockedBlockHeight=" + lockedBlockHeight +
                ", fromTx='" + fromTx + '\'' +
                '}';
    }
}
