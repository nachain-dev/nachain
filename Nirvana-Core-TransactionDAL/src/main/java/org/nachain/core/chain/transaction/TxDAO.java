package org.nachain.core.chain.transaction;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

public class TxDAO extends RocksDAO {


    public TxDAO(long instance) throws RocksDBException, IOException {
        super("Tx", instance);
    }


    public TxDAO(String dbName) throws RocksDBException, IOException {
        super(dbName);
    }


    public TxDAO(String dbName, long instance) throws RocksDBException, IOException {
        super(dbName, instance);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<Tx>>() {
            public Optional<Tx> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public Tx find(String txHash) throws ExecutionException {
        return (Tx) cache.get(txHash).orElse(null);
    }


    public List<Tx> findList(long token, final List<String> txHashList) throws ExecutionException {
        List<Tx> txList = new ArrayList<>();


        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>(txHashList);
        for (String hash : cowList) {
            Tx tx = find(hash);
            if (tx != null && tx.getToken() == token) {
                txList.add(tx);
            }
        }

        return txList;
    }


    public BigInteger sumValue(long token, List<String> txHashList) throws ExecutionException {
        BigInteger balance = BigInteger.ZERO;

        List<Tx> txList = findList(token, txHashList);
        for (Tx tx : txList) {
            if (tx != null) {
                balance = balance.add(tx.getValue());
            }
        }

        return balance;
    }


    public boolean add(Tx tx) throws RocksDBException {

        if (get(tx.getHash()) != null) {
            return false;
        }
        put(tx.getHash(), tx);
        return true;
    }


    public boolean edit(Tx tx) throws RocksDBException, ExecutionException {

        if (get(tx.getHash()) == null) {
            return false;
        }
        put(tx.getHash(), tx);
        return true;
    }


    public Tx get(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Tx.class);
    }

}
