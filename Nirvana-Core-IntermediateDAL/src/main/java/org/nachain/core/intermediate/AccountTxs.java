package org.nachain.core.intermediate;


import lombok.Data;

import java.math.BigInteger;
import java.util.List;


@Data
public class AccountTxs {


    String address;


    long tokenId;


    TxBatchType txBatchType;


    long batchID;


    List<BigInteger> txIDs;
}
