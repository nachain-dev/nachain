package org.nachain.core.chain.transaction.unused;

public interface IUnusedPool {


    void addUnusedTx(String txHash);


    void removeUnusedTx(String txHash);


    String toString();
}
