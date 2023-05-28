package org.nachain.core.chain.transaction.unverifiedunused;

import org.nachain.core.util.CommUtils;

import java.util.List;


public class UnverifiedUnusedPool implements IUnverifiedUnusedPool {


    private String walletAddress;

    private List<String> unusedTxs;

    public String getWalletAddress() {
        return walletAddress;
    }

    public UnverifiedUnusedPool setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
        return this;
    }

    public List<String> getUnusedTxs() {
        return unusedTxs;
    }

    public UnverifiedUnusedPool setUnusedTxs(List<String> unusedTxs) {
        this.unusedTxs = unusedTxs;
        return this;
    }


    @Override
    public void addUnusedTx(String txHash) {
        unusedTxs.add(txHash);
    }

    public void removeUnusedTx(String txHash) {
        if (unusedTxs != null) {
            unusedTxs = CommUtils.remove(unusedTxs, txHash);
        }
    }

    @Override
    public String toString() {
        return "UnverifiedUnusedPool{" +
                "walletAddress='" + walletAddress + '\'' +
                ", unusedTxs=" + unusedTxs +
                '}';
    }
}
