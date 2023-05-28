package org.nachain.core.crypto.bip32;

public enum Scheme {

    BIP32("Bitcoin seed"),


    SLIP10_ED25519("ed25519 seed"),


    BIP32_ED25519("ed25519 seed");

    private final String seed;

    Scheme(String seed) {
        this.seed = seed;
    }

    public String getSeed() {
        return seed;
    }
}
