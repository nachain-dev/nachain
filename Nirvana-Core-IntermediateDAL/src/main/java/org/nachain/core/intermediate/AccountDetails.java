package org.nachain.core.intermediate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AccountDetails {


    String address;


    Map<Long, InstanceUsedToken> usedInstances;

    public String getAddress() {
        return address;
    }

    public AccountDetails setAddress(String address) {
        this.address = address;
        return this;
    }

    public Map<Long, InstanceUsedToken> getUsedInstances() {
        return usedInstances;
    }

    public AccountDetails setUsedInstances(Map<Long, InstanceUsedToken> usedInstances) {
        this.usedInstances = usedInstances;
        return this;
    }


    public InstanceUsedToken getInstanceDetail(long instance) {
        return usedInstances.get(instance);
    }


    public AccountDetails addUsedInstanceDetail(InstanceUsedToken instanceUsedToken) {
        if (usedInstances == null) {
            usedInstances = new ConcurrentHashMap();
        }
        usedInstances.put(instanceUsedToken.getInstance(), instanceUsedToken);

        return this;
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
                "address='" + address + '\'' +
                ", usedInstances=" + usedInstances +
                '}';
    }


}

