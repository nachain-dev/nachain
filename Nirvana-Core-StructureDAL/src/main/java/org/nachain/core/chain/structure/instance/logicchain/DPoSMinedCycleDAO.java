package org.nachain.core.chain.structure.instance.logicchain;

import com.google.common.cache.CacheLoader;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class DPoSMinedCycleDAO extends RocksDAO {


    public DPoSMinedCycleDAO(long instance) {
        super("DPoSMinedCycle", instance);
    }


    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<DPoSMinedCycle>>() {
            public Optional<DPoSMinedCycle> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public DPoSMinedCycle find(String minerAddress, long blockCycle) throws ExecutionException {
        return find(toKey(minerAddress, blockCycle));
    }


    public DPoSMinedCycle find(String key) throws ExecutionException {
        return (DPoSMinedCycle) cache.get(key).orElse(null);
    }


    public boolean add(DPoSMinedCycle minedCycle) throws RocksDBException, ExecutionException {
        DPoSMinedCycle result = find(minedCycle.toKeyString());

        if (result != null) {
            return false;
        }
        put(minedCycle.toKeyString(), minedCycle);
        return true;
    }


    public boolean put(DPoSMinedCycle minedCycle) throws RocksDBException {
        put(minedCycle.toKeyString(), minedCycle);
        return true;
    }


    public DPoSMinedCycle get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, DPoSMinedCycle.class);
    }


    public List<DPoSMinedCycle> findList(List<String> keyList) throws ExecutionException {
        List<DPoSMinedCycle> dmList = new ArrayList<>();
        for (String key : keyList) {
            DPoSMinedCycle dm = find(key);

            if (dm != null) {
                dmList.add(dm);
            }
        }

        return dmList;
    }


    public String toKey(String minerAddress, long blockCycle) {
        return minerAddress + "_" + blockCycle;
    }


    public String toKey(DPoSMinedCycle dPoSMined) {
        return dPoSMined.getMinerAddress() + "_" + dPoSMined.getSchedule();
    }
}
