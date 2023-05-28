package org.nachain.core.dapp.internal.swap;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;

public class SwapPairDAO extends RocksDAO {


    public SwapPairDAO() throws RocksDBException, IOException {
        super("SwapPair", CoreInstanceEnum.SWAP.id);
    }


    public boolean add(SwapPair swapPair) throws RocksDBException {
        String result = db.get(swapPair.getPairName());

        if (result != null) {
            return false;
        }
        put(swapPair.getPairName(), swapPair);

        return true;
    }


    public boolean edit(SwapPair swapPair) throws RocksDBException {
        put(swapPair.getPairName(), swapPair);

        return true;
    }


    public SwapPair get(String pairName) throws RocksDBException {
        String result = db.get(pairName);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, SwapPair.class);
    }


    public List<SwapPair> findAll() {
        List all = db.findAll(SwapPair.class);
        return all;
    }

}
