package org.nachain.core.chain.transaction.recycle;

import org.nachain.core.chain.transaction.*;
import org.nachain.core.chain.transaction.creditused.CreditUsedDAO;
import org.nachain.core.chain.transaction.unused.UnusedPoolDAO;
import org.nachain.core.chain.transaction.unused.UnusedPoolService;
import org.nachain.core.chain.transaction.unverified.UnverifiedTxDAO;
import org.nachain.core.chain.transaction.unverifiedunused.UnverifiedUnusedPoolDAO;
import org.nachain.core.util.JsonUtils;

public class RecycleTxService {

    private static RecycleTxCleanerServer recycleTxCleanerServer;


    public static RecycleTxCleanerServer getServer() {
        return recycleTxCleanerServer;
    }


    public static RecycleTxCleanerServer startServer() {

        if (recycleTxCleanerServer == null) {
            recycleTxCleanerServer = new RecycleTxCleanerServer();
        }

        if (recycleTxCleanerServer.isRun() && !recycleTxCleanerServer.isEnd()) {
            throw new RuntimeException("RecycleTxCleanerServer is already running, do not start again");
        }

        recycleTxCleanerServer.startServer();

        return recycleTxCleanerServer;
    }


    public static void stopServer() {
        if (recycleTxCleanerServer != null) {
            recycleTxCleanerServer.stopServer();
        }
    }


    public static RecycleTx toRecycleTx(String json) {
        return JsonUtils.jsonToPojo(json, RecycleTx.class);
    }


    public static void doRecycleTx(Tx unvTx, long blockHeight, String minerWallet, String cause) throws Exception {
        long instance = unvTx.getInstance();

        TxDAO txDAO = new TxDAO(instance);
        UnverifiedTxDAO unverifiedTxDAO = new UnverifiedTxDAO(instance);
        UnusedPoolDAO unusedPoolDAO = new UnusedPoolDAO(instance);
        RecycleTxDAO recycleTxDAO = new RecycleTxDAO();
        UnverifiedUnusedPoolDAO unverifyUnusedPoolDAO = new UnverifiedUnusedPoolDAO(instance);
        CreditUsedDAO creditUsedDAO = new CreditUsedDAO(instance);


        RecycleTx recycleTx = RecycleTxService.newRecycleTx(blockHeight, minerWallet, cause, unvTx);
        recycleTxDAO.add(recycleTx);


        unverifiedTxDAO.delete(unvTx.getHash());


        unverifyUnusedPoolDAO.delUnverifyUnusedTx(unvTx.getTo(), unvTx.getHash());


        if (unvTx.getTxType() == TxType.TRANSFER_CROSS_IN.value) {

        }


        for (TxInput txInput : unvTx.getInputData()) {
            Tx inputTx = txDAO.find(txInput.getTx());


            if (inputTx == null) {
                continue;
            }


            if (inputTx.getBlockHeight() == 0) {

                unverifyUnusedPoolDAO.addUnusedTx(inputTx.getTo(), inputTx.getHash());

                creditUsedDAO.delete(inputTx.getHash());
            } else {

                UnusedPoolService.addUnusedTx(inputTx.getInstance(), inputTx.getTo(), inputTx.getHash());
            }


            Tx changeTx = TxService.getTx(instance, inputTx.getChangeTx());
            if (changeTx != null) {
                unverifiedTxDAO.delete(changeTx.getHash());
            }
        }
    }


    public static RecycleTx newRecycleTx(long blockHeight, String miner, String cause, Tx tx) throws Exception {
        RecycleTx rTx = new RecycleTx();
        rTx.setBlockHeight(blockHeight);
        rTx.setMiner(miner);
        rTx.setCause(cause);
        rTx.setTx(tx);
        rTx.setHash(rTx.encodeHashString());

        return rTx;
    }


}
