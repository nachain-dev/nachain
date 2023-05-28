package org.nachain.core.chain.das.unverified;

import org.nachain.core.chain.das.TxDas;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UnverifiedTxDasDAO extends RocksDAO {


    public UnverifiedTxDasDAO(long instance) throws RocksDBException, IOException {
        super("UnverifiedTxDas", instance);
    }


    public boolean add(TxDas txDas) throws RocksDBException {
        put(txDas.getHash(), txDas);
        return true;
    }


    public boolean delete(String txHash) throws RocksDBException {
        return super.delete(txHash);
    }


    public TxDas find(String txHash) throws RocksDBException {
        String result = db.get(txHash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, TxDas.class);
    }


    public List<TxDas> findList(long token, List<String> txHashList) throws RocksDBException {
        List<TxDas> txList = new ArrayList<>();
        for (String hash : txHashList) {
            TxDas tx = find(hash);

            if (tx != null && token == tx.getToken()) {
                txList.add(tx);
            }
        }

        return txList;
    }


    public List<TxDas> findAll() {
        List txDasList = db.findAll(TxDas.class);
        return txDasList;
    }


    public long count() {
        return db.count();
    }


}
