package org.nachain.core.wallet.keystore;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class KeystoreService {


    public final static int KeystoreVersion = 0x01;


    public static Keystore readKey(File file) throws IOException {
        Keystore keystore = null;

        ObjectMapper objectMapper = new ObjectMapper();

        keystore = objectMapper.readValue(file, Keystore.class);

        return keystore;
    }


    public static void saveKey(Keystore keystore, File file) throws IOException {

        file.getParentFile().mkdirs();

        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream outputStream = new FileOutputStream(file);
        objectMapper.writeValue(outputStream, keystore);
    }


    public static WalletKeystore readWallet(File file, String walletPassword) throws IOException {
        WalletKeystore walletKeystore;

        ObjectMapper objectMapper = new ObjectMapper();

        walletKeystore = objectMapper.readValue(file, WalletKeystore.class);


        for (Keystore keystore : walletKeystore) {
            keystore.decrypt(walletPassword);
            keystore.updatePublicKey();
            keystore.updateAddress();
        }

        return walletKeystore;
    }


    public static void saveWallet(WalletKeystore walletKeystore, File file, String walletPassword) throws IOException {

        file.getParentFile().mkdirs();


        for (Keystore keystore : walletKeystore) {
            keystore.encrypt(walletPassword);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream outputStream = new FileOutputStream(file);
        objectMapper.writeValue(outputStream, walletKeystore);
    }


}
