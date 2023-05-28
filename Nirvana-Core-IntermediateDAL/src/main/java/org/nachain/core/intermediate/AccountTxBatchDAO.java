package org.nachain.core.intermediate;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AccountTxBatchDAO extends RocksDAO {

    public AccountTxBatchDAO(long instance) throws RocksDBException, IOException {
        super("AccountTxBatch", instance);
    }

    private String key(AccountTxBatch accountTxBatch) {
        return key(accountTxBatch.getAddress(), accountTxBatch.getTokenId());
    }

    private String key(String address, long tokenId) {
        return address + "_" + tokenId;
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<AccountTxBatch>>() {
            public Optional<AccountTxBatch> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public AccountTxBatch find(String address, long tokenId) throws RocksDBException, ExecutionException {
        String key = key(address, tokenId);
        return (AccountTxBatch) cache.get(key).orElse(null);
    }


    public boolean put(AccountTxBatch accountTxBatch) throws RocksDBException {
        String key = key(accountTxBatch);
        put(key, accountTxBatch);
        return true;
    }

    public AccountTxBatch get(String address, long tokenId) throws RocksDBException {
        String key = key(address, tokenId);
        return get(key);
    }


    public AccountTxBatch get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, AccountTxBatch.class);
    }


    public long count() {
        return super.count();
    }

}
