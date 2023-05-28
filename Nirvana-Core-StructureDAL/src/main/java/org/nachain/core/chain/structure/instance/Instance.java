package org.nachain.core.chain.structure.instance;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;

import java.util.List;


public class Instance {


    private long id;


    private String tx;


    private String appName;


    private String appVersion;


    private String appAddress;


    private String symbol;


    private String info;


    private String author;


    private InstanceType instanceType;


    private String minedTokenHash;


    private List<String> relatedTokensHash;


    private String data;


    private String hash;

    public long getId() {
        return id;
    }

    public Instance setId(long id) {
        this.id = id;
        return this;
    }

    public String getTx() {
        return tx;
    }

    public Instance setTx(String tx) {
        this.tx = tx;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public Instance setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public Instance setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getAppAddress() {
        return appAddress;
    }

    public Instance setAppAddress(String appAddress) {
        this.appAddress = appAddress;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public Instance setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public Instance setInfo(String info) {
        this.info = info;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Instance setAuthor(String author) {
        this.author = author;
        return this;
    }

    public InstanceType getInstanceType() {
        return instanceType;
    }

    public Instance setInstanceType(InstanceType instanceType) {
        this.instanceType = instanceType;
        return this;
    }

    public String getMinedTokenHash() {
        return minedTokenHash;
    }

    public Instance setMinedTokenHash(String minedTokenHash) {
        this.minedTokenHash = minedTokenHash;
        return this;
    }

    public List<String> getRelatedTokensHash() {
        return relatedTokensHash;
    }

    public Instance setRelatedTokensHash(List<String> relatedTokensHash) {
        this.relatedTokensHash = relatedTokensHash;
        return this;
    }

    public String getData() {
        return data;
    }

    public Instance setData(String data) {
        this.data = data;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public Instance setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String toHashString() {
        return "Instance{" +
                "tx='" + tx + '\'' +
                ", appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", appAddress='" + appAddress + '\'' +
                ", symbol='" + symbol + '\'' +
                ", info='" + info + '\'' +
                ", author='" + author + '\'' +
                ", instanceType=" + instanceType +
                ", minedTokenHash='" + minedTokenHash + '\'' +
                ", relatedTokensHash='" + relatedTokensHash + '\'' +
                ", data='" + data + '\'' +
                '}';
    }


    @Override
    public String toString() {
        return "Instance{" +
                "id=" + id +
                ", tx='" + tx + '\'' +
                ", appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", appAddress='" + appAddress + '\'' +
                ", symbol='" + symbol + '\'' +
                ", info='" + info + '\'' +
                ", author='" + author + '\'' +
                ", instanceType=" + instanceType +
                ", minedTokenHash='" + minedTokenHash + '\'' +
                ", relatedTokensHash=" + relatedTokensHash +
                ", data='" + data + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }


    public byte[] encodeHash() throws Exception {

        return Hash.h256(this.toHashString().getBytes());
    }


    public String encodeHashString() throws Exception {
        return Hex.encode0x(encodeHash());
    }


}
