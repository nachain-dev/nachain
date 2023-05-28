package org.nachain.core.token.nft.dto;

import java.util.List;


public class NftTransferDTO {


    long instance;


    long token;


    List<Long> tokenIds;

    public long getInstance() {
        return instance;
    }

    public NftTransferDTO setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getToken() {
        return token;
    }

    public NftTransferDTO setToken(long token) {
        this.token = token;
        return this;
    }

    public List<Long> getTokenIds() {
        return tokenIds;
    }

    public NftTransferDTO setTokenIds(List<Long> tokenIds) {
        this.tokenIds = tokenIds;
        return this;
    }

    @Override
    public String toString() {
        return "NftTransferDTO{" +
                "instance=" + instance +
                ", token=" + token +
                ", tokenIds=" + tokenIds +
                '}';
    }
}
