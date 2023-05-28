package org.nachain.core.chain.transaction.unverified;

import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.TxDAO;
import org.nachain.core.chain.transaction.TxStatus;
import org.nachain.core.chain.transaction.creditused.CreditUsed;
import org.nachain.core.chain.transaction.creditused.CreditUsedDAO;
import org.nachain.core.chain.transaction.unused.UnusedPoolService;
import org.nachain.core.chain.transaction.unverifiedunused.UnverifiedUnusedPoolDAO;
import org.nachain.core.crypto.Key;
import org.nachain.core.networks.BroadcastWorker;
import org.nachain.core.signverify.SignVerify;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class UnverifiedTxService {


    public static void unverifiedToMined(Tx unverifiedTx, long blockHeight, Key minerKey) throws Exception {


        if (unverifiedTx.getStatus() == TxStatus.PENDING.value) {
            TxDAO txDAO = new TxDAO(unverifiedTx.getInstance());
            UnverifiedTxDAO unverifiedTxPoolDAO = new UnverifiedTxDAO(unverifiedTx.getInstance());
            UnverifiedUnusedPoolDAO unverifyUnusedPoolDAO = new UnverifiedUnusedPoolDAO(unverifiedTx.getInstance());


            unverifiedTx.setBlockHeight(blockHeight);
            unverifiedTx.setStatus(TxStatus.COMPLETED.value);
            unverifiedTx.setMinedSign(SignVerify.signHexString(unverifiedTx, minerKey));
            txDAO.add(unverifiedTx);


            unverifiedTxPoolDAO.delete(unverifiedTx.getHash());


            CreditUsedDAO creditUsedDAO = new CreditUsedDAO(unverifiedTx.getInstance());
            CreditUsed creditUsed = creditUsedDAO.find(unverifiedTx.getHash());
            if (creditUsed != null) {

                creditUsedDAO.delete(unverifiedTx.getHash());
            } else {

                unverifyUnusedPoolDAO.delUnverifyUnusedTx(unverifiedTx.getTo(), unverifiedTx.getHash());


                UnusedPoolService.addUnusedTx(unverifiedTx.getInstance(), unverifiedTx.getTo(), unverifiedTx.getHash());
            }

        }
    }


    public static void saveUnverifiedAndBroadcast(Tx unverifiedTx) throws RocksDBException, IOException, ExecutionException {

        if (unverifiedTx.getStatus() == TxStatus.PENDING.value) {

            UnverifiedTxDAO unverifiedTxDAO = new UnverifiedTxDAO(unverifiedTx.getInstance());
            unverifiedTxDAO.put(unverifiedTx);


            UnverifiedUnusedPoolDAO unverifyUnusedPoolDAO = new UnverifiedUnusedPoolDAO(unverifiedTx.getInstance());
            unverifyUnusedPoolDAO.addUnusedTx(unverifiedTx.getTo(), unverifiedTx.getHash());


            BroadcastWorker.unverifiedTx(unverifiedTx);
        }
    }

}
