package org.nachain.core.intermediate;

import com.google.common.cache.CacheLoader;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AccountNftCollDAO extends RocksDAO {

    public AccountNftCollDAO() {
        super("AccountNftColl", CoreInstanceEnum.APPCHAIN.id);
    }


    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<AccountNftColl>>() {
            public Optional<AccountNftColl> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public AccountNftColl find(String key) throws ExecutionException {
        return (AccountNftColl) cache.get(key).orElse(null);
    }


    public boolean put(AccountNftColl account) throws RocksDBException {
        put(account.getAddress(), account);
        return true;
    }


    public AccountNftColl get(String address) throws RocksDBException {
        String result = db.get(address);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, AccountNftColl.class);
    }


    public long count() {
        return db.count();
    }


}
