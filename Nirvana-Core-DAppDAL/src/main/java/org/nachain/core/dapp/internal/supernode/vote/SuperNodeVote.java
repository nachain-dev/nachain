package org.nachain.core.dapp.internal.supernode.vote;


import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;

import java.math.BigInteger;


public class SuperNodeVote {


    private long voteInstance;


    private String voteTx;


    private long blockHeight;


    private long unlockHeight;


    private String voteAddress;


    private String beneficiaryAddress;


    private String nominateAddress;


    private BigInteger amount;


    private String hash;


    private String cancelTx;


    private String withdrawTx;

    public long getVoteInstance() {
        return voteInstance;
    }

    public SuperNodeVote setVoteInstance(long voteInstance) {
        this.voteInstance = voteInstance;
        return this;
    }

    public String getVoteAddress() {
        return voteAddress;
    }

    public SuperNodeVote setVoteAddress(String voteAddress) {
        this.voteAddress = voteAddress;
        return this;
    }

    public String getVoteTx() {
        return voteTx;
    }

    public SuperNodeVote setVoteTx(String voteTx) {
        this.voteTx = voteTx;
        return this;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public SuperNodeVote setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public String getBeneficiaryAddress() {
        return beneficiaryAddress;
    }

    public SuperNodeVote setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
        return this;
    }

    public String getNominateAddress() {
        return nominateAddress;
    }

    public SuperNodeVote setNominateAddress(String nominateAddress) {
        this.nominateAddress = nominateAddress;
        return this;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public SuperNodeVote setAmount(BigInteger amount) {
        this.amount = amount;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public SuperNodeVote setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public long getUnlockHeight() {
        return unlockHeight;
    }

    public SuperNodeVote setUnlockHeight(long unlockHeight) {
        this.unlockHeight = unlockHeight;
        return this;
    }

    public String getCancelTx() {
        return cancelTx;
    }

    public SuperNodeVote setCancelTx(String cancelTx) {
        this.cancelTx = cancelTx;
        return this;
    }

    public String getWithdrawTx() {
        return withdrawTx;
    }

    public SuperNodeVote setWithdrawTx(String withdrawTx) {
        this.withdrawTx = withdrawTx;
        return this;
    }

    @Override
    public String toString() {
        return "SuperNodeVote{" +
                "voteInstance=" + voteInstance +
                ", voteTx='" + voteTx + '\'' +
                ", blockHeight=" + blockHeight +
                ", unlockHeight=" + unlockHeight +
                ", voteAddress='" + voteAddress + '\'' +
                ", beneficiaryAddress='" + beneficiaryAddress + '\'' +
                ", nominateAddress='" + nominateAddress + '\'' +
                ", amount=" + amount +
                ", hash='" + hash + '\'' +
                ", cancelTx='" + cancelTx + '\'' +
                ", withdrawTx='" + withdrawTx + '\'' +
                '}';
    }


    public String toHashString() {
        return "SuperNodeVote{" +
                "voteInstance=" + voteInstance +
                ", voteTx='" + voteTx + '\'' +
                ", blockHeight=" + blockHeight +
                ", unlockHeight=" + unlockHeight +
                ", voteAddress='" + voteAddress + '\'' +
                ", beneficiaryAddress='" + beneficiaryAddress + '\'' +
                ", nominateAddress='" + nominateAddress + '\'' +
                ", amount=" + amount +
                '}';
    }


    public byte[] encodeHash() {

        return Hash.h256(this.toHashString().getBytes());
    }


    public String encodeHashString() {
        return Hex.encode0x(encodeHash());
    }
}
