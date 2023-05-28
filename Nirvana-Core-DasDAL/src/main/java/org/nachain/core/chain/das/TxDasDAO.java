package org.nachain.core.chain.das;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TxDasDAO extends RocksDAO {


    public TxDasDAO(long instance) throws RocksDBException, IOException {
        super("TxDas", instance);
    }


    public TxDasDAO(String dbName) {
        super(dbName);
    }


    public boolean add(TxDas tx) throws RocksDBException {
        String result = db.get(tx.getHash());

        if (result != null) {
            return false;
        }
        put(tx.getHash(), tx);
        return true;
    }


    public boolean edit(TxDas tx) throws RocksDBException {
        String result = db.get(tx.getHash());

        if (result == null) {
            return false;
        }
        put(tx.getHash(), tx);
        return true;
    }


    public TxDas get(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, TxDas.class);
    }


    public List<TxDas> findList(long token, List<String> txHashList) throws RocksDBException {
        List<TxDas> txList = new ArrayList<>();
        for (String hash : txHashList) {
            TxDas tx = get(hash);

            if (tx != null && tx.getToken() == token) {
                txList.add(tx);
            }
        }

        return txList;
    }

}
