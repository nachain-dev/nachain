package org.nachain.core.token.nft.collection;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;

public class NftCollectionDAO extends RocksDAO {


    public NftCollectionDAO(long instance) {
        super("NftCollection", instance);
    }


    public boolean add(NftCollection nft) throws RocksDBException {
        String result = db.get(nft.getToken());

        if (result != null) {
            return false;
        }
        put(nft.getToken(), nft);

        return true;
    }


    public boolean edit(NftCollection nft) throws RocksDBException {
        put(nft.getToken(), nft);

        return true;
    }


    public NftCollection get(long token) throws RocksDBException {
        String result = db.get(token);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, NftCollection.class);
    }


    public List<NftCollection> findAll() {
        List<NftCollection> nftList = db.findAll(NftCollection.class);

        return nftList;
    }
}
