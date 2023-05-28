package org.nachain.core.wallet.keystore;


public interface IKeystore {


    String toSeedHexString();


    String toPrivateKeyHexString();


    String toPublicKeyHexString();
}
