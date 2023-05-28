package org.nachain.core.dapp.internal.swap.amm;

import org.nachain.core.dapp.internal.swap.PairService;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmmService {


    public static final int SCALE = 9;


    public static final RoundingMode ROUNDING_MODE = RoundingMode.CEILING;


    public static final AmmDAO ammDAO;

    static {
        try {
            ammDAO = new AmmDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Amm newAmm(String base, String quote) {
        Amm amm = new Amm();
        amm.setPairName(PairService.toPairName(base, quote));
        amm.setBase(base);
        amm.setQuote(quote);
        amm.setBaseAmount(BigDecimal.ZERO);
        amm.setQuoteAmount(BigDecimal.ZERO);
        amm.setProduct(BigDecimal.ZERO);
        amm.setLastBasePrice(BigDecimal.ZERO);
        return amm;
    }


}
