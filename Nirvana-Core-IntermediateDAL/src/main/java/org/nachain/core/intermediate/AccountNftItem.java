package org.nachain.core.intermediate;

import java.util.HashSet;


public class AccountNftItem {


    private String address;


    private long instance;


    private long collTokenId;


    private HashSet<Long> nftItemIds;

    public String getAddress() {
        return address;
    }

    public AccountNftItem setAddress(String address) {
        this.address = address;
        return this;
    }

    public long getInstance() {
        return instance;
    }

    public AccountNftItem setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getCollTokenId() {
        return collTokenId;
    }

    public AccountNftItem setCollTokenId(long collTokenId) {
        this.collTokenId = collTokenId;
        return this;
    }

    public HashSet<Long> getNftItemIds() {
        return nftItemIds;
    }

    public AccountNftItem setNftItemIds(HashSet<Long> nftItemIds) {
        this.nftItemIds = nftItemIds;
        return this;
    }

    @Override
    public String toString() {
        return "AccountNftItem{" +
                "address='" + address + '\'' +
                ", instance=" + instance +
                ", collTokenId=" + collTokenId +
                ", nftItemIds=" + nftItemIds +
                '}';
    }


    public boolean addNftItemId(long nftItemId) {
        if (!nftItemIds.contains(nftItemId)) {
            this.nftItemIds.add(nftItemId);
            return true;
        }

        return false;
    }


    public boolean delNftItemId(long nftItemId) {
        if (nftItemIds.contains(nftItemId)) {
            this.nftItemIds.remove(nftItemId);
            return true;
        }

        return false;
    }
}
