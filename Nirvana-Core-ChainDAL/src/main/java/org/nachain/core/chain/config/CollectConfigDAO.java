package org.nachain.core.chain.config;

import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class CollectConfigDAO extends AbstractConfigDAO {

    private final String COLLECT_DESTROY_NAC = "Collect.Destroy.Nac";


    public CollectConfigDAO(long instance) throws RocksDBException, IOException {
        super(instance);
    }

    @Override
    public String[] getRegKey() {
        return new String[]{COLLECT_DESTROY_NAC};
    }


    private boolean saveNacDestroy(BigInteger destroy) throws RocksDBException {
        put(COLLECT_DESTROY_NAC, destroy.toString());
        return true;
    }


    public BigInteger addNacDestroy(BigInteger destroy) throws RocksDBException, ExecutionException {
        BigInteger nacDestroy = getNacDestroy();


        nacDestroy = nacDestroy.add(destroy);


        saveNacDestroy(nacDestroy);

        return nacDestroy;
    }


    public BigInteger getNacDestroy() throws ExecutionException {
        String result = findValue(COLLECT_DESTROY_NAC);
        if (result != null) {
            return new BigInteger(result);
        }
        return BigInteger.ZERO;
    }


}
