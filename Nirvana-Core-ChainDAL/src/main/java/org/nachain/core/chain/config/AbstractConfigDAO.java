package org.nachain.core.chain.config;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Sets;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public abstract class AbstractConfigDAO extends RocksDAO implements IConfig {


    private final static Set<String> keyNames = Sets.newHashSet();


    public AbstractConfigDAO() {
        super("Config");

        registerKey(getRegKey());
    }

    public AbstractConfigDAO(long instance) {
        super("Config", instance);

        registerKey(getRegKey());
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<Config>>() {
            public Optional<Config> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    private void registerKey(String... keyNameArray) {
        synchronized (keyNames) {
            for (String keyName : keyNameArray) {
                if (!keyNames.contains(keyName)) {
                    keyNames.add(keyName);
                }
            }
        }
    }


    public Config find(String key) throws ExecutionException {
        return (Config) cache.get(key).orElse(null);
    }


    protected String findValue(String key) throws ExecutionException {
        Config config = find(key);
        if (config != null) {
            return config.getValue();
        }
        return null;
    }


    private boolean put(Config config) throws RocksDBException {
        put(config.getKey(), config);
        return true;
    }


    protected boolean put(String key, String value) throws RocksDBException {
        return put(new Config(key, value));
    }


    protected Config get(String key) throws RocksDBException {
        String value = db.get(key);
        Config config = null;
        if (value != null) {
            config = JsonUtils.jsonToPojo(value, Config.class);
        }

        return config;
    }


}
