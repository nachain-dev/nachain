package org.nachain.core.intermediate;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AccountNftItemDAO extends RocksDAO {

    public AccountNftItemDAO(long instance) {
        super("AccountNftItem", instance);
    }


    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<AccountNftItem>>() {
            public Optional<AccountNftItem> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public AccountNftItem find(String key) throws ExecutionException {
        return (AccountNftItem) cache.get(key).orElse(null);
    }


    public AccountNftItem find(String address, long collTokenId) throws ExecutionException {
        String key = toKey(address, collTokenId);
        return find(key);
    }


    public boolean put(AccountNftItem account) throws RocksDBException {
        String key = toKey(account);
        put(key, account);
        return true;
    }


    public AccountNftItem get(String address, long collTokenId) throws RocksDBException {
        String key = toKey(address, collTokenId);
        return get(key);
    }


    public AccountNftItem get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, AccountNftItem.class);
    }


    public long count() {
        return db.count();
    }


    public static String toKey(AccountNftItem accountNftItem) {
        return toKey(accountNftItem.getAddress(), accountNftItem.getCollTokenId());
    }


    public static String toKey(String address, long collTokenId) {
        return String.format("%s_%d", address, collTokenId);
    }

}
