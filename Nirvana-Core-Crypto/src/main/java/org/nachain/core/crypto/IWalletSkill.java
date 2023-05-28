package org.nachain.core.crypto;

public interface IWalletSkill {

    String generateWalletAddress(byte[] publicKey);

    String generateDAppAddress(byte[] publicKey);

    String generateMinerAddress(byte[] publicKey);

}
