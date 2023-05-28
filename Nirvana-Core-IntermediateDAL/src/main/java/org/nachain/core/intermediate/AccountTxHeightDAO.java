package org.nachain.core.intermediate;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AccountTxHeightDAO extends RocksDAO {


    public AccountTxHeightDAO(long instance) {
        super("AccountTxHeight", instance);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<AccountTxHeight>>() {
            public Optional<AccountTxHeight> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public AccountTxHeight find(String key) throws ExecutionException {
        return (AccountTxHeight) cache.get(key).orElse(null);
    }


    public boolean add(AccountTxHeight accountTxHeight) throws RocksDBException {
        String key = toKey(accountTxHeight);
        String result = db.get(key);

        if (result != null) {
            return false;
        }
        put(key, accountTxHeight);
        return true;
    }


    public boolean put(AccountTxHeight accountTxHeight) throws RocksDBException {
        String key = toKey(accountTxHeight);
        put(key, accountTxHeight);
        return true;
    }


    public AccountTxHeight get(String keyName) throws RocksDBException {
        String result = db.get(keyName);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, AccountTxHeight.class);
    }


    public long count() {
        return db.count();
    }


    public String toKey(String wallet, long instance, long token) {
        return wallet + "_" + instance + "_" + token;
    }


    public String toKey(AccountTxHeight accountTxHeight) {
        return toKey(accountTxHeight.getWallet(), accountTxHeight.getInstance(), accountTxHeight.getToken());
    }
}
