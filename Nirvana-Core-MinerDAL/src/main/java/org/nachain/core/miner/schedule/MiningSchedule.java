package org.nachain.core.miner.schedule;

import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;

import java.util.List;


public class MiningSchedule implements IMiningSchedule {


    private long instance;


    private long schedule;


    private long timestamp;


    private List<String> miners;


    private String hash;


    private String sign;

    public long getInstance() {
        return instance;
    }

    public MiningSchedule setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getSchedule() {
        return schedule;
    }

    public MiningSchedule setSchedule(long schedule) {
        this.schedule = schedule;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public MiningSchedule setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public List<String> getMiners() {
        return miners;
    }

    public MiningSchedule setMiners(List<String> miners) {
        this.miners = miners;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public MiningSchedule setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public MiningSchedule setSign(String sign) {
        this.sign = sign;
        return this;
    }

    @Override
    public String toString() {
        return "MiningSchedule{" +
                "instance=" + instance +
                ", schedule=" + schedule +
                ", timestamp=" + timestamp +
                ", miners=" + miners +
                ", hash='" + hash + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    @Override
    public String toHashString() {
        return "MiningSchedule{" +
                "instance=" + instance +
                ", schedule=" + schedule +
                ", timestamp=" + timestamp +
                ", miners=" + miners +
                '}';
    }

    @Override
    public String toSignString() {
        return "MiningSchedule{" +
                "instance=" + instance +
                ", schedule=" + schedule +
                ", timestamp=" + timestamp +
                ", miners=" + miners +
                ", hash='" + hash + '\'' +
                '}';
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }


    @Override
    public byte[] encodeHash() {

        return Hash.h256(this.toHashString().getBytes());
    }


    @Override
    public String encodeHashString() {
        return Hex.encode0x(encodeHash());
    }


    public String calcMinerAddress(long blockHeight) {

        int index = (int) (blockHeight % miners.size()) - 1;


        if (index == -1) {
            index = miners.size() - 1;
        }

        return miners.get(index);
    }


    public String calcDWorker() {

        int numLength = String.valueOf(miners.size()).length();

        int hashLength = hash.length();

        String numWold = hash.substring(hashLength - numLength);

        long hashToNumber = Long.parseLong(numWold, 16);


        return calcMinerAddress(hashToNumber);
    }

}
