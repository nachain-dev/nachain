package org.nachain.core.dapp.internal.supernode.vote.events;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.util.JsonUtils;


public class CancelVoteDTO {


    private String voteHash;

    public String getVoteHash() {
        return voteHash;
    }

    public CancelVoteDTO setVoteHash(String voteHash) {
        this.voteHash = voteHash;
        return this;
    }

    @Override
    public String toString() {
        return "CancelVoteDTO{" +
                "voteHash='" + voteHash + '\'' +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }

}
