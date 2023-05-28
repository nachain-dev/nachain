package org.nachain.core.dapp.internal.swap.amm;

import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

public class LiquidityPoolService {

    public static LiquidityPoolDAO liquidityPoolDAO;

    static {
        try {
            liquidityPoolDAO = new LiquidityPoolDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static LiquidityPool newLiquidityPool(String pairName) {
        LiquidityPool liquidityPool = new LiquidityPool();
        liquidityPool.setPairName(pairName);
        liquidityPool.setBaseAmount(BigInteger.ZERO);
        liquidityPool.setQuoteAmount(BigInteger.ZERO);
        liquidityPool.setProduct(BigInteger.ZERO);
        return liquidityPool;
    }


    public static void addPool(String pairName, LiquidityProvider lp) {
        try {

            LiquidityPool liquidityPool = liquidityPoolDAO.get(pairName);
            if (liquidityPool == null) {
                liquidityPool = LiquidityPoolService.newLiquidityPool(pairName);
            }
            List<String> lpList = liquidityPool.getLps();
            lpList.add(lp.getHash());

            liquidityPoolDAO.edit(liquidityPool);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static void removePool(String pairName, String hash) {
        try {

            LiquidityPool liquidityPool = liquidityPoolDAO.get(pairName);
            if (liquidityPool != null) {
                List<String> lpHashList = liquidityPool.getLps();
                Iterator<String> lpHashIter = lpHashList.iterator();
                while (lpHashIter.hasNext()) {
                    if (lpHashIter.next().equals(hash)) {
                        lpHashIter.remove();
                    }
                }

                liquidityPoolDAO.edit(liquidityPool);
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

}
