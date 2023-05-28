package org.nachain.core.dapp.internal.swap.coinage;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;

public class CoinageDAO extends RocksDAO {


    public CoinageDAO() throws RocksDBException, IOException {
        super("Coinage", CoreInstanceEnum.SWAP.id);
    }


    public boolean add(Coinage coinage) throws RocksDBException {
        String result = db.get(coinage.getCoinageName());

        if (result != null) {
            return false;
        }
        put(coinage.getCoinageName(), coinage);

        return true;
    }


    public boolean edit(Coinage coinage) throws RocksDBException {
        put(coinage.getCoinageName(), coinage);

        return true;
    }


    public Coinage get(String coinageName) throws RocksDBException {
        String result = db.get(coinageName);
        if (result == null) {
            return null;
        }
        return Coinage.to(result);
    }


    public List<Coinage> findAll() {
        List all = db.findAll(Coinage.class);
        return all;
    }

}
