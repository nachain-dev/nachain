package org.nachain.core.chain.transaction.unverified;

import com.google.common.cache.CacheLoader;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


public class UnverifiedTxDAO extends RocksDAO {


    public UnverifiedTxDAO(long instance) {
        super("UnverifiedTx", instance);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<Tx>>() {
            public Optional<Tx> load(String txHash) throws RocksDBException {
                return Optional.ofNullable(get(txHash));
            }
        };
    }


    public Tx find(String txHash) throws RocksDBException, ExecutionException {
        return (Tx) cache.get(txHash).orElse(null);
    }


    public List<Tx> findList(long token, List<String> txHashList) throws RocksDBException, ExecutionException {
        List<Tx> txList = new ArrayList<>();
        for (String hash : txHashList) {
            Tx tx = find(hash);
            if (tx != null && token == tx.getToken()) {
                txList.add(tx);
            }
        }

        return txList;
    }


    public BigInteger sumValue(long token, List<String> txHashList) throws RocksDBException, ExecutionException {
        BigInteger balance = BigInteger.ZERO;
        List<Tx> txList = findList(token, txHashList);
        for (Tx tx : txList) {
            if (tx != null) {
                balance = balance.add(tx.getValue());
            }
        }

        return balance;
    }


    public boolean put(Tx tx) throws RocksDBException {
        put(tx.getHash(), tx);
        return true;
    }


    public boolean delete(String txHash) throws RocksDBException {
        super.delete(txHash);
        return true;
    }


    public Tx get(String txHash) throws RocksDBException {
        String result = db.get(txHash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Tx.class);
    }

}
