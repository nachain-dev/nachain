package org.nachain.core.intermediate;

import java.util.HashSet;


public class AccountNftColl {


    private String address;


    private HashSet<Long> collTokenIds;

    public String getAddress() {
        return address;
    }

    public AccountNftColl setAddress(String address) {
        this.address = address;
        return this;
    }

    public HashSet<Long> getCollTokenIds() {
        return collTokenIds;
    }

    public AccountNftColl setCollTokenIds(HashSet<Long> collTokenIds) {
        this.collTokenIds = collTokenIds;
        return this;
    }

    @Override
    public String toString() {
        return "AccountNftColl{" +
                "address='" + address + '\'' +
                ", collTokenIds=" + collTokenIds +
                '}';
    }


    public boolean addCollTokenId(long collTokenId) {
        if (!collTokenIds.contains(collTokenId)) {
            this.collTokenIds.add(collTokenId);
            return true;
        }

        return false;
    }
}
