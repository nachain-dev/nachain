package org.nachain.core.dapp.internal.bridge;


public class Binding {


    String address;


    ChainName chainName;


    String mappingAddress;

    public String getAddress() {
        return address;
    }

    public Binding setAddress(String address) {
        this.address = address;
        return this;
    }

    public ChainName getChainName() {
        return chainName;
    }

    public Binding setChainName(ChainName chainName) {
        this.chainName = chainName;
        return this;
    }

    public String getMappingAddress() {
        return mappingAddress;
    }

    public Binding setMappingAddress(String mappingAddress) {
        this.mappingAddress = mappingAddress;
        return this;
    }
}
