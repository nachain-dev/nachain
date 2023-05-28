package org.nachain.core.chain.transaction.unverifiedunused;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.Optional;
import java.util.concurrent.ExecutionException;


public class UnverifiedUnusedPoolDAO extends RocksDAO {


    public UnverifiedUnusedPoolDAO(long instance) {
        super("UnverifiedUnusedPool", instance);
    }


    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<UnverifiedUnusedPool>>() {
            public Optional<UnverifiedUnusedPool> load(String address) throws RocksDBException {
                return Optional.ofNullable(get(address));
            }
        };
    }


    public UnverifiedUnusedPool find(String address) throws RocksDBException, ExecutionException {
        return (UnverifiedUnusedPool) cache.get(address).orElse(null);
    }


    public UnverifiedUnusedPool addUnusedTx(String walletAddress, String txHash) throws RocksDBException, ExecutionException {


        UnverifiedUnusedPool unusedPool = find(walletAddress);
        if (unusedPool == null) {
            unusedPool = new UnverifiedUnusedPool();
            unusedPool.setWalletAddress(walletAddress);
            unusedPool.setUnusedTxs(Lists.newArrayList());
        }

        unusedPool.addUnusedTx(txHash);

        put(unusedPool);

        return unusedPool;
    }


    public boolean delUnverifyUnusedTx(String walletAddress, String txHash) throws RocksDBException, ExecutionException {
        boolean isExist = false;


        UnverifiedUnusedPool unverifyUnusedPool = find(walletAddress);
        if (unverifyUnusedPool != null) {

            isExist = unverifyUnusedPool.getUnusedTxs().contains(txHash);

            if (isExist) {

                unverifyUnusedPool.removeUnusedTx(txHash);

                put(unverifyUnusedPool);
            }
        }

        return isExist;
    }


    private boolean put(UnverifiedUnusedPool unusedPool) throws RocksDBException {
        put(unusedPool.getWalletAddress(), unusedPool);
        return true;
    }


    public UnverifiedUnusedPool get(String walletAddress) throws RocksDBException {
        String result = db.get(walletAddress);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, UnverifiedUnusedPool.class);
    }

}
