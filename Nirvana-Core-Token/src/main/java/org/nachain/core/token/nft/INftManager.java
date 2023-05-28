package org.nachain.core.token.nft;

import org.rocksdb.RocksDBException;

import java.util.concurrent.ExecutionException;


public interface INftManager {


    void mint(String address) throws RocksDBException, ExecutionException;


    int balanceOf(String address);


    String ownerOf(long tokenId);


    String transferFrom(String from, String to, long tokenId);


    String tokenURI();


    boolean exists(long tokenId);

}

