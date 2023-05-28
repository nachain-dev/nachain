package org.nachain.core.wallet;

import io.github.novacrypto.base58.Base58;
import org.nachain.core.crypto.Key;
import org.nachain.core.crypto.bip32.CoinType;
import org.nachain.core.crypto.bip32.HdKeyPair;
import org.nachain.core.crypto.bip32.key.KeyVersion;
import org.nachain.core.crypto.bip39.Language;
import org.nachain.core.crypto.bip39.MnemonicGenerator;
import org.nachain.core.crypto.bip44.Bip44;
import org.nachain.core.util.ByteUtils;
import org.nachain.core.util.RipeMDUtils;
import org.nachain.core.util.SHAUtils;
import org.nachain.core.wallet.keystore.Keystore;

import java.security.spec.InvalidKeySpecException;

public class WalletUtils {


    private final static MnemonicGenerator mnemonicGenerator = new MnemonicGenerator();


    private final static byte PREFIX = 0x35;


    private final static byte DAPP_PREFIX = 0x17;


    private final static byte MINER_PREFIX = 0x32;


    public static String generateWords(Language language) {
        return mnemonicGenerator.getWordlist(128, language);
    }


    public static String generateWalletAddress(final byte[] publicKey) {
        return generateAddress(PREFIX, publicKey);
    }


    public static String generateDAppAddress(final byte[] hashData) {
        return generateAddress(DAPP_PREFIX, hashData);
    }


    public static String generateMinerAddress(final byte[] publicKey) {
        return generateAddress(MINER_PREFIX, publicKey);
    }


    private static String generateAddress(final byte PREFIX, byte[] publicKeyBytes) {

        String address = "";

        try {

            byte[] addressBytes = SHAUtils.encodeSHA256(publicKeyBytes);


            addressBytes = RipeMDUtils.encodeRipeMD160(addressBytes);


            byte[] checkCodeWABytes = ByteUtils.merge(new byte[]{PREFIX}, addressBytes);
            checkCodeWABytes = SHAUtils.encodeSHA256(checkCodeWABytes);
            checkCodeWABytes = SHAUtils.encodeSHA256(checkCodeWABytes);

            checkCodeWABytes = new byte[]{checkCodeWABytes[0], checkCodeWABytes[1], checkCodeWABytes[2], checkCodeWABytes[3]};


            addressBytes = ByteUtils.merge(new byte[]{PREFIX}, addressBytes);
            addressBytes = ByteUtils.merge(addressBytes, checkCodeWABytes);


            address = Base58.base58Encode(addressBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return address;
    }


    public static Keystore generate(Language language, String mnemonicWords, String password, int index) {
        Keystore keystore;


        byte[] seed = mnemonicGenerator.getSeedFromWordlist(mnemonicWords, password, language);
        Bip44 BIP_44 = new Bip44();
        HdKeyPair rootAddress = BIP_44.getRootKeyPairFromSeed(seed, KeyVersion.MAINNET, CoinType.NIRVANA_SLIP10);
        HdKeyPair childKey = BIP_44.getChildKeyPair(rootAddress, index);
        Key key = Key.fromRawPrivateKey(childKey.getPrivateKey().getKeyData());


        byte[] privateKeyBytes = key.getPrivateKey();
        byte[] publicKeyBytes = key.getPublicKey();


        keystore = new Keystore(language, mnemonicWords, seed, index, privateKeyBytes, publicKeyBytes);

        return keystore;
    }


    public static Keystore generate(Language language, String mnemonicWords, String password) {
        return generate(language, mnemonicWords, password, 0);
    }


    public static Keystore generate(Language language, String mnemonicWords) {
        return generate(language, mnemonicWords, "", 0);
    }


    public static Keystore generate(byte[] privateKey) throws InvalidKeySpecException {

        Key key = new Key(privateKey);
        byte[] publicKeyBytes = key.getPublicKey();


        Keystore keystore = new Keystore(null, "", null, 0, privateKey, publicKeyBytes);

        return keystore;
    }

}
