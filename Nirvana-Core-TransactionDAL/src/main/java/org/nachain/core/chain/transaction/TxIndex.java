package org.nachain.core.chain.transaction;

import lombok.Data;

import java.math.BigInteger;


@Data
public class TxIndex {

    BigInteger id;


    String txHash;


    String output;
}
