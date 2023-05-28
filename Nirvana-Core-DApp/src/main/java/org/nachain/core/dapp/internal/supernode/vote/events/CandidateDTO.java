package org.nachain.core.dapp.internal.supernode.vote.events;


public class CandidateDTO {


    private String candidateAddress;

    public String getCandidateAddress() {
        return candidateAddress;
    }

    public CandidateDTO setCandidateAddress(String candidateAddress) {
        this.candidateAddress = candidateAddress;
        return this;
    }

    @Override
    public String toString() {
        return "CandidateDTO{" +
                "candidateAddress='" + candidateAddress + '\'' +
                '}';
    }
}
