package org.nachain.core.crypto.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import org.nachain.core.util.ByteArray;
import org.nachain.core.util.exception.CryptoException;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public final class PublicKeyCache {

    private static final int MAX_CACHE_SIZE = 16 * 1024;


    private static final Cache<ByteArray, EdDSAPublicKey> pubKeyCache = Caffeine.newBuilder()
            .maximumSize(MAX_CACHE_SIZE).build();

    private PublicKeyCache() {
    }


    public static EdDSAPublicKey computeIfAbsent(byte[] pubKey) {
        return pubKeyCache.get(ByteArray.of(pubKey), input -> {
            try {
                return new EdDSAPublicKey(new X509EncodedKeySpec(pubKey));
            } catch (InvalidKeySpecException e) {
                throw new CryptoException(e);
            }
        });
    }
}
