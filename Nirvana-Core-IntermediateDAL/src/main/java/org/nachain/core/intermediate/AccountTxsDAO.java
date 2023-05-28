package org.nachain.core.intermediate;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AccountTxsDAO extends RocksDAO {

    public AccountTxsDAO(long instance) throws RocksDBException, IOException {
        super("AccountTxs", instance);
    }

    private String key(AccountTxs accountTxs) {
        return key(accountTxs.address, accountTxs.getTokenId(), accountTxs.getTxBatchType(), accountTxs.batchID);
    }

    private String key(String address, long tokenId, TxBatchType txBatchType, long batchID) {
        return address + "_" + tokenId + "_" + txBatchType.symbol + "_" + batchID;
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<AccountTxs>>() {
            public Optional<AccountTxs> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public boolean put(AccountTxs accountTxs) throws RocksDBException {
        String key = key(accountTxs);
        put(key, accountTxs);
        return true;
    }


    public AccountTxs find(String address, long tokenId, TxBatchType txBatchType, long batchID) throws RocksDBException, ExecutionException {
        String key = key(address, tokenId, txBatchType, batchID);
        return (AccountTxs) cache.get(key).orElse(null);
    }


    public AccountTxs get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, AccountTxs.class);
    }


    public long count() {
        return db.count();
    }

}
