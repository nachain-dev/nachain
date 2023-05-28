package org.nachain.core.persistence.rocksdb;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.config.ChainConfig;
import org.nachain.core.config.Constants;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class RocksDAO implements IDataCache {


    protected String dbName;


    protected long groupId = 0;


    private static Map<String, RocksDB> holder = new ConcurrentHashMap<>();


    private static Map<String, LoadingCache<Object, Optional<Object>>> cacheHolder = new ConcurrentHashMap<>();


    private static Map<String, Counter> counterHolder = new ConcurrentHashMap<>();


    protected RocksDB db;


    protected LoadingCache<Object, Optional<Object>> cache;


    protected Counter counter;

    public RocksDAO() {
    }


    public RocksDAO(Class clazz, long groupId) {
        init(clazz.getSimpleName(), groupId);
    }


    public RocksDAO(String dbName) {
        init(dbName, groupId);
    }


    public RocksDAO(String dbName, long groupId) {
        init(dbName, groupId);
    }


    public RocksDAO(String dbName, long groupId, String dbPath) {
        init(dbName, groupId, dbPath + File.separator + dbName);
    }


    protected void init(String dbName, long groupID) {
        String dbPath = ChainConfig.DATA_PATH + File.separator + Constants.CHAIN_DIR + File.separator + dbName;
        init(dbName, groupID, dbPath);
    }


    protected void init(String dbName, long groupId, String dbPath) {
        this.dbName = dbName;
        this.groupId = groupId;
        this.db = holder.get(getHolderKey());
        this.cache = cacheHolder.get(getHolderKey());
        this.counter = counterHolder.get(getHolderKey());

        if (db == null) {
            synchronized (holder) {

                db = holder.get(getHolderKey());
                if (db == null) {
                    try {

                        File dbFile = new File(dbPath);
                        dbFile.mkdirs();
                        if (!dbFile.exists()) {
                            throw new RuntimeException(String.format("RocksDB file not found. path=%s", dbPath));
                        }
                        db = new RocksDB(dbName, groupId, dbPath);
                    } catch (RocksDBException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    holder.put(getHolderKey(), db);
                }
            }
        }

        if (cache == null) {
            synchronized (cacheHolder) {
                cache = cacheHolder.get(getHolderKey());
                if (cache == null) {

                    CacheLoader cacheLoader = cacheLoader();
                    if (cacheLoader != null) {

                        cache = CacheBuilder.newBuilder()

                                .maximumSize(100000)

                                .expireAfterAccess(30, TimeUnit.MINUTES)

                                .build(cacheLoader);

                        cacheHolder.put(getHolderKey(), cache);
                    }
                }
            }
        }

        if (counter == null) {
            synchronized (counterHolder) {
                counter = counterHolder.get(getHolderKey());
                if (counter == null) {

                    counter = new Counter();

                    counter.setDataAmount(db().count());
                    if (counter != null) {

                        counterHolder.put(getHolderKey(), counter);
                    }
                }
            }
        }
    }

    @Override
    public CacheLoader cacheLoader() {
        return null;
    }


    public RocksDB db() {
        return this.db;
    }


    public String getDbName() {
        return dbName;
    }


    public long getGroupId() {
        return groupId;
    }


    private String getHolderKey() {
        return dbName + "." + groupId;
    }


    public void close() {
        synchronized (holder) {
            if (db != null) {

                holder.remove(getHolderKey());

                db.close();
            }
        }
    }


    protected boolean put(long key, Object value) throws RocksDBException {
        String keyName = String.valueOf(key);


        if (db.get(keyName) == null) {

            counter.add();
        }


        db.put(keyName, JsonUtils.objectToJson(value));


        if (cache != null) {

            cache.put(key, Optional.of(value));
        }

        return true;
    }


    protected boolean put(String keyName, Object value) throws RocksDBException {

        if (db.get(keyName) == null) {

            counter.add();
        }


        db.put(keyName, JsonUtils.objectToJson(value));


        if (cache != null) {

            cache.put(keyName, Optional.of(value));
        }

        return true;
    }


    public boolean delete(long key) throws RocksDBException {
        String keyName = String.valueOf(key);


        if (db.get(keyName) != null) {

            counter.remove();
        }


        db.delete(keyName);


        if (cache != null) {
            cache.invalidate(key);
        }

        return true;
    }


    public boolean delete(String keyName) throws RocksDBException {

        if (db.get(keyName) != null) {

            counter.remove();
        }


        db.delete(keyName);


        if (cache != null) {
            cache.invalidate(keyName);
        }

        return true;
    }


    public long count() {
        return counter.getDataAmount();
    }


    public long cacheSize() {
        if (cache != null) {
            return cache.size();
        }

        return 0;
    }


    public Object getRand(Class clazz, int limit) {
        Random ra = new Random();

        int min = 1;
        int max = (int) count();
        if (limit != 0) {
            if (max > limit) {
                max = limit;
            }
        }

        int rand = 0;
        if (max > 0) {
            rand = ra.nextInt(max) + min;
        }

        int index = 0;
        try (final RocksIterator iterator = db.getRocksDB().newIterator()) {

            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {

                index++;

                if (rand == index) {
                    byte[] value = iterator.value();
                    return JsonUtils.jsonToPojo(new String(value), clazz);
                }
            }
        }
        return null;
    }


    public Object getRand(Class clazz) {
        return getRand(clazz, 0);
    }
}
