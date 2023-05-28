package org.nachain.core.persistence.rocksdb;

import com.google.common.cache.CacheLoader;

public interface IDataCache {
    CacheLoader cacheLoader();
}
