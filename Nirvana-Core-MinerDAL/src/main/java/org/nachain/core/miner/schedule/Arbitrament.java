package org.nachain.core.miner.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.crypto.Hash;
import org.nachain.core.crypto.Key;
import org.nachain.core.signverify.SignVerify;
import org.nachain.core.util.CommUtils;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;


public class Arbitrament implements IArbitrament {


    private long instance;


    private long height;


    private long timestamp;


    private String backupMiner;


    private String arbitratorAddress;


    private String sign;

    public long getInstance() {
        return instance;
    }

    public Arbitrament setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getHeight() {
        return height;
    }

    public Arbitrament setHeight(long height) {
        this.height = height;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Arbitrament setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getBackupMiner() {
        return backupMiner;
    }

    public Arbitrament setBackupMiner(String backupMiner) {
        this.backupMiner = backupMiner;
        return this;
    }

    public String getArbitratorAddress() {
        return arbitratorAddress;
    }

    public Arbitrament setArbitratorAddress(String arbitratorAddress) {
        this.arbitratorAddress = arbitratorAddress;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public Arbitrament setSign(String sign) {
        this.sign = sign;
        return this;
    }


    public static Arbitrament toArbitrament(String json) {
        return JsonUtils.jsonToPojo(json, Arbitrament.class);
    }


    public static Arbitrament newArbitrament(long instance, long height, String backupMiner, String arbitratorAddress, Key minerKey) throws Exception {
        Arbitrament arbitrament = new Arbitrament();
        arbitrament.setInstance(instance);
        arbitrament.setHeight(height);
        arbitrament.setTimestamp(CommUtils.currentTimeMillis());
        arbitrament.setBackupMiner(backupMiner);
        arbitrament.setArbitratorAddress(arbitratorAddress);

        arbitrament.setSign(Hex.encode0x(SignVerify.sign(arbitrament.toSignString(), minerKey)));

        return arbitrament;
    }

    @Override
    public String toString() {
        return "Arbitrament{" +
                "instance=" + instance +
                ", height=" + height +
                ", timestamp=" + timestamp +
                ", backupMiner='" + backupMiner + '\'' +
                ", arbitratorAddress='" + arbitratorAddress + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    @Override
    public String toHashString() throws Exception {
        return "Arbitrament{" +
                "instance=" + instance +
                ", height=" + height +
                ", timestamp=" + timestamp +
                ", backupMiner='" + backupMiner + '\'' +
                ", arbitratorAddress='" + arbitratorAddress + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    @Override
    public String toSignString() throws Exception {
        return "Arbitrament{" +
                "instance=" + instance +
                ", height=" + height +
                ", timestamp=" + timestamp +
                ", backupMiner='" + backupMiner + '\'' +
                ", arbitratorAddress='" + arbitratorAddress + '\'' +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }


    @Override
    public byte[] encodeHash() throws Exception {

        return Hash.h256(this.toHashString().getBytes());
    }


    @Override
    public String encodeHashString() throws Exception {
        return Hex.encode0x(encodeHash());
    }

}
