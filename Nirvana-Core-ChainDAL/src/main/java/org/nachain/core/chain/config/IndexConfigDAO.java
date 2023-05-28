package org.nachain.core.chain.config;

import org.nachain.core.chain.structure.instance.InstanceType;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class IndexConfigDAO extends AbstractConfigDAO {

    private static final String CHAIN_S_INDEX = "Chain.%s.Index";

    private static final String CHAIN_S_TYPE_S_INDEX = "Chain.%s.Type.%s.Index";


    public IndexConfigDAO() throws RocksDBException, IOException {
        super(0);
    }


    public static String toKey(IndexEnum indexEnum) {
        String key = String.format(CHAIN_S_INDEX, indexEnum);
        return key;
    }


    public static String toKey(IndexEnum indexEnum, String type) {
        String key = String.format(CHAIN_S_TYPE_S_INDEX, indexEnum, type);
        return key;
    }

    @Override
    public String[] getRegKey() {
        return new String[]{CHAIN_S_INDEX, CHAIN_S_TYPE_S_INDEX, IndexConfigDAO.toKey(IndexEnum.SWAP_QUOTE), IndexConfigDAO.toKey(IndexEnum.FULL_NODE)};
    }


    public boolean saveIndexId(IndexEnum indexEnum, long indexId) throws RocksDBException {
        String key = toKey(indexEnum);
        put(key, String.valueOf(indexId));
        return true;
    }


    public long getIndexId(IndexEnum indexEnum) throws ExecutionException {
        String key = toKey(indexEnum);
        String result = findValue(key);
        if (result != null) {
            return Long.valueOf(result);
        }


        if (IndexEnum.INSTANCE == indexEnum) {
            return -1;
        } else {
            return 0;
        }
    }


    public long nextIndexId(IndexEnum indexEnum) throws RocksDBException, ExecutionException {
        return getIndexId(indexEnum) + 1;
    }


    public boolean saveIndexId(IndexEnum indexEnum, String type, long indexId) throws RocksDBException {
        String key = toKey(indexEnum, type);
        put(key, String.valueOf(indexId));
        return true;
    }


    public long getIndexId(IndexEnum indexEnum, String type) throws ExecutionException {
        String key = toKey(indexEnum, type);
        String result = findValue(key);
        if (result != null) {
            return Long.valueOf(result);
        }


        if (IndexEnum.INSTANCE == indexEnum) {
            return -1;
        } else {
            return 0;
        }
    }


    public long getInstanceAppHeight() throws ExecutionException {
        return getIndexId(IndexEnum.INSTANCE);
    }


    public long nextInstanceAppHeight() throws ExecutionException, RocksDBException {
        return nextIndexId(IndexEnum.INSTANCE);
    }


    public boolean saveInstanceAppHeight(long instanceHeight) throws RocksDBException {
        return saveIndexId(IndexEnum.INSTANCE, instanceHeight);
    }


    public long getInstanceAppHeight(InstanceType instanceType) throws ExecutionException {
        return getIndexId(IndexEnum.INSTANCE_TYPE, instanceType.name);
    }


    public long nextInstanceAppHeight(InstanceType instanceType) throws ExecutionException {
        return getInstanceAppHeight(instanceType) + 1;
    }


    public boolean saveInstanceAppHeight(InstanceType instanceType, long instanceHeight) throws RocksDBException {
        return saveIndexId(IndexEnum.INSTANCE_TYPE, instanceType.name, instanceHeight);
    }

}
