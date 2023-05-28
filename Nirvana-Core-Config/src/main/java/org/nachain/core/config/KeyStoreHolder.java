package org.nachain.core.config;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.config.timezone.TimeZoneService;
import org.nachain.core.crypto.Key;
import org.nachain.core.wallet.WalletUtils;
import org.nachain.core.wallet.keystore.Keystore;
import org.nachain.core.wallet.keystore.KeystoreService;
import org.nachain.core.wallet.keystore.WalletKeystore;

import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@Slf4j
public class KeyStoreHolder {


    private static Map<String, Key> keyMap;

    private static WalletKeystore walletKeystore;

    private static File walletKeystoreFile;

    static {

        TimeZoneService.setDefault();


        keyMap = Maps.newConcurrentMap();


        walletKeystoreFile = new File(ChainConfig.WALLET_KEYSTORE_PATH);


        try {

            walletKeystore = KeystoreService.readWallet(walletKeystoreFile, ChainConfig.WALLET_PASSWORD);


            for (Keystore keystore : walletKeystore) {
                byte[] privateKey = keystore.getPrivateKey();

                addKeyMap(privateKey);
            }
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("new Key(PRIVATE_KEY)", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.debug("KeyStoreConfig initialization complete.");
    }


    public static Key getKey(String walletAddress) {
        return keyMap.get(walletAddress);
    }


    private static void addKeyMap(byte[] privateKey) throws InvalidKeySpecException {

        Key key = new Key(privateKey);

        String walletAddress = WalletUtils.generateWalletAddress(key.getPublicKey());

        keyMap.put(walletAddress, key);
    }


    public static void add(Keystore keystore) throws InvalidKeySpecException {

        walletKeystore.add(keystore);


        addKeyMap(keystore.getPrivateKey());


        try {
            KeystoreService.saveWallet(walletKeystore, walletKeystoreFile, ChainConfig.WALLET_PASSWORD);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
