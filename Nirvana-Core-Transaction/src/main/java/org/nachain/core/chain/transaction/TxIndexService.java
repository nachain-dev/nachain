package org.nachain.core.chain.transaction;

import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;

public class TxIndexService {


    public static TxIndex newTxIndex(BigInteger id, String txHash) {
        TxIndex txIndex = new TxIndex();
        txIndex.setId(id);
        txIndex.setTxHash(txHash);

        return txIndex;
    }


    public static boolean addTxIndex(long instanceId, BigInteger txIndexId, String txHash) throws RocksDBException, IOException {

        TxIndexDAO txIndexDAO = new TxIndexDAO(instanceId);
        return txIndexDAO.put(TxIndexService.newTxIndex(txIndexId, txHash));
    }


}
