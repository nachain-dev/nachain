package org.nachain.core.persistence.page;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class PageDAO extends RocksDAO {


    public PageDAO(long instance) {
        super("Page", instance);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<Page>>() {
            public Optional<Page> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public boolean add(Page page) throws RocksDBException {
        String result = db.get(page.toKey());

        if (result != null) {
            return false;
        }
        put(page.toKey(), page);

        return true;
    }


    public boolean edit(Page page) throws RocksDBException {
        put(page.toKey(), page);
        return true;
    }


    public Page get(String pageClass, String pageKey, long pageNum) throws RocksDBException {
        return get(toKey(pageClass, pageKey, pageNum));
    }

    public Page get(Class pageClass, String pageKey, long pageNum) throws RocksDBException {
        return get(toKey(pageClass.getSimpleName(), pageKey, pageNum));
    }


    public Page find(String pageClass, String pageKey, long pageNum) throws ExecutionException {
        String key = toKey(pageClass, pageKey, pageNum);
        return (Page) cache.get(key).orElse(null);
    }

    public Page find(String pageClass, long pageNum) throws ExecutionException {
        String key = toKey(pageClass, "", pageNum);
        return (Page) cache.get(key).orElse(null);
    }


    public Page get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Page.class);
    }


    public List<Page> findAll() {
        List all = db.findAll(Page.class);
        return all;
    }


    public String toKey(String className, String pageKey, long pageNum) {
        return className + "_" + pageKey + "_" + pageNum;
    }

}
