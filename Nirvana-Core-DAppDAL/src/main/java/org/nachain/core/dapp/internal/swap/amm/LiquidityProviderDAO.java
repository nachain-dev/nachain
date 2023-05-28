package org.nachain.core.dapp.internal.swap.amm;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;

public class LiquidityProviderDAO extends RocksDAO {


    public LiquidityProviderDAO() throws RocksDBException, IOException {
        super("LiquidityProvider", CoreInstanceEnum.SWAP.id);
    }


    public boolean add(LiquidityProvider lp) throws RocksDBException {
        String result = db.get(lp.getHash());

        if (result != null) {
            return false;
        }
        put(lp.getHash(), lp);

        return true;
    }


    public LiquidityProvider get(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, LiquidityProvider.class);
    }


    public List<LiquidityProvider> findAll() {
        List all = db.findAll(LiquidityProvider.class);
        return all;
    }

}
