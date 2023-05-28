package org.nachain.core.intermediate;

import java.util.HashSet;


public class InstanceUsedToken {

    private long instance;


    private HashSet<Long> tokenIds;

    public long getInstance() {
        return instance;
    }

    public InstanceUsedToken setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public HashSet<Long> getTokenIds() {
        return tokenIds;
    }

    public InstanceUsedToken setTokenIds(HashSet<Long> tokenIds) {
        this.tokenIds = tokenIds;
        return this;
    }

    @Override
    public String toString() {
        return "InstanceUsedToken{" +
                "instance=" + instance +
                ", tokenIds=" + tokenIds +
                '}';
    }


    public boolean addTokenId(long tokenId) {
        if (!tokenIds.contains(tokenId)) {
            this.tokenIds.add(tokenId);
            return true;
        }

        return false;
    }
}
