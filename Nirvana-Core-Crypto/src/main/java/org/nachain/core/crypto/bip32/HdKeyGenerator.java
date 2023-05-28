package org.nachain.core.crypto.bip32;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.nachain.core.crypto.bip32.key.HdPrivateKey;
import org.nachain.core.crypto.bip32.key.HdPublicKey;
import org.nachain.core.crypto.bip32.key.KeyVersion;
import org.nachain.core.crypto.bip32.util.BytesUtil;
import org.nachain.core.crypto.bip32.util.HashUtil;
import org.nachain.core.crypto.bip32.util.Hmac;
import org.nachain.core.crypto.bip32.util.Secp256k1;
import org.nachain.core.util.Hex;
import org.nachain.core.util.exception.CryptoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HdKeyGenerator {

    public static final String MASTER_PATH = "m";
    private static final Logger logger = LoggerFactory.getLogger(HdKeyGenerator.class);
    private static final EdDSAParameterSpec ED25519SPEC = EdDSANamedCurveTable.getByName("ed25519");

    public HdKeyPair getMasterKeyPairFromSeed(byte[] seed, KeyVersion keyVersion, CoinType coinType) {

        Scheme scheme = coinType.getScheme();
        HdPublicKey publicKey = new HdPublicKey();
        HdPrivateKey privateKey = new HdPrivateKey();
        HdKeyPair key = new HdKeyPair(privateKey, publicKey, coinType, MASTER_PATH);

        byte[] I = Hmac.hmac512(seed, scheme.getSeed().getBytes(StandardCharsets.UTF_8));
        byte[] IL = Arrays.copyOfRange(I, 0, 32);
        byte[] IR = Arrays.copyOfRange(I, 32, 64);

        switch (scheme) {
            case BIP32:
                BigInteger masterSecretKey = BytesUtil.parse256(IL);

                if (masterSecretKey.compareTo(BigInteger.ZERO) == 0
                        || masterSecretKey.compareTo(Secp256k1.getN()) > 0) {
                    throw new CryptoException("The master key is invalid");
                }
            case SLIP10_ED25519:

                break;
            case BIP32_ED25519:

                while ((I[31] & 0b00100000) != 0) {
                    I = Hmac.hmac512(I, scheme.getSeed().getBytes(StandardCharsets.UTF_8));
                }

                I[0] = (byte) (I[0] & ~0b00000111);

                I[31] = (byte) (I[31] & ~0b10000000);

                I[31] = (byte) (I[31] | 0b01000000);

                IL = Arrays.copyOfRange(I, 0, 32);
                IR = Arrays.copyOfRange(I, 32, 64);
                break;
        }

        privateKey.setVersion(keyVersion.getPrivateKeyVersion());
        privateKey.setDepth(0);
        privateKey.setFingerprint(new byte[]{0, 0, 0, 0});
        privateKey.setChildNumber(new byte[]{0, 0, 0, 0});
        privateKey.setChainCode(IR);

        publicKey.setVersion(keyVersion.getPublicKeyVersion());
        publicKey.setDepth(0);
        publicKey.setFingerprint(new byte[]{0, 0, 0, 0});
        publicKey.setChildNumber(new byte[]{0, 0, 0, 0});
        publicKey.setChainCode(IR);

        switch (scheme) {
            case BIP32:
                privateKey.setKeyData(BytesUtil.merge(new byte[]{0}, IL));
                publicKey.setKeyData(Secp256k1.serP(Secp256k1.point(BytesUtil.parse256(IL))));
                break;
            case SLIP10_ED25519:
                EdDSAPrivateKey sk = new EdDSAPrivateKey(new EdDSAPrivateKeySpec(IL, ED25519SPEC));
                EdDSAPublicKey pk = new EdDSAPublicKey(new EdDSAPublicKeySpec(sk.getA(), sk.getParams()));
                privateKey.setKeyData(IL);
                publicKey.setKeyData(BytesUtil.merge(new byte[]{0}, pk.getAbyte()));
                break;
            case BIP32_ED25519:

                byte[] A = ED25519SPEC.getB().scalarMultiply(IL).toByteArray();
                privateKey.setKeyData(I);
                publicKey.setKeyData(A);

                byte[] c = Hmac.hmac256(BytesUtil.merge(new byte[]{1}, seed),
                        scheme.getSeed().getBytes(StandardCharsets.UTF_8));
                privateKey.setChainCode(c);
                publicKey.setChainCode(c);
                break;
        }

        return key;
    }


    public HdPublicKey getChildPublicKey(HdPublicKey parent, long child, boolean isHardened, Scheme scheme) {
        if (isHardened) {
            throw new CryptoException("Cannot derive child public keys from hardened keys");
        }

        if (scheme == Scheme.SLIP10_ED25519) {
            throw new UnsupportedOperationException("Unable to derive ed25519 public key chaining");
        }

        if (scheme == Scheme.BIP32_ED25519) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        byte[] key = parent.getKeyData();
        byte[] data = BytesUtil.merge(key, BytesUtil.ser32(child));


        byte[] I = Hmac.hmac512(data, parent.getChainCode());
        byte[] IL = Arrays.copyOfRange(I, 0, 32);
        byte[] IR = Arrays.copyOfRange(I, 32, 64);

        HdPublicKey publicKey = new HdPublicKey();

        publicKey.setVersion(parent.getVersion());
        publicKey.setDepth(parent.getDepth() + 1);

        byte[] pKd = parent.getKeyData();
        byte[] h160 = HashUtil.h160(pKd);
        byte[] childFingerprint = new byte[]{h160[0], h160[1], h160[2], h160[3]};

        BigInteger ILBigInt = BytesUtil.parse256(IL);
        ECPoint point = Secp256k1.point(ILBigInt);
        point = point.add(Secp256k1.deserP(parent.getKeyData()));

        if (ILBigInt.compareTo(Secp256k1.getN()) > 0 || point.isInfinity()) {
            throw new CryptoException("This key is invalid, should proceed to next key");

        }

        byte[] childKey = Secp256k1.serP(point);

        publicKey.setFingerprint(childFingerprint);
        publicKey.setChildNumber(BytesUtil.ser32(child));
        publicKey.setChainCode(IR);
        publicKey.setKeyData(childKey);

        return publicKey;
    }


    public HdKeyPair getChildKeyPair(HdKeyPair parent, long child, boolean isHardened) {
        HdPrivateKey privateKey = new HdPrivateKey();
        HdPublicKey publicKey = new HdPublicKey();
        HdKeyPair key = new HdKeyPair(privateKey, publicKey, parent.getCoinType(),
                getPath(parent.getPath(), child, isHardened));

        if (isHardened) {
            child += 0x80000000;
        } else if (parent.getCoinType().getScheme() == Scheme.SLIP10_ED25519) {
            throw new CryptoException("ed25519 only supports hardened keys");
        }

        byte[] xChain = parent.getPrivateKey().getChainCode();

        byte[] I;
        if (isHardened) {


            BigInteger kpar = BytesUtil.parse256(parent.getPrivateKey().getKeyData());
            byte[] data = BytesUtil.merge(new byte[]{0}, BytesUtil.ser256(kpar), BytesUtil.ser32(child));
            I = Hmac.hmac512(data, xChain);
        } else {


            byte[] data = BytesUtil.merge(parent.getPublicKey().getKeyData(), BytesUtil.ser32(child));
            I = Hmac.hmac512(data, xChain);
        }

        byte[] IL = Arrays.copyOfRange(I, 0, 32);
        byte[] IR = Arrays.copyOfRange(I, 32, 64);

        byte[] childNumber = BytesUtil.ser32(child);

        privateKey.setVersion(parent.getPrivateKey().getVersion());
        privateKey.setDepth(parent.getPrivateKey().getDepth() + 1);
        privateKey.setChildNumber(childNumber);
        privateKey.setChainCode(IR);

        publicKey.setVersion(parent.getPublicKey().getVersion());
        publicKey.setDepth(parent.getPublicKey().getDepth() + 1);
        publicKey.setChildNumber(childNumber);
        publicKey.setChainCode(IR);

        switch (parent.getCoinType().getScheme()) {
            case BIP32:

                BigInteger parse256 = BytesUtil.parse256(IL);
                BigInteger kpar = BytesUtil.parse256(parent.getPrivateKey().getKeyData());
                BigInteger childSecretKey = parse256.add(kpar).mod(Secp256k1.getN());

                byte[] fingerprintSK = BytesUtil.getFingerprint(parent.getPrivateKey().getKeyData());
                privateKey.setFingerprint(fingerprintSK);
                privateKey.setKeyData(BytesUtil.merge(new byte[]{0}, BytesUtil.ser256(childSecretKey)));

                byte[] pKd = parent.getPublicKey().getKeyData();
                byte[] h160 = HashUtil.h160(pKd);
                byte[] fingerprintPK = new byte[]{h160[0], h160[1], h160[2], h160[3]};
                publicKey.setFingerprint(fingerprintPK);
                publicKey.setKeyData(Secp256k1.serP(Secp256k1.point(childSecretKey)));
                break;
            case SLIP10_ED25519:

                h160 = HashUtil.h160(parent.getPublicKey().getKeyData());
                fingerprintPK = new byte[]{h160[0], h160[1], h160[2], h160[3]};

                privateKey.setFingerprint(fingerprintPK);
                privateKey.setKeyData(IL);

                EdDSAPrivateKey sk = new EdDSAPrivateKey(new EdDSAPrivateKeySpec(IL, ED25519SPEC));
                EdDSAPublicKey pk = new EdDSAPublicKey(new EdDSAPublicKeySpec(sk.getA(), sk.getParams()));
                publicKey.setFingerprint(fingerprintPK);
                publicKey.setKeyData(BytesUtil.merge(new byte[]{0}, pk.getAbyte()));
                break;
            case BIP32_ED25519:
                byte[] kP = parent.getPrivateKey().getKeyData();
                byte[] kLP = Arrays.copyOfRange(kP, 0, 32);
                byte[] kRP = Arrays.copyOfRange(kP, 32, 64);
                byte[] AP = parent.getPublicKey().getKeyData();
                byte[] cP = parent.getPublicKey().getChainCode();

                byte[] Z, c;
                if (isHardened) {
                    byte[] data = BytesUtil.merge(new byte[]{0}, kLP, kRP, BytesUtil.ser32LE(child));
                    Z = Hmac.hmac512(data, cP);
                    data[0] = 1;
                    c = Hmac.hmac512(data, cP);
                } else {
                    byte[] data = BytesUtil.merge(new byte[]{2}, AP, BytesUtil.ser32LE(child));
                    Z = Hmac.hmac512(data, cP);
                    data[0] = 3;
                    c = Hmac.hmac512(data, cP);
                }
                c = Arrays.copyOfRange(c, 32, 64);
                byte[] ZL = Arrays.copyOfRange(Z, 0, 28);
                byte[] ZR = Arrays.copyOfRange(Z, 32, 64);

                if (logger.isTraceEnabled()) {
                    logger.trace("parent, kLP = " + Hex.encode(kLP));
                    logger.trace("parent, kRP = " + Hex.encode(kRP));
                    logger.trace("parent,  AP = " + Hex.encode(AP));
                    logger.trace("parent,  cP = " + Hex.encode(cP));
                }

                BigInteger kLiBI = parseUnsignedLE(ZL)
                        .multiply(BigInteger.valueOf(8))
                        .add(parseUnsignedLE(kLP));
                BigInteger order = BigInteger.valueOf(2).pow(252)
                        .add(new BigInteger("27742317777372353535851937790883648493"));
                if (kLiBI.mod(order).equals(BigInteger.ZERO)) {
                    return null;
                }
                IL = serializeUnsignedLE256(kLiBI);

                BigInteger kRiBI = parseUnsignedLE(ZR)
                        .add(parseUnsignedLE(kRP))
                        .mod(BigInteger.valueOf(2).pow(256));
                IR = serializeUnsignedLE256(kRiBI);

                I = BytesUtil.merge(IL, IR);
                byte[] A = ED25519SPEC.getB().scalarMultiply(IL).toByteArray();

                privateKey.setKeyData(I);
                publicKey.setKeyData(A);

                privateKey.setChainCode(c);
                publicKey.setChainCode(c);

                if (logger.isTraceEnabled()) {
                    logger.trace("child, IL = " + Hex.encode(IL));
                    logger.trace("child, IR = " + Hex.encode(IR));
                    logger.trace("child,  A = " + Hex.encode(A));
                    logger.trace("child,  c = " + Hex.encode(c));
                }

                break;
        }

        return key;
    }

    private String getPath(String parentPath, long child, boolean isHardened) {
        if (parentPath == null) {
            parentPath = MASTER_PATH;
        }
        return parentPath + "/" + child + (isHardened ? "'" : "");
    }

    private void reverse(byte[] input) {
        for (int i = 0; i < input.length / 2; i++) {
            byte temp = input[i];
            input[i] = input[input.length - 1 - i];
            input[input.length - 1 - i] = temp;
        }
    }

    private BigInteger parseUnsignedLE(byte[] bytes) {
        byte[] temp = bytes.clone();
        reverse(temp);
        return new BigInteger(1, temp);
    }

    private byte[] serializeUnsignedLE256(BigInteger bi) {
        byte[] temp = bi.toByteArray();
        if (temp.length > 32) {
            temp = Arrays.copyOfRange(temp, temp.length - 32, temp.length);
        }

        reverse(temp);

        if (temp.length < 32) {
            return Arrays.copyOf(temp, 32);
        } else {
            return temp;
        }
    }
}
