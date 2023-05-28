package org.nachain.core.dapp.internal.swap.trading.request;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;


public class UnBuyRequestDAO extends RocksDAO {


    public UnBuyRequestDAO() throws RocksDBException, IOException {
        super("UnBuyRequest", CoreInstanceEnum.SWAP.id);
    }


    public boolean add(BuyRequest buyRequest) throws RocksDBException {
        String result = db.get(buyRequest.getHash());

        if (result != null) {
            return false;
        }
        put(buyRequest.getHash(), buyRequest);

        return true;
    }


    public BuyRequest get(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, BuyRequest.class);
    }


    public boolean delete(String hash) throws RocksDBException {
        return super.delete(hash);
    }


    public List<BuyRequest> findAll() {
        List all = db.findAll(BuyRequest.class);
        return all;
    }
}
