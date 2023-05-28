package org.nachain.core.chain.transaction.context;

public class TxMarkService {


    public static TxMark newTxMark() {
        TxMark txMark = new TxMark();
        txMark.setClientName("");
        txMark.setOsName("");

        return txMark;
    }
}
