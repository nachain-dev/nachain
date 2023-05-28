package org.nachain.core.dapp.internal.swap.amm;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;

public class LiquidityPoolDAO extends RocksDAO {


    public LiquidityPoolDAO() throws RocksDBException, IOException {
        super("LiquidityPool", CoreInstanceEnum.SWAP.id);
    }


    public boolean add(LiquidityPool lPool) throws RocksDBException {
        String result = db.get(lPool.getPairName());

        if (result != null) {
            return false;
        }
        put(lPool.getPairName(), lPool);

        return true;
    }


    public boolean edit(LiquidityPool lPool) throws RocksDBException {
        put(lPool.getPairName(), lPool);

        return true;
    }


    public LiquidityPool get(String pairName) throws RocksDBException {
        String result = db.get(pairName);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, LiquidityPool.class);
    }


    public List<LiquidityPool> findAll() {
        List all = db.findAll(LiquidityPool.class);
        return all;
    }

}
