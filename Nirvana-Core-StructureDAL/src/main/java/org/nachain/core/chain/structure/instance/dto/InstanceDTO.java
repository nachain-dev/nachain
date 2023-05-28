package org.nachain.core.chain.structure.instance.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.util.JsonUtils;

import java.util.List;


public class InstanceDTO {


    private String appName;


    private String appVersion;


    private String symbol;


    private String info;


    private InstanceType instanceType;


    private String minedTokenHash;


    private List<String> relatedTokensHash;


    private String data;

    public String getAppName() {
        return appName;
    }

    public InstanceDTO setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public InstanceDTO setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public InstanceDTO setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public InstanceDTO setInfo(String info) {
        this.info = info;
        return this;
    }

    public InstanceType getInstanceType() {
        return instanceType;
    }

    public InstanceDTO setInstanceType(InstanceType instanceType) {
        this.instanceType = instanceType;
        return this;
    }

    public String getMinedTokenHash() {
        return minedTokenHash;
    }

    public InstanceDTO setMinedTokenHash(String minedTokenHash) {
        this.minedTokenHash = minedTokenHash;
        return this;
    }

    public List<String> getRelatedTokensHash() {
        return relatedTokensHash;
    }

    public InstanceDTO setRelatedTokensHash(List<String> relatedTokensHash) {
        this.relatedTokensHash = relatedTokensHash;
        return this;
    }

    public String getData() {
        return data;
    }

    public InstanceDTO setData(String data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "InstanceDTO{" +
                "appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", symbol='" + symbol + '\'' +
                ", info='" + info + '\'' +
                ", instanceType=" + instanceType +
                ", minedTokenHash='" + minedTokenHash + '\'' +
                ", relatedTokensHash=" + relatedTokensHash +
                ", data='" + data + '\'' +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }


}
