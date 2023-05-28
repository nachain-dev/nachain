package org.nachain.core.crypto.bip32.key;

import org.nachain.core.util.Hex;


public enum KeyVersion {

    MAINNET("0x0488ADE4", "0x0488B21E"),
    TESTNET("0x04358394", "0x043587CF");

    private final byte[] privatePrefix;
    private final byte[] publicPrefix;

    KeyVersion(String privatePrefix, String publicPrefix) {
        this.privatePrefix = Hex.decode0x(privatePrefix);
        this.publicPrefix = Hex.decode0x(publicPrefix);
    }

    public byte[] getPrivateKeyVersion() {
        return privatePrefix;
    }

    public byte[] getPublicKeyVersion() {
        return publicPrefix;
    }
}
