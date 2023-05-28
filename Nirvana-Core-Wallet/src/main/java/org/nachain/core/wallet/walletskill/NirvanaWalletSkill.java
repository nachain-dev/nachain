package org.nachain.core.wallet.walletskill;

import org.nachain.core.crypto.IWalletSkill;
import org.nachain.core.wallet.WalletUtils;

public class NirvanaWalletSkill implements IWalletSkill {
    @Override
    public String generateWalletAddress(byte[] publicKey) {
        return WalletUtils.generateWalletAddress(publicKey);
    }

    @Override
    public String generateDAppAddress(byte[] publicKey) {
        return WalletUtils.generateDAppAddress(publicKey);
    }

    @Override
    public String generateMinerAddress(byte[] publicKey) {
        return WalletUtils.generateMinerAddress(publicKey);
    }
}
