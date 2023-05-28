package org.nachain.core.token.nft;

import com.google.common.collect.Lists;
import org.nachain.core.base.Amount;
import org.nachain.core.token.Token;
import org.nachain.core.token.nft.collection.NftCollection;
import org.nachain.core.token.nft.collection.NftCollectionDAO;
import org.nachain.core.token.protocol.NFTProtocol;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;
import java.util.List;

public class NftCollectionService {


    public static NftCollection newNftCollection(Token token, NFTProtocol nftProtocol) {
        NftCollection nftCollection = new NftCollection();


        long maxMintAmount = Amount.toTokenLong(token.getAmount());


        nftCollection.setInstance(token.getInstanceId());

        nftCollection.setToken(token.getId());

        nftCollection.setEnabledMint(true);

        nftCollection.setEnabledWhiteListMint(false);

        nftCollection.setEnabledSingleMint(false);

        nftCollection.setWhiteList(Lists.newArrayList());

        nftCollection.setFloorPrice(BigInteger.ZERO);

        nftCollection.setMintAmount(0);

        nftCollection.setMaxMintAmount(maxMintAmount);

        nftCollection.setNftProtocol(nftProtocol);

        return nftCollection;
    }


    public static BigInteger getPrice(long instanceId, long token, long nftTokenId) {

        BigInteger mintPrice = BigInteger.ZERO;

        NftCollectionDAO nftCollectionDAO = new NftCollectionDAO(instanceId);
        try {

            NftCollection nftCollection = nftCollectionDAO.get(token);
            if (nftCollection != null) {

                NFTProtocol nftProtocol = nftCollection.getNftProtocol();


                List<BigInteger> mintPrices = nftProtocol.getMintPrices();


                List<Long> mintPricesBatch = nftProtocol.getMintPricesBatch();


                int batchIndex = 0;
                for (long batch : mintPricesBatch) {
                    if (nftTokenId <= batch) {
                        break;
                    }
                    batchIndex++;
                }


                mintPrice = mintPrices.get(batchIndex);
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }

        return mintPrice;
    }


    public static long nextNftItemId(long instanceId, long token) throws RocksDBException {
        return getNftItemId(instanceId, token) + 1;
    }


    public static long getNftItemId(long instanceId, long token) throws RocksDBException {
        long nextTokenId = 0;

        NftCollectionDAO nftCollectionDAO = new NftCollectionDAO(instanceId);
        NftCollection nftCollection = nftCollectionDAO.get(token);

        if (nftCollection != null) {
            nextTokenId = nftCollection.getMintAmount();
        }

        return nextTokenId;
    }


    public static NftCollection getNftCollection(long instanceId, long token) throws RocksDBException {
        NftCollectionDAO nftCollectionDAO = new NftCollectionDAO(instanceId);
        NftCollection nftCollection = nftCollectionDAO.get(token);

        return nftCollection;
    }


    public static boolean updateNftCollection(NftCollection nftCollection) throws RocksDBException {
        NftCollectionDAO nftCollectionDAO = new NftCollectionDAO(nftCollection.getInstance());
        return nftCollectionDAO.edit(nftCollection);
    }

}