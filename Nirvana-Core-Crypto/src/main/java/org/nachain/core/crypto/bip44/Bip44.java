package org.nachain.core.crypto.bip44;

import org.nachain.core.crypto.bip32.CoinType;
import org.nachain.core.crypto.bip32.HdKeyGenerator;
import org.nachain.core.crypto.bip32.HdKeyPair;
import org.nachain.core.crypto.bip32.key.KeyVersion;


public class Bip44 {

    private static final int PURPOSE = 44;

    private static final long ACCOUNT = 0;

    private static final int CHANGE = 0;
    private final HdKeyGenerator hdKeyGenerator = new HdKeyGenerator();


    public HdKeyPair getRootKeyPairFromSeed(byte[] seed, KeyVersion keyVersion, CoinType coinType) {
        HdKeyPair masterKey = hdKeyGenerator.getMasterKeyPairFromSeed(seed, keyVersion, coinType);
        HdKeyPair purposeKey = hdKeyGenerator.getChildKeyPair(masterKey, PURPOSE, true);
        HdKeyPair coinTypeKey = hdKeyGenerator.getChildKeyPair(purposeKey, coinType.getCoinType(), true);
        HdKeyPair accountKey = hdKeyGenerator.getChildKeyPair(coinTypeKey, ACCOUNT, true);
        HdKeyPair changeKey = hdKeyGenerator.getChildKeyPair(accountKey, CHANGE, coinType.getAlwaysHardened());

        return changeKey;
    }

    public HdKeyPair getChildKeyPair(HdKeyPair parent, int index) {
        return hdKeyGenerator.getChildKeyPair(parent, index, parent.getCoinType().getAlwaysHardened());
    }
}
