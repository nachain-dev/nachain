package org.nachain.core.crypto.bip32.key;

import org.nachain.core.crypto.bip32.util.HashUtil;
import org.nachain.core.util.exception.CryptoException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class HdKey {
    private byte[] version;
    private int depth;
    private byte[] fingerprint;
    private byte[] childNumber;
    private byte[] chainCode;
    private byte[] keyData;

    HdKey(byte[] version, int depth, byte[] fingerprint, byte[] childNumber, byte[] chainCode, byte[] keyData) {
        this.version = version;
        this.depth = depth;
        this.fingerprint = fingerprint;
        this.childNumber = childNumber;
        this.chainCode = chainCode;
        this.keyData = keyData;
    }

    HdKey() {
    }

    public void setChildNumber(byte[] childNumber) {
        this.childNumber = childNumber;
    }

    public byte[] getChainCode() {
        return chainCode;
    }

    public void setChainCode(byte[] chainCode) {
        this.chainCode = chainCode;
    }


    public byte[] getKey() {

        ByteArrayOutputStream key = new ByteArrayOutputStream();

        try {
            key.write(version);
            key.write(new byte[]{(byte) depth});
            key.write(fingerprint);
            key.write(childNumber);
            key.write(chainCode);
            key.write(keyData);
            byte[] checksum = HashUtil.sha256Twice(key.toByteArray());
            key.write(Arrays.copyOfRange(checksum, 0, 4));
        } catch (IOException e) {
            throw new CryptoException("Unable to write key");
        }

        return key.toByteArray();
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public byte[] getKeyData() {
        return keyData;
    }

    public void setKeyData(byte[] keyData) {
        this.keyData = keyData;
    }

    public byte[] getVersion() {
        return version;
    }

    public void setVersion(byte[] version) {
        this.version = version;
    }

    public byte[] getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(byte[] fingerprint) {
        this.fingerprint = fingerprint;
    }
}
