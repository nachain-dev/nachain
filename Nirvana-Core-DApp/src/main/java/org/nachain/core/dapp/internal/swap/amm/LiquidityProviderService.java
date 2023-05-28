package org.nachain.core.dapp.internal.swap.amm;

import org.nachain.core.util.CommUtils;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;

public class LiquidityProviderService {

    public static LiquidityProviderDAO liquidityProviderDAO;

    static {
        try {
            liquidityProviderDAO = new LiquidityProviderDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static LiquidityProvider newLiquidityProvider(String pairName, String providerAddress, BigInteger baseAmount, BigInteger quoteAmount) {
        LiquidityProvider lp = new LiquidityProvider();
        lp.setPairName(pairName);
        lp.setProviderAddress(providerAddress);
        lp.setBaseAmount(baseAmount);
        lp.setQuoteAmount(quoteAmount);
        lp.setTimestamp(CommUtils.currentTimeMillis());
        lp.setHash(lp.encodeHashString());

        return lp;
    }


    public static LiquidityProvider toLiquidityProvider(String json) {
        return JsonUtils.jsonToPojo(json, LiquidityProvider.class);
    }

}
