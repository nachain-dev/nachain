package org.nachain.core.crypto.bip32.util;

import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class Secp256k1 {
    static final X9ECParameters SECP = CustomNamedCurves.getByName("secp256k1");


    public static byte[] serP(ECPoint p) {
        return p.getEncoded(true);
    }

    public static ECPoint deserP(byte[] p) {
        return SECP.getCurve().decodePoint(p);
    }


    public static ECPoint point(BigInteger p) {
        return SECP.getG().multiply(p);
    }


    public static BigInteger getN() {
        return SECP.getN();
    }
}
