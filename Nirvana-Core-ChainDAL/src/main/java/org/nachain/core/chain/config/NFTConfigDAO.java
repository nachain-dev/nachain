package org.nachain.core.chain.config;

import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class NFTConfigDAO extends AbstractConfigDAO {

    private final String CHAIN_TOKEN_NFT_D_MINT = "Chain.Token.NFT.%d.Mint";


    public NFTConfigDAO(long instance) throws RocksDBException, IOException {
        super(instance);
    }

    @Override
    public String[] getRegKey() {
        return new String[]{CHAIN_TOKEN_NFT_D_MINT};
    }


    private boolean saveNFTMint(long token, BigInteger mintTotal) throws RocksDBException {
        String key = String.format(CHAIN_TOKEN_NFT_D_MINT, token);
        put(key, mintTotal.toString());
        return true;
    }


    public BigInteger getNFTMint(long token) throws ExecutionException {
        String key = String.format(CHAIN_TOKEN_NFT_D_MINT, token);
        String result = findValue(key);
        if (result != null) {
            return new BigInteger(result);
        }
        return BigInteger.ZERO;
    }


}
