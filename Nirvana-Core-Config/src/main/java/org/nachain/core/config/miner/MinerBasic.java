package org.nachain.core.config.miner;

import org.nachain.core.crypto.Key;


public class MinerBasic {


    private String walletAddress;


    private String minerAddress;


    private Key minerKey;

    public String getWalletAddress() {
        return walletAddress;
    }

    public MinerBasic setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
        return this;
    }

    public String getMinerAddress() {
        return minerAddress;
    }

    public MinerBasic setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress;
        return this;
    }

    public Key getMinerKey() {
        return minerKey;
    }

    public MinerBasic setMinerKey(Key minerKey) {
        this.minerKey = minerKey;
        return this;
    }
}
