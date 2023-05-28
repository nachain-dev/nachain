package org.nachain.core.intermediate;

import java.util.ArrayList;

public class AccountTxsService {


    public static AccountTxs newAccountTxs(String address, long tokenId, TxBatchType txBatchType, long batchID) {
        AccountTxs accountTxs = new AccountTxs();
        accountTxs.setAddress(address);
        accountTxs.setTokenId(tokenId);
        accountTxs.setTxBatchType(txBatchType);
        accountTxs.setBatchID(batchID);
        accountTxs.setTxIDs(new ArrayList());

        return accountTxs;
    }

}
