package org.nachain.core.token.nft;


import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.config.NFTConfigDAO;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;


@Slf4j
public class NftManager implements INftManager {


    private long instance;


    private int token;


    private NFTConfigDAO nftConfigDAO;


    public NftManager(long instance, int token) {
        this.instance = instance;
        this.token = token;
        try {
            nftConfigDAO = new NFTConfigDAO(instance);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void mint(String address) throws ExecutionException {

        BigInteger mintTotal = nftConfigDAO.getNFTMint(instance);


    }


    @Override
    public int balanceOf(String address) {
        return 0;
    }


    @Override
    public String ownerOf(long tokenId) {
        return null;
    }


    @Override
    public String transferFrom(String from, String to, long tokenId) {
        return null;
    }


    @Override
    public String tokenURI() {
        return null;
    }


    @Override
    public boolean exists(long tokenId) {
        return false;
    }


}

