package org.nachain.core.intermediate;

import lombok.Data;


@Data
public class AccountTxBatch {


    private String address;


    private long tokenId;


    private long txBatchAllTotal;


    private long txBatchInTotal;


    private long txBatchOutTotal;


    public void setTxBatchTotal(TxBatchType txBatchType, long batchID) {

        switch (txBatchType) {
            case ALL:
                setTxBatchAllTotal(batchID);
                break;
            case IN:
                setTxBatchInTotal(batchID);
                break;
            case OUT:
                setTxBatchOutTotal(batchID);
                break;
        }
    }


    public long getTxBatchTotal(TxBatchType txBatchType) {
        long batchID = 0;

        switch (txBatchType) {
            case ALL:
                batchID = getTxBatchAllTotal();
                break;
            case IN:
                batchID = getTxBatchInTotal();
                break;
            case OUT:
                batchID = getTxBatchOutTotal();
                break;
        }
        return batchID;
    }

}
