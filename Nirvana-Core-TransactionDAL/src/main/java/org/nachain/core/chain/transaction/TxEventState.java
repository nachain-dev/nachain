package org.nachain.core.chain.transaction;

import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


public class TxEventState {


    private int stateType;


    private String tx;


    private String address;


    private BigInteger before;


    private BigInteger after;


    private BigInteger stateDifference;


    private String hash;

    public int getStateType() {
        return stateType;
    }

    public TxEventState setStateType(int stateType) {
        this.stateType = stateType;
        return this;
    }

    public String getTx() {
        return tx;
    }

    public TxEventState setTx(String tx) {
        this.tx = tx;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public TxEventState setAddress(String address) {
        this.address = address;
        return this;
    }

    public BigInteger getBefore() {
        return before;
    }

    public TxEventState setBefore(BigInteger before) {
        this.before = before;
        return this;
    }

    public BigInteger getAfter() {
        return after;
    }

    public TxEventState setAfter(BigInteger after) {
        this.after = after;
        return this;
    }

    public BigInteger getStateDifference() {
        return stateDifference;
    }

    public TxEventState setStateDifference(BigInteger stateDifference) {
        this.stateDifference = stateDifference;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public TxEventState setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String toHashString() {
        return "TxEventState{" +
                "stateType=" + stateType +
                ", tx='" + tx + '\'' +
                ", address='" + address + '\'' +
                ", before=" + before +
                ", after=" + after +
                ", stateDifference=" + stateDifference +
                '}';
    }

    @Override
    public String toString() {
        return "TxEventState{" +
                "stateType=" + stateType +
                ", tx='" + tx + '\'' +
                ", address='" + address + '\'' +
                ", before=" + before +
                ", after=" + after +
                ", stateDifference=" + stateDifference +
                ", hash='" + hash + '\'' +
                '}';
    }


    public static TxEventState to(String json) {
        return JsonUtils.jsonToPojo(json, TxEventState.class);
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }


    public byte[] encodeHash() {

        return Hash.h256(this.toHashString().getBytes());
    }


    public String encodeHashString() {
        return Hex.encode0x(encodeHash());
    }
}
