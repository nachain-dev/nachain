package org.nachain.core.chain.config;

import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class CollectConfigService {

    private static CollectConfigDAO collectConfigDAO;

    static {
        try {

            final long instanceId = 0;
            collectConfigDAO = new CollectConfigDAO(instanceId);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void addNacDestroy(BigInteger nacDestroy) {
        try {
            collectConfigDAO.addNacDestroy(nacDestroy);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public static BigInteger getNacDestroy() {
        try {
            return collectConfigDAO.getNacDestroy();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
