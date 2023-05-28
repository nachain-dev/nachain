package org.nachain.core.dapp.internal.supernode.vote.vo;

import java.math.BigInteger;


public class CandidateVO {


    private String candidateAddress;


    private BigInteger pledgeAmount;


    private BigInteger voteAmount;

    public String getCandidateAddress() {
        return candidateAddress;
    }

    public void setCandidateAddress(String candidateAddress) {
        this.candidateAddress = candidateAddress;
    }

    public BigInteger getPledgeAmount() {
        return pledgeAmount;
    }

    public void setPledgeAmount(BigInteger pledgeAmount) {
        this.pledgeAmount = pledgeAmount;
    }

    public BigInteger getVoteAmount() {
        return voteAmount;
    }

    public void setVoteAmount(BigInteger voteAmount) {
        this.voteAmount = voteAmount;
    }

    @Override
    public String toString() {
        return "CandidateVO{" +
                "candidateAddress='" + candidateAddress + '\'' +
                ", pledgeAmount=" + pledgeAmount +
                ", voteAmount=" + voteAmount +
                '}';
    }
}
