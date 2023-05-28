package org.nachain.core.subscribe.transaction;

import com.google.common.collect.Lists;
import org.rocksdb.RocksDBException;

public class TxLogService {

    private static TxLogDAO txLogDAO;

    static {
        txLogDAO = new TxLogDAO();
    }


    public static TxLog newTxLog(String txHash, long blockHeight, String eventData, String walletAddress) {
        TxLog txLog = new TxLog();
        txLog.setTxHash(txHash);
        txLog.setBlockHeight(blockHeight);
        txLog.setEventData(eventData);
        txLog.setTxLogState(TxLogState.Unprocessed);
        txLog.setMessage("");
        txLog.setWalletAddress(walletAddress);
        txLog.setRedoLog(Lists.newArrayList());
        txLog.setUndoLog(Lists.newArrayList());

        return txLog;
    }


    public static void add(TxLog txLog) {
        try {
            txLogDAO.add(txLog);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static void edit(TxLog txLog) {
        try {
            txLogDAO.edit(txLog);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean exist(String txHash) {
        try {
            return txLogDAO.get(txHash) != null;
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static TxLog get(String txHash) {
        try {
            return txLogDAO.get(txHash);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

}
