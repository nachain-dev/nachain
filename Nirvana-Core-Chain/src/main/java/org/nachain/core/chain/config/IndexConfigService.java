package org.nachain.core.chain.config;

import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class IndexConfigService {

    private static final String CHAIN_S_INDEX = "Chain.%s.Index";

    private static IndexConfigDAO indexConfigDAO;

    static {
        try {
            indexConfigDAO = new IndexConfigDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String toKey(IndexEnum indexEnum) {
        String key = String.format(CHAIN_S_INDEX, indexEnum);
        return key;
    }


    public static IndexConfigDAO getDAO() {
        return indexConfigDAO;
    }


    public static long nextInstanceAppHeight() throws ExecutionException {
        return indexConfigDAO.getInstanceAppHeight() + 1;
    }

}
