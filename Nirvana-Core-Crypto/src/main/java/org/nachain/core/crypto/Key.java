package org.nachain.core.crypto;

import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.*;
import org.nachain.core.crypto.cache.PublicKeyCache;
import org.nachain.core.util.ByteUtils;
import org.nachain.core.util.Hex;
import org.nachain.core.util.SystemUtils;
import org.nachain.core.util.exception.CryptoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Collection;


public class Key {

    public static final int PUBLIC_KEY_LEN = 44;
    public static final int PRIVATE_KEY_LEN = 48;
    public static final int ADDRESS_LEN = 20;

    private static final Logger logger = LoggerFactory.getLogger(Key.class);

    private static final KeyPairGenerator gen = new KeyPairGenerator();
    private static final EdDSAParameterSpec ED25519SPEC = EdDSANamedCurveTable.getByName("ed25519");

    static {

        try {
            EdDSANamedCurveSpec params = EdDSANamedCurveTable.getByName("Ed25519");
            gen.initialize(params, new SecureRandom());
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("Failed to initialize Ed25519 engine", e);
            SystemUtils.exit(SystemUtils.Code.FAILED_TO_INIT_ED25519);
        }
    }

    protected EdDSAPrivateKey sk;
    protected EdDSAPublicKey pk;


    protected IWalletSkill walletSkill;


    public Key() {
        KeyPair keypair = gen.generateKeyPair();
        sk = (EdDSAPrivateKey) keypair.getPrivate();
        pk = (EdDSAPublicKey) keypair.getPublic();
    }


    public Key(byte[] privateKey) throws InvalidKeySpecException {
        this.sk = new EdDSAPrivateKey(new PKCS8EncodedKeySpec(privateKey));
        this.pk = new EdDSAPublicKey(new EdDSAPublicKeySpec(sk.getA(), sk.getParams()));
    }

    private Key(EdDSAPrivateKey sk, EdDSAPublicKey pk) {
        this.sk = sk;
        this.pk = pk;
    }


    public Key(byte[] privateKey, byte[] publicKey) throws InvalidKeySpecException {
        this(privateKey);

        if (!Arrays.equals(getPublicKey(), publicKey)) {
            throw new InvalidKeySpecException("Public key and private key do not match!");
        }
    }


    public static boolean verify(byte[] message, Signature signature) {
        if (message != null && signature != null) {
            try {
                if (Native.isEnabled()) {
                    return Native.verify(message, signature.getS(), signature.getA());
                } else {
                    EdDSAEngine engine = new EdDSAEngine();
                    engine.initVerify(PublicKeyCache.computeIfAbsent(signature.getPublicKey()));

                    return engine.verifyOneShot(message, signature.getS());
                }
            } catch (Exception e) {

            }
        }

        return false;
    }

    public static boolean isVerifyBatchSupported() {
        return Native.isEnabled();
    }

    public static boolean verifyBatch(Collection<byte[]> messages, Collection<Signature> signatures) {
        if (!isVerifyBatchSupported()) {
            throw new UnsupportedOperationException("Key#verifyBatch is only implemented in the native library.");
        }

        return Native.verifyBatch(
                messages.toArray(new byte[messages.size()][]),
                signatures.stream().map(Signature::getS).toArray(byte[][]::new),
                signatures.stream().map(Signature::getA).toArray(byte[][]::new));
    }


    public static boolean verify(byte[] message, byte[] signature) {
        Signature sig = Signature.fromBytes(signature);

        return verify(message, sig);
    }

    public static Key fromRawPrivateKey(byte[] privateKey) {
        EdDSAPrivateKey sk = new EdDSAPrivateKey(new EdDSAPrivateKeySpec(privateKey, ED25519SPEC));
        EdDSAPublicKey pk = new EdDSAPublicKey(new EdDSAPublicKeySpec(sk.getA(), sk.getParams()));
        return new Key(sk, pk);
    }


    public Key init(IWalletSkill walletSkill) {
        this.walletSkill = walletSkill;
        return this;
    }


    public byte[] getPrivateKey() {
        return sk.getEncoded();
    }


    public byte[] getPublicKey() {
        return pk.getEncoded();
    }


    public byte[] toAddress() {
        return Hash.h160(getPublicKey());
    }


    public String toAddressString() {
        return Hex.encode(toAddress());
    }


    public String toWalletAddress() {
        if (walletSkill != null) {
            return walletSkill.generateWalletAddress(getPublicKey());
        } else {
            throw new CryptoException("Wallet Skill object is not set,please execute Key.init(new WalletSkill()).");
        }
    }


    public String toDAppAddress() {
        if (walletSkill != null) {
            return walletSkill.generateDAppAddress(getPublicKey());
        } else {
            throw new CryptoException("Wallet Skill object is not set,please execute Key.init(new WalletSkill()).");
        }
    }


    public String toMinerAddress() {
        if (walletSkill != null) {
            return walletSkill.generateMinerAddress(getPublicKey());
        } else {
            throw new CryptoException("Wallet Skill object is not set,please execute Key.init(new WalletSkill()).");
        }
    }


    public Signature sign(byte[] message) {
        try {
            byte[] sig;
            if (Native.isEnabled()) {
                sig = Native.sign(message, ByteUtils.merge(sk.getSeed(), sk.getAbyte()));
            } else {
                EdDSAEngine engine = new EdDSAEngine();
                engine.initSign(sk);
                sig = engine.signOneShot(message);
            }

            return new Signature(sig, pk.getAbyte());
        } catch (InvalidKeyException | SignatureException e) {
            throw new CryptoException(e);
        }
    }


    @Override
    public String toString() {
        return toAddressString();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getPrivateKey());
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Key) && Arrays.equals(getPrivateKey(), ((Key) obj).getPrivateKey());
    }


    public Signature signHash256(byte[] message) {
        return sign(Hash.h256(message));
    }


    public static Key toKey(String hexPrivate) throws InvalidKeySpecException {
        return new Key(Hex.decode(hexPrivate));
    }


    public static Key toKey0x(String hexPrivate) throws InvalidKeySpecException {
        return new Key(Hex.decode0x(hexPrivate));
    }


    public static class Signature {
        public static final int LENGTH = 96;

        private static final byte[] X509 = Hex.decode("302a300506032b6570032100");
        private static final int S_LEN = 64;
        private static final int A_LEN = 32;

        private final byte[] s;
        private final byte[] a;


        public Signature(byte[] s, byte[] a) {
            if (s == null || s.length != S_LEN || a == null || a.length != A_LEN) {
                throw new IllegalArgumentException("Invalid S or A");
            }
            this.s = s;
            this.a = a;
        }


        public static Signature fromBytes(byte[] bytes) {
            if (bytes == null || bytes.length != LENGTH) {
                return null;
            }

            byte[] s = Arrays.copyOfRange(bytes, 0, S_LEN);
            byte[] a = Arrays.copyOfRange(bytes, LENGTH - A_LEN, LENGTH);

            return new Signature(s, a);
        }


        public byte[] getS() {
            return s;
        }


        public byte[] getA() {
            return a;
        }


        public byte[] getPublicKey() {
            return ByteUtils.merge(X509, a);
        }


        public byte[] getAddress() {
            return Hash.h160(getPublicKey());
        }


        public byte[] toBytes() {
            return ByteUtils.merge(s, a);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (o == null || getClass() != o.getClass())
                return false;

            return Arrays.equals(toBytes(), ((Signature) o).toBytes());
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(toBytes());
        }
    }
}
