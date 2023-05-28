package org.nachain.core.dapp.internal.swap.trading.request;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;


public class UnSellRequestDAO extends RocksDAO {


    public UnSellRequestDAO() throws RocksDBException, IOException {
        super("UnSellRequest", CoreInstanceEnum.SWAP.id);
    }


    public boolean add(SellRequest sellRequest) throws RocksDBException {
        String result = db.get(sellRequest.getHash());

        if (result != null) {
            return false;
        }
        put(sellRequest.getHash(), sellRequest);

        return true;
    }


    public SellRequest get(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, SellRequest.class);
    }


    public boolean delete(String hash) throws RocksDBException {
        return super.delete(hash);
    }


    public List<SellRequest> findAll() {
        List all = db.findAll(SellRequest.class);
        return all;
    }
}
