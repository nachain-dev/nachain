package org.nachain.core.persistence.page;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class PageDetailsDAO extends RocksDAO {


    public PageDetailsDAO(long instance) {
        super("PageDetails", instance);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<PageDetails>>() {
            public Optional<PageDetails> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public boolean add(PageDetails pageDetails) throws RocksDBException {
        String result = db.get(pageDetails.toKey());

        if (result != null) {
            return false;
        }
        put(pageDetails.toKey(), pageDetails);

        return true;
    }


    public boolean edit(PageDetails pageDetails) throws RocksDBException {
        put(pageDetails.toKey(), pageDetails);
        return true;
    }


    public PageDetails get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, PageDetails.class);
    }


    public PageDetails get(String pageClass, String pageKey) throws RocksDBException {
        return get(toKey(pageClass, pageKey));
    }


    public PageDetails find(String pageClass, String pageKey) throws ExecutionException {
        return (PageDetails) cache.get(toKey(pageClass, pageKey)).orElse(null);
    }


    public PageDetails find(String pageClass) throws ExecutionException {
        return (PageDetails) cache.get(toKey(pageClass, "")).orElse(null);
    }


    public List<PageDetails> findAll() {
        List all = db.findAll(PageDetails.class);
        return all;
    }


    public String toKey(String pageClass, String pageKey) {
        return pageClass + "_" + pageKey;
    }

}
