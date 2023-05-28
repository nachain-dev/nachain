package org.nachain.core.intermediate;

import com.google.common.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
public class AccountBalanceDAO extends RocksDAO {


    public AccountBalanceDAO(long instance) {
        super("AccountBalance", instance);
    }

    private String key(AccountBalance account) {
        return key(account.getAddress(), account.getToken());
    }

    private String key(String wallet, long token) {
        return wallet + "_" + token;
    }


    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<AccountBalance>>() {
            public Optional<AccountBalance> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public AccountBalance find(String key) throws ExecutionException {
        return (AccountBalance) cache.get(key).orElse(null);
    }


    public AccountBalance find(String address, long token) throws RocksDBException, ExecutionException {
        String key = key(address, token);
        return find(key);
    }


    public boolean put(AccountBalance account) throws RocksDBException {
        String key = key(account);
        put(key, account);
        return true;
    }


    public AccountBalance get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, AccountBalance.class);
    }


    public long count() {
        return db.count();
    }

}
