package org.nachain.core.chain.config;

import org.rocksdb.RocksDBException;

import java.util.concurrent.ExecutionException;

public class MiningAfterConfigDAO extends AbstractConfigDAO {

    private final String MINING_AFTER_TASK = "MiningAfter.Task.Instance.%d";


    public MiningAfterConfigDAO(long instance) {
        super(instance);
    }

    @Override
    public String[] getRegKey() {
        return new String[]{MINING_AFTER_TASK};
    }

    private String key(long instance) {
        return String.format(MINING_AFTER_TASK, instance);
    }


    public boolean save(long instance, long blockHeight) throws RocksDBException {
        put(key(instance), String.valueOf(blockHeight));
        return true;
    }


    public long get(long instance) throws ExecutionException {
        String result = findValue(key(instance));
        if (result != null) {
            return Long.valueOf(result);
        }
        return 0;
    }


}
