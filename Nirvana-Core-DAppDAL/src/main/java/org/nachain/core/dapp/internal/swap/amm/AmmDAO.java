package org.nachain.core.dapp.internal.swap.amm;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;

public class AmmDAO extends RocksDAO {


    public AmmDAO() throws RocksDBException, IOException {
        super("Amm", CoreInstanceEnum.SWAP.id);
    }


    public boolean add(Amm amm) throws RocksDBException {
        String result = db.get(amm.getPairName());

        if (result != null) {
            return false;
        }
        put(amm.getPairName(), amm);

        return true;
    }


    public boolean edit(Amm amm) throws RocksDBException {
        put(amm.getPairName(), amm);
        return true;
    }


    public Amm get(String pairName) throws RocksDBException {
        String result = db.get(pairName);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Amm.class);
    }


    public List<Amm> findAll() {
        List all = db.findAll(Amm.class);
        return all;
    }

}
