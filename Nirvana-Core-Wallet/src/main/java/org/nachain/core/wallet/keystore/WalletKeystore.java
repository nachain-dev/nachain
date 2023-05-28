package org.nachain.core.wallet.keystore;

import java.util.ArrayList;


public class WalletKeystore extends ArrayList<Keystore> {

    public void print() {
        System.out.println();
        for (Keystore keystore : this) {
            System.out.println("[index=" + keystore.getIndex() + "]" + keystore + "\r\n");
        }
    }


    public Keystore getKeystore(String walletAddress) {
        for (Keystore keystore : this) {
            if (keystore.getWalletAddress().equals(walletAddress)) {
                return keystore;
            }
        }
        return null;
    }

}
