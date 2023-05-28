package org.nachain.core.chain.config;

public class Config {

    private String key;

    private String value;

    public Config() {
    }

    public Config(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Config setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Config setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "Config{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
