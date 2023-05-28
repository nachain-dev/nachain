package org.nachain.core.wallet.walletskill;

import org.nachain.core.crypto.IWalletSkill;

public class EthereumWalletSkill implements IWalletSkill {
    @Override
    public String generateWalletAddress(byte[] publicKey) {
        return null;
    }

    @Override
    public String generateDAppAddress(byte[] publicKey) {
        return null;
    }

    @Override
    public String generateMinerAddress(byte[] publicKey) {
        return null;
    }
}
