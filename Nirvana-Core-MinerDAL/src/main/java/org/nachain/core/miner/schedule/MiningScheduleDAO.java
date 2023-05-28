package org.nachain.core.miner.schedule;

import com.google.common.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
public class MiningScheduleDAO extends RocksDAO {


    public MiningScheduleDAO() throws RocksDBException, IOException {
        super("MiningSchedule", CoreInstanceEnum.APPCHAIN.id);
    }

    @Override
    public final CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<MiningSchedule>>() {
            public Optional<MiningSchedule> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    private String toKey(MiningSchedule miningSchedule) {
        return miningSchedule.getInstance() + "." + miningSchedule.getSchedule();
    }


    public String toKey(long instance, long schedule) {
        return instance + "." + schedule;
    }


    public MiningSchedule find(long instance, long schedule) throws RocksDBException, ExecutionException {
        String key = toKey(instance, schedule);
        return (MiningSchedule) cache.get(key).orElse(null);
    }


    public boolean put(MiningSchedule miningSchedule) throws RocksDBException {
        String key = toKey(miningSchedule);

        put(key, miningSchedule);
        return true;
    }


    public MiningSchedule get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }

        return JsonUtils.jsonToPojo(result, MiningSchedule.class);
    }

}
