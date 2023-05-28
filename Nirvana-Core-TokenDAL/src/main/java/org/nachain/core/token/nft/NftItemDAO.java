package org.nachain.core.token.nft;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;

public class NftItemDAO extends RocksDAO {


    public NftItemDAO(long instance) {
        super("NftItem", instance);
    }


    public boolean add(NftItem nft) throws RocksDBException {
        String result = db.get(nft.getNftItemId());

        if (result != null) {
            return false;
        }
        put(nft.getNftItemId(), nft);

        return true;
    }


    public boolean put(NftItem nft) throws RocksDBException {
        put(nft.getNftItemId(), nft);

        return true;
    }


    public NftItem get(long nftItemId) throws RocksDBException {
        String result = db.get(nftItemId);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, NftItem.class);
    }


    public List<NftItem> gets(long startItemId, long endItemId) throws RocksDBException {
        List<NftItem> nftItemList = db.gets(startItemId, endItemId, NftItem.class);

        return nftItemList;
    }


    public List<NftItem> findAll() {
        List nftList = db.findAll(NftItem.class);

        return nftList;
    }
}
