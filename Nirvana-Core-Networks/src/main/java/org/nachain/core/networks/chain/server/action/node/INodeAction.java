package org.nachain.core.networks.chain.server.action.node;

public interface INodeAction {


    void joinGroup();


    void subscribeGroup();


    void subscribeAddress(String address);

}
