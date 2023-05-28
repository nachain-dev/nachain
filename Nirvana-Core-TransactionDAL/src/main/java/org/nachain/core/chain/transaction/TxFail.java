package org.nachain.core.chain.transaction;

import lombok.Data;

@Data
public class TxFail {


    String causeMsg;


    Tx tx;


    long blockHeight;


    public static TxFail newTxFail(String causeMsg, Tx tx, long blockHeight) {
        TxFail txFail = new TxFail();
        txFail.setCauseMsg(causeMsg);
        txFail.setTx(tx);
        txFail.setBlockHeight(blockHeight);

        return txFail;
    }

}
