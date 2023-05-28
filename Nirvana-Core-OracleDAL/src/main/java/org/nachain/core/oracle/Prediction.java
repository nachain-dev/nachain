package org.nachain.core.oracle;

import org.nachain.core.chain.sign.IMinedSignObject;
import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;


public class Prediction implements IPrediction, IMinedSignObject {


    private long instance;


    private long token;


    private int eventType;


    private String eventData;


    private String superNode;


    private String hash;


    private String minedSign;

    public long getInstance() {
        return instance;
    }

    public Prediction setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getToken() {
        return token;
    }

    public Prediction setToken(long token) {
        this.token = token;
        return this;
    }

    public int getEventType() {
        return eventType;
    }

    public Prediction setEventType(int eventType) {
        this.eventType = eventType;
        return this;
    }

    public String getEventData() {
        return eventData;
    }

    public Prediction setEventData(String eventData) {
        this.eventData = eventData;
        return this;
    }

    public String getSuperNode() {
        return superNode;
    }

    public Prediction setSuperNode(String superNode) {
        this.superNode = superNode;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public Prediction setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getMinedSign() {
        return minedSign;
    }

    public Prediction setMinedSign(String minedSign) {
        this.minedSign = minedSign;
        return this;
    }

    @Override
    public String toMinedSignString() throws Exception {
        return "Prediction{" +
                "instance=" + instance +
                ", token=" + token +
                ", eventType=" + eventType +
                ", eventData='" + eventData + '\'' +
                ", superNode='" + superNode + '\'' +
                ", hash=" + hash +
                '}';
    }

    @Override
    public String toString() {
        return "Prediction{" +
                "instance=" + instance +
                ", token=" + token +
                ", eventType=" + eventType +
                ", eventData='" + eventData + '\'' +
                ", superNode='" + superNode + '\'' +
                ", hash=" + hash +
                ", minedSign='" + minedSign + '\'' +
                '}';
    }

    public String toHashString() {
        return "Prediction{" +
                "instance=" + instance +
                ", token=" + token +
                ", eventType=" + eventType +
                ", eventData='" + eventData + '\'' +
                ", superNode='" + superNode + '\'' +
                '}';
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
