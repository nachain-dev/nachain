package org.nachain.core.dapp.internal.supernode.vote.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.util.JsonUtils;


public class CancelCandidateDTO {


    private String candidateAddress;

    public String getCandidateAddress() {
        return candidateAddress;
    }

    public CancelCandidateDTO setCandidateAddress(String candidateAddress) {
        this.candidateAddress = candidateAddress;
        return this;
    }

    @Override
    public String toString() {
        return "CancelCandidateDTO{" +
                "candidateAddress='" + candidateAddress + '\'' +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }
}
