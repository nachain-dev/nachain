package org.nachain.core.crypto.bip32;

public enum CoinType {

    BITCOIN(Scheme.BIP32, CoinTypes.BTC, false),

    NIRVANA_SLIP10(Scheme.SLIP10_ED25519, CoinTypes.NAC, true),

    NIRVANA_BIP32_ED25519(Scheme.BIP32_ED25519, CoinTypes.NAC, false);

    private final Scheme scheme;
    private final long coinType;
    private final boolean alwaysHardened;

    CoinType(Scheme scheme, long coinType, boolean alwaysHardened) {
        this.scheme = scheme;
        this.coinType = coinType;
        this.alwaysHardened = alwaysHardened;
    }


    public Scheme getScheme() {
        return scheme;
    }


    public long getCoinType() {
        return coinType;
    }


    public boolean getAlwaysHardened() {
        return alwaysHardened;
    }
}
