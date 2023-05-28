package org.nachain.core.token.nft;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;

public class NftItemDetailDAO extends RocksDAO {


    public NftItemDetailDAO(long instance) {
        super("NftItemDetail", instance);
    }


    public boolean add(NftItemDetail nftDetail) throws RocksDBException {
        String result = db.get(nftDetail.getNftItemId());

        if (result != null) {
            return false;
        }
        put(nftDetail.getNftItemId(), nftDetail);

        return true;
    }


    public NftItemDetail get(long nftItemId) throws RocksDBException {
        String result = db.get(nftItemId);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, NftItemDetail.class);
    }


    public List<NftItemDetail> findAll() {
        List nftList = db.findAll(NftItemDetail.class);

        return nftList;
    }

}
