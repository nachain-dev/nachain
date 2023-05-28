package org.nachain.core.chain.structure.instance.logicchain;

import java.math.BigInteger;


public class DPoSMinedCycle implements IDPoSMined {


    private String minerAddress;


    private BigInteger tokenValue;


    private long schedule;


    private long timestamp;

    public String getMinerAddress() {
        return minerAddress;
    }

    public DPoSMinedCycle setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress;
        return this;
    }

    public BigInteger getTokenValue() {
        return tokenValue;
    }

    public DPoSMinedCycle setTokenValue(BigInteger tokenValue) {
        this.tokenValue = tokenValue;
        return this;
    }

    public long getSchedule() {
        return schedule;
    }

    public DPoSMinedCycle setSchedule(long schedule) {
        this.schedule = schedule;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public DPoSMinedCycle setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public String toString() {
        return "DPoSMinedCycle{" +
                "minerAddress='" + minerAddress + '\'' +
                ", tokenValue=" + tokenValue +
                ", schedule=" + schedule +
                ", timestamp=" + timestamp +
                '}';
    }


    @Override
    public String toKeyString() {
        return minerAddress + "_" + schedule;
    }


    public DPoSMinedCycle addTokenValue(BigInteger amount) {
        tokenValue = tokenValue.add(amount);
        return this;
    }
}
