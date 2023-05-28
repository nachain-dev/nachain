package org.nachain.core.dapp.internal.swap.coinage;

import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;

public class CoinageService {

    public static CoinageDAO coinageDAO;

    static {
        try {
            coinageDAO = new CoinageDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Coinage newCoinage(String base, String quote, double buyFee, CoinagePriceType coinagePriceType, BigInteger fixedPrice) {
        Coinage coinage = new Coinage();
        coinage.setCoinageName(String.format("%s/%s", base, quote));
        coinage.setBase(base);
        coinage.setQuote(quote);
        coinage.setBaseAmount(BigInteger.ZERO);
        coinage.setQuoteAmount(BigInteger.ZERO);
        coinage.setFee(buyFee);
        coinage.setInitializationAmount(BigInteger.ZERO);
        coinage.setCoinagePriceType(coinagePriceType);
        coinage.setFixedPrice(fixedPrice);
        return coinage;
    }


}
