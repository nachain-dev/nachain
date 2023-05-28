package org.nachain.core.chain.transaction.unverifiedunused;


public interface IUnverifiedUnusedPool {


    void addUnusedTx(String txHash);


    void removeUnusedTx(String txHash);


    String toString();
}
