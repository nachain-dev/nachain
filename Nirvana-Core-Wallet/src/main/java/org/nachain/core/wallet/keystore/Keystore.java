package org.nachain.core.wallet.keystore;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.crypto.Aes;
import org.nachain.core.crypto.Key;
import org.nachain.core.crypto.bip39.Language;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;
import org.nachain.core.wallet.WalletUtils;

import java.security.spec.InvalidKeySpecException;


public class Keystore implements IKeystore {


    private int version;

    private String language;

    private String mnemonic;

    private byte[] seed;

    private int index;

    private byte[] privateKey;

    private byte[] publicKey;

    private String walletAddress;

    private String appAddress;

    private String minerAddress;

    private boolean cipher;

    public Keystore() {
    }

    public Keystore(byte[] privateKey) {
        this.version = KeystoreService.KeystoreVersion;
        this.privateKey = privateKey;
        updatePublicKey();
        updateAddress();
    }

    public Keystore(String privateKey) {
        this.version = KeystoreService.KeystoreVersion;
        this.privateKey = Hex.decode0x(privateKey);
        updatePublicKey();
        updateAddress();
    }

    public Keystore(Key key) {
        this.version = KeystoreService.KeystoreVersion;
        this.privateKey = key.getPrivateKey();
        updatePublicKey();
        updateAddress();
    }

    public Keystore(Language language, String mnemonic, byte[] seed, int index, byte[] privateKey, byte[] publicKey) {
        this.version = KeystoreService.KeystoreVersion;
        this.language = language != null ? language.name() : null;
        this.mnemonic = mnemonic;
        this.seed = seed;
        this.index = index;
        this.privateKey = privateKey;
        this.publicKey = publicKey;

        updateAddress();
    }

    public int getVersion() {
        return version;
    }

    public Keystore setVersion(int version) {
        this.version = version;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public Keystore setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public Keystore setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
        return this;
    }

    public byte[] getSeed() {
        return seed;
    }

    public Keystore setSeed(byte[] seed) {
        this.seed = seed;
        return this;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public Keystore setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public Keystore setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public Keystore setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
        return this;
    }

    public String getAppAddress() {
        return appAddress;
    }

    public Keystore setAppAddress(String appAddress) {
        this.appAddress = appAddress;
        return this;
    }

    public String getMinerAddress() {
        return minerAddress;
    }

    public Keystore setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress;
        return this;
    }

    public boolean isCipher() {
        return cipher;
    }

    public Keystore setCipher(boolean cipher) {
        this.cipher = cipher;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public Keystore setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public String toString() {
        return "Keystore{" +
                "version=" + version +
                ", language='" + language + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                ", seed=" + toSeedHexString() +
                ", index=" + index +
                ", privateKey=" + toPrivateKeyHexString() +
                ", publicKey=" + toPublicKeyHexString() +
                ", walletAddress='" + walletAddress + '\'' +
                ", appAddress='" + appAddress + '\'' +
                ", minerAddress='" + minerAddress + '\'' +
                ", cipher='" + cipher + '\'' +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }

    @Override
    public String toSeedHexString() {
        if (seed != null) {
            return Hex.encode0x(seed);
        } else {
            return "";
        }
    }

    @Override
    public String toPrivateKeyHexString() {
        return Hex.encode0x(privateKey);
    }

    @Override
    public String toPublicKeyHexString() {
        return Hex.encode0x(publicKey);
    }


    public void updatePublicKey() {
        if (privateKey != null && publicKey == null) {

            try {
                setPublicKey(new Key(privateKey).getPublicKey());
            } catch (InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void updateAddress() {
        try {
            if (publicKey != null) {
                walletAddress = WalletUtils.generateWalletAddress(publicKey);
                appAddress = WalletUtils.generateDAppAddress(publicKey);
                minerAddress = WalletUtils.generateMinerAddress(publicKey);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Keystore generate(String password, int index) {
        return WalletUtils.generate(Language.valueOf(this.language), this.mnemonic, password, index);
    }


    public void encrypt(String walletPassword) {

        if (cipher) {
            return;
        }
        byte[] passwordBytes = walletPassword.getBytes();

        if (this.mnemonic != null) {
            this.mnemonic = Hex.encode(Aes.encrypt(this.mnemonic.getBytes(), passwordBytes));
        }

        if (this.seed != null) {
            this.seed = Aes.encrypt(this.seed, passwordBytes);
        }

        if (this.privateKey != null) {
            this.privateKey = Aes.encrypt(this.privateKey, passwordBytes);
        }

        if (this.publicKey != null) {
            this.publicKey = Aes.encrypt(this.publicKey, passwordBytes);
        }

        this.cipher = true;
    }


    public void decrypt(String walletPassword) {

        if (!cipher) {
            return;
        }
        byte[] passwordBytes = walletPassword.getBytes();

        if (this.mnemonic != null) {
            this.mnemonic = new String(Aes.decrypt(Hex.decode(this.mnemonic), passwordBytes));
        }

        if (this.seed != null) {
            this.seed = Aes.decrypt(this.seed, passwordBytes);
        }

        if (this.privateKey != null) {
            this.privateKey = Aes.decrypt(this.privateKey, passwordBytes);
        }

        if (this.publicKey != null) {
            this.publicKey = Aes.decrypt(this.publicKey, passwordBytes);
        }


        this.cipher = false;
    }

}
