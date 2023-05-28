package org.nachain.core.chain.transaction.unused;

import com.google.common.collect.Lists;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.TxService;
import org.nachain.core.chain.transaction.TxStatus;
import org.rocksdb.RocksDBException;

import java.util.concurrent.ExecutionException;

public class UnusedPoolService {


    public static UnusedPool newUnusedPool(String walletAddress) {
        UnusedPool unusedPool = new UnusedPool();
        unusedPool.setWalletAddress(walletAddress);
        unusedPool.setUnusedTxs(Lists.newArrayList());

        return unusedPool;
    }


    public static UnusedPool addUnusedTx(long instance, String walletAddress, String txHash) throws RocksDBException, ExecutionException {
        UnusedPoolDAO unusedPoolDAO = new UnusedPoolDAO(instance);


        if (TxService.excludeAddress.contains(walletAddress)) {
            return null;
        }


        UnusedPool unusedPool = unusedPoolDAO.find(walletAddress);
        if (unusedPool == null) {
            unusedPool = UnusedPoolService.newUnusedPool(walletAddress);
        }


        unusedPool.addUnusedTx(txHash);


        unusedPoolDAO.put(unusedPool);

        return unusedPool;
    }


    public static UnusedPool getUnusedPool(long instance, String walletAddress) throws ExecutionException {
        UnusedPoolDAO unusedPoolDAO = new UnusedPoolDAO(instance);
        return unusedPoolDAO.find(walletAddress);
    }


    public static boolean delUnusedTx(long instance, String walletAddress, String txHash) throws RocksDBException, ExecutionException {
        UnusedPoolDAO unusedPoolDAO = new UnusedPoolDAO(instance);
        UnusedPool unusedPool = unusedPoolDAO.find(walletAddress);


        boolean isExist = unusedPool.getUnusedTxs().contains(txHash);


        if (isExist) {

            unusedPool.removeUnusedTx(txHash);

            unusedPoolDAO.put(unusedPool);
        }

        return isExist;
    }


    public static boolean delUsed(Tx tx) throws RocksDBException, ExecutionException {
        boolean rtv = false;
        if (tx.getStatus() == TxStatus.COMPLETED.value) {
            rtv = delUnusedTx(tx.getInstance(), tx.getTo(), tx.getHash());
        }
        return rtv;
    }

}
