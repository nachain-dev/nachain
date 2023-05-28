package org.nachain.core.dapp.internal.supernode.vote.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;

public class VoteDTO {


    private long voteInstance;


    private String voteAddress;


    private String beneficiaryAddress;


    private String nominateAddress;


    private BigInteger amount;

    public long getVoteInstance() {
        return voteInstance;
    }

    public VoteDTO setVoteInstance(long voteInstance) {
        this.voteInstance = voteInstance;
        return this;
    }

    public String getVoteAddress() {
        return voteAddress;
    }

    public VoteDTO setVoteAddress(String voteAddress) {
        this.voteAddress = voteAddress;
        return this;
    }

    public String getBeneficiaryAddress() {
        return beneficiaryAddress;
    }

    public VoteDTO setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
        return this;
    }

    public String getNominateAddress() {
        return nominateAddress;
    }

    public VoteDTO setNominateAddress(String nominateAddress) {
        this.nominateAddress = nominateAddress;
        return this;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public VoteDTO setAmount(BigInteger amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public String toString() {
        return "VoteDTO{" +
                "voteInstance=" + voteInstance +
                ", voteAddress='" + voteAddress + '\'' +
                ", beneficiaryAddress='" + beneficiaryAddress + '\'' +
                ", nominateAddress='" + nominateAddress + '\'' +
                ", amount=" + amount +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }

}
