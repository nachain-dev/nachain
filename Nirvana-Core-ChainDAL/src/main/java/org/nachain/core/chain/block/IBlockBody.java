package org.nachain.core.chain.block;

import java.util.List;


public interface IBlockBody {


    boolean addTransaction(String txHash);


    String toMinedSignString() throws Exception;


    String toString();


    List<String> initTxs();

}
