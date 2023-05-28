package org.nachain.core.intermediate;

import com.google.common.cache.CacheLoader;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AccountDetailsDAO extends RocksDAO {

    public AccountDetailsDAO() throws RocksDBException, IOException {
        super("AccountDetails", CoreInstanceEnum.APPCHAIN.id);
    }


    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<AccountDetails>>() {
            public Optional<AccountDetails> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public AccountDetails find(String key) throws ExecutionException {
        return (AccountDetails) cache.get(key).orElse(null);
    }


    public boolean put(AccountDetails accountDetails) throws RocksDBException {
        put(accountDetails.getAddress(), accountDetails);
        return true;
    }


    public AccountDetails get(String address) throws RocksDBException {
        String result = db.get(address);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, AccountDetails.class);
    }


    public long count() {
        return db.count();
    }


}
