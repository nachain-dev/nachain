package org.nachain.core.config.miner;

import org.nachain.core.crypto.Key;

public class Mining {


    private static ThreadLocal<MinerBasic> miner = new ThreadLocal();


    private static ThreadLocal<Long> miningInstance = new ThreadLocal();


    private static ThreadLocal<Long> miningBlockHeight = new ThreadLocal();


    public static MinerBasic getMiner() {
        MinerBasic minerBasic = miner.get();
        if (minerBasic == null) {
            throw new RuntimeException("Miner not found.");
        }
        return minerBasic;
    }


    public static Key getKey() {
        MinerBasic minerBasic = getMiner();
        Key minerKey = minerBasic.getMinerKey();
        if (minerKey == null) {
            throw new RuntimeException("MinerKey not found.");
        }
        return minerKey;
    }


    public static long getInstance() {
        return miningInstance.get();
    }


    public static long getBlockHeight() {
        return miningBlockHeight.get();
    }


    public static void set(String walletAddress, String minerAddress, Key minerKey) {
        miner.set(new MinerBasic().setWalletAddress(walletAddress).setMinerAddress(minerAddress).setMinerKey(minerKey));
    }


    public static void set(long instance, long blockHeight) {
        miningInstance.set(instance);
        miningBlockHeight.set(blockHeight);
    }


    public static void clear() {
        miner.remove();
        miningInstance.remove();
        miningBlockHeight.remove();
    }
}
