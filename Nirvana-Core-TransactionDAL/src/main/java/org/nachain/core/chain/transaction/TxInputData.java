package org.nachain.core.chain.transaction;

import java.util.ArrayList;


public class TxInputData extends ArrayList<TxInput> {


    public static TxInputData newEmptyInputData() {
        return new TxInputData();
    }


    public static TxInputData newInputData(TxInput txInput) {
        TxInputData txInputData = new TxInputData();
        txInputData.add(txInput);
        return txInputData;
    }

}
