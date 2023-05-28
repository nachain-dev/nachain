package org.nachain.core.intermediate;

import com.google.common.collect.Lists;

import java.util.List;


public class AccountDeploy {


    String address;


    List<Long> deployInstances;

    public String getAddress() {
        return address;
    }

    public AccountDeploy setAddress(String address) {
        this.address = address;
        return this;
    }

    public List<Long> getDeployInstances() {
        return deployInstances;
    }

    public AccountDeploy setDeployInstances(List<Long> deployInstances) {
        this.deployInstances = deployInstances;
        return this;
    }

    @Override
    public String toString() {
        return "AccountDeploy{" +
                "address='" + address + '\'' +
                ", deployInstances=" + deployInstances +
                '}';
    }


    public void addDeploy(long instance) {
        if (deployInstances == null) {
            deployInstances = Lists.newArrayList();
        }

        deployInstances.add(instance);
    }
}

