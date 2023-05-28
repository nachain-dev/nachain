package org.nachain.core.chain.transaction.unverifyunused;

import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.TxStatus;
import org.nachain.core.chain.transaction.unverifiedunused.UnverifiedUnusedPoolDAO;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class UnverifyUnusedService {


    public static boolean delUnverifyUsed(Tx tx) throws RocksDBException, IOException, ExecutionException {
        boolean rtv = false;
        if (tx.getStatus() == TxStatus.PENDING.value) {
            UnverifiedUnusedPoolDAO unverifyUnusedPoolDAO = new UnverifiedUnusedPoolDAO(tx.getInstance());
            rtv = unverifyUnusedPoolDAO.delUnverifyUnusedTx(tx.getTo(), tx.getHash());
        }
        return rtv;
    }
}
