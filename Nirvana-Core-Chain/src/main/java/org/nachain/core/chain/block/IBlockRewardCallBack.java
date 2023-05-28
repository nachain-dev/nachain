package org.nachain.core.chain.block;

import org.nachain.core.crypto.Key;

import java.math.BigInteger;
import java.util.List;

public interface IBlockRewardCallBack {


    List<String> blockReward(long instance, long newBlockHeight, BigInteger blockReward, BigInteger gasAward, String minerAddress, Key minerKey) throws Exception;

}
