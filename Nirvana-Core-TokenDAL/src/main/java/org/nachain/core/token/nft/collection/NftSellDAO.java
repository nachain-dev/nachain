package org.nachain.core.token.nft.collection;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;


public class NftSellDAO extends RocksDAO {


    public NftSellDAO(long instance) {
        super("NftSell", instance);
    }


    public boolean add(NftSell nftSell) throws RocksDBException {
        String result = db.get(nftSell.getToken());

        if (result != null) {
            return false;
        }
        put(nftSell.getToken(), nftSell);

        return true;
    }


    public NftSell get(long token, long tokenId) throws RocksDBException {
        String result = db.get(token);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, NftSell.class);
    }


    public List<NftSell> findAll() {
        List<NftSell> nftList = db.findAll(NftSell.class);

        return nftList;
    }


    private String toKey(NftSell nftSell) {
        return toKey(nftSell.getToken(), nftSell.getTokenId());
    }


    private String toKey(long token, long tokenId) {
        return String.format("%d_%d", token, token);
    }
}
