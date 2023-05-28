package org.nachain.core.dapp.internal.swap.amm;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.config.PricingSystem;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.dapp.internal.swap.PairService;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigDecimal;


@Slf4j
public class AmmManager {


    private String pairName;
    private String base;
    private String quote;
    private BigDecimal baseAmount = BigDecimal.ZERO;
    private BigDecimal quoteAmount = BigDecimal.ZERO;

    private BigDecimal product = BigDecimal.ZERO;

    private BigDecimal lastBasePrice = BigDecimal.ZERO;


    private Amm amm;


    private LiquidityPool liquidityPool;

    public AmmManager(Amm amm) {
        this.pairName = amm.getPairName();
        this.amm = amm;

        init();
    }

    public AmmManager(String pairName) {
        this.pairName = pairName;
        try {

            this.amm = AmmService.ammDAO.get(pairName);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }

        init();
    }

    @Override
    public String toString() {
        return "AmmManager{" +
                "pairName='" + pairName + '\'' +
                ", amm=" + amm +
                ", base='" + base + '\'' +
                ", baseAmount=" + baseAmount +
                ", lastBasePrice=" + lastBasePrice +
                ", quote='" + quote + '\'' +
                ", quoteAmount=" + quoteAmount +
                ", product=" + product +
                '}';
    }


    private void init() {

        LiquidityPoolDAO liquidityPoolDAO;
        try {
            liquidityPoolDAO = new LiquidityPoolDAO();
            liquidityPool = liquidityPoolDAO.get(pairName);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if (amm == null) {
            base = PairService.getBaseName(pairName);
            quote = PairService.getQuoteName(pairName);


            if (liquidityPool != null) {
                baseAmount = new BigDecimal(liquidityPool.getBaseAmount());
                quoteAmount = new BigDecimal(liquidityPool.getQuoteAmount());
            } else {
                baseAmount = BigDecimal.ZERO;
                quoteAmount = BigDecimal.ZERO;
            }

            if (product.compareTo(BigDecimal.ZERO) == 0) {
                calcProduct();
            }


            try {
                AmmService.ammDAO.add(getAmm());
            } catch (RocksDBException e) {
                throw new RuntimeException(e);
            }
        } else {
            base = amm.getBase();
            baseAmount = amm.getBaseAmount();
            lastBasePrice = amm.getLastBasePrice();
            quote = amm.getQuote();
            quoteAmount = amm.getQuoteAmount();
            product = amm.getProduct();
        }
    }


    public void saveAmm() {

        try {
            AmmService.ammDAO.edit(getAmm());
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public Amm getAmm() {
        if (amm == null) {
            amm = AmmService.newAmm(base, quote);
        }
        amm.setPairName(pairName);
        amm.setBase(base);
        amm.setBaseAmount(baseAmount);
        amm.setLastBasePrice(lastBasePrice);
        amm.setQuote(quote);
        amm.setQuoteAmount(quoteAmount);
        amm.setProduct(product);
        return amm;
    }


    public LiquidityPool getLiquidityPool() {
        return liquidityPool;
    }


    public void addLiquidity(LiquidityProvider liquidityProvider) {
        BigDecimal baseAmount = new BigDecimal(liquidityProvider.getBaseAmount());
        BigDecimal quoteAmount = new BigDecimal(liquidityProvider.getQuoteAmount());


        if (baseAmount.compareTo(BigDecimal.ZERO) != 1) {
            throw new ChainException(String.format("Add LP baseAmount cannot be zero"));
        }
        if (quoteAmount.compareTo(BigDecimal.ZERO) != 1) {
            throw new ChainException(String.format("Add LP quoteAmount cannot be zero"));
        }

        BigDecimal basePrice = quoteAmount.divide(baseAmount, AmmService.SCALE, AmmService.ROUNDING_MODE);


        if (lastBasePrice.compareTo(BigDecimal.ZERO) == 1) {

            BigDecimal differencePrice = lastBasePrice.subtract(basePrice).abs();
            if (differencePrice.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal differencePercent = differencePrice.divide(lastBasePrice, AmmService.SCALE, AmmService.ROUNDING_MODE);
                if (differencePercent.compareTo(BigDecimal.valueOf(PricingSystem.Swap.LP_DIFFERENCE_PERCENT)) == 1) {
                    throw new ChainException(String.format("Add LP pool, price fluctuation %f more than %f", differencePercent, PricingSystem.Swap.LP_DIFFERENCE_PERCENT));
                }
                log.info("Add LP, lastBasePrice={}, differencePrice={}, differencePrice={}, differencePercent={}, baseAmount={}, quoteAmount={}", lastBasePrice, basePrice, differencePrice, differencePercent, baseAmount, quoteAmount);
            }
        }


        this.baseAmount = this.baseAmount.add(baseAmount);
        this.quoteAmount = this.quoteAmount.add(quoteAmount);


        calcProduct();


        try {
            LiquidityProviderService.liquidityProviderDAO.add(liquidityProvider);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }


        LiquidityPoolService.addPool(pairName, liquidityProvider);


        saveAmm();
    }


    public void removeLiquidity(LiquidityProvider liquidityProvider) {


        throw new ChainException(String.format("Can't remove liquidity provider."));

    }


    private void calcProduct() {

        product = baseAmount.multiply(quoteAmount);

        if (baseAmount.compareTo(BigDecimal.ZERO) == 1) {
            lastBasePrice = quoteAmount.divide(baseAmount, AmmService.SCALE, AmmService.ROUNDING_MODE);
        }

        log.info("Product:{}, {} New Price:{}, Base:{}, Quote:{}", product, base, lastBasePrice, baseAmount, quoteAmount);
    }


    private boolean verifyProduct() {

        if (baseAmount.compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }


        BigDecimal checkProduct = baseAmount.multiply(quoteAmount);

        log.debug("checkProduct={}, product={}, baseAmount={}, quoteAmount={}", checkProduct, product, baseAmount, quoteAmount);

        return product.compareTo(checkProduct) == 0;
    }


    public BigDecimal calcBuyBaseAmount(BigDecimal payQuoteAmount) {

        if (product.compareTo(BigDecimal.ZERO) == 0) {
            calcProduct();
        }

        BigDecimal buyBaseAmount;


        buyBaseAmount = baseAmount.subtract(product.divide(quoteAmount.add(payQuoteAmount), AmmService.SCALE, AmmService.ROUNDING_MODE));


        return buyBaseAmount;
    }


    public BigDecimal calcEarnQuoteAmount(BigDecimal sellBaseAmount) {

        if (product.compareTo(BigDecimal.ZERO) == 0) {
            calcProduct();
        }

        BigDecimal earnQuoteAmount;


        earnQuoteAmount = quoteAmount.subtract(product.divide(baseAmount.add(sellBaseAmount), AmmService.SCALE, AmmService.ROUNDING_MODE));


        return earnQuoteAmount;
    }


    public BigDecimal calcBuyQuoteAmount(BigDecimal buyBaseAmount) {
        BigDecimal needPayQuote = BigDecimal.ZERO;


        if (product.compareTo(BigDecimal.ZERO) == 0) {
            calcProduct();
        }


        if (buyBaseAmount.compareTo(BigDecimal.ZERO) != 1) {
            return BigDecimal.ZERO;
        }


        if (baseAmount.compareTo(buyBaseAmount) == -1) {
            return BigDecimal.ZERO;
        }


        BigDecimal tradeBalance = baseAmount.subtract(buyBaseAmount);
        BigDecimal quoteAmountTotal = BigDecimal.ZERO;

        if (tradeBalance.compareTo(BigDecimal.ZERO) == 1) {

            quoteAmountTotal = product.divide(tradeBalance, AmmService.SCALE, AmmService.ROUNDING_MODE);


            needPayQuote = quoteAmountTotal.subtract(quoteAmount);
        } else if (tradeBalance.compareTo(BigDecimal.ZERO) == 0) {
            quoteAmountTotal = product;

            needPayQuote = quoteAmountTotal;
        }


        return needPayQuote;
    }


    public BigDecimal calcSellQuoteAmount(BigDecimal sellBaseAmount) {
        BigDecimal earnQuoteAmount = BigDecimal.ZERO;


        if (product.compareTo(BigDecimal.ZERO) == 0) {
            calcProduct();
        }


        if (sellBaseAmount.compareTo(BigDecimal.ZERO) != 1) {
            return BigDecimal.ZERO;
        }


        if (quoteAmount.compareTo(BigDecimal.ZERO) != 1) {
            return BigDecimal.ZERO;
        }


        BigDecimal tradeBalance = baseAmount.add(sellBaseAmount);

        if (tradeBalance.compareTo(BigDecimal.ZERO) == 1) {

            BigDecimal quoteAmountTotal = product.divide(tradeBalance, AmmService.SCALE, AmmService.ROUNDING_MODE);

            earnQuoteAmount = quoteAmount.subtract(quoteAmountTotal);
        }


        return earnQuoteAmount;
    }


    public BigDecimal buyBase(BigDecimal payQuoteAmount, BigDecimal buyBaseAmount, double slippage) {

        if (product.compareTo(BigDecimal.ZERO) == 0) {
            calcProduct();
        }


        if (payQuoteAmount.compareTo(BigDecimal.ZERO) != 1) {
            throw new ChainException(String.format("Pay quoteAmount cannot be zero"));
        }


        if (buyBaseAmount.compareTo(BigDecimal.ZERO) != 1) {
            throw new ChainException(String.format("Buy baseAmount cannot be zero"));
        }


        if (baseAmount.compareTo(buyBaseAmount) == -1) {
            throw new ChainException(String.format("Buy baseAmount greater than balance, baseAmount=%s, buyBaseAmount=%s", baseAmount, buyBaseAmount));
        }


        BigDecimal tradeAmount = calcBuyBaseAmount(payQuoteAmount);


        BigDecimal willPay = calcBuyQuoteAmount(tradeAmount);
        if (willPay.compareTo(BigDecimal.ZERO) == 0) {
            throw new ChainException(String.format("Pay quoteAmount cannot be zero"));
        }


        if (slippage > 0) {
            log.debug("buyBaseAmount:" + buyBaseAmount + ", tradeAmount:" + tradeAmount);

            if (buyBaseAmount.compareTo(tradeAmount) == 1) {

                BigDecimal differenceAmount = buyBaseAmount.subtract(tradeAmount);

                BigDecimal differencePercent = differenceAmount.divide(buyBaseAmount, AmmService.SCALE, AmmService.ROUNDING_MODE);
                log.debug("differencePercent:" + differencePercent);

                if (BigDecimal.valueOf(slippage).compareTo(differencePercent) == -1) {
                    throw new ChainException(String.format("The slippage value is exceeded. slippage=%f, differencePercent=%f", slippage, differencePercent));
                }
            }
        } else if (slippage == 0 && payQuoteAmount.compareTo(willPay) != -1) {
            throw new ChainException(String.format("Pay quoteAmount is wrong"));
        }


        baseAmount = baseAmount.subtract(tradeAmount);


        lastBasePrice = willPay.divide(tradeAmount, AmmService.SCALE, AmmService.ROUNDING_MODE);


        quoteAmount = quoteAmount.add(payQuoteAmount);


        if (!verifyProduct()) {
            log.debug(String.format("Verify product, To do calcProduct(). Product=%s, CalcProduct=%s, baseAmount=%s, quoteAmount=%s", product, baseAmount.multiply(quoteAmount), baseAmount, quoteAmount));

            calcProduct();
        }

        log.info("Buy AMM-> {}", this.toString());
        log.info("Pay {} {}, Buy:{} {}, Product:{}, {} New Price:{}, {} Base Balance:{} ,{} Quote Balance:{}", payQuoteAmount, quote, tradeAmount, base, product, base, lastBasePrice, base, baseAmount, quote, quoteAmount);


        saveAmm();

        return tradeAmount;
    }


    public BigDecimal sellBase(BigDecimal earnQuoteAmount, BigDecimal sellBaseAmount, double slippage) {

        if (product.compareTo(BigDecimal.ZERO) == 0) {
            calcProduct();
        }


        if (earnQuoteAmount.compareTo(BigDecimal.ZERO) != 1) {
            throw new ChainException(String.format("Earn quoteAmount cannot be zero"));
        }


        if (sellBaseAmount.compareTo(BigDecimal.ZERO) != 1) {
            throw new ChainException(String.format("Sell baseAmount cannot be zero"));
        }


        if (quoteAmount.compareTo(earnQuoteAmount) == -1) {
            throw new ChainException(String.format("Buy baseAmount greater than balance, quoteAmount=%s, earnQuoteAmount=%s", quoteAmount, earnQuoteAmount));
        }


        BigDecimal quoteAmount = calcSellQuoteAmount(sellBaseAmount);


        if (slippage > 0) {
            log.debug("sellBaseAmount:" + sellBaseAmount + ", quoteAmount:" + quoteAmount);

            if (earnQuoteAmount.compareTo(quoteAmount) == 1) {

                BigDecimal differenceAmount = earnQuoteAmount.subtract(quoteAmount);

                BigDecimal differencePercent = differenceAmount.divide(earnQuoteAmount, AmmService.SCALE, AmmService.ROUNDING_MODE);
                log.debug("differencePercent:" + differencePercent);

                if (BigDecimal.valueOf(slippage).compareTo(differencePercent) == -1) {
                    throw new ChainException(String.format("The slippage value is exceeded"));
                }
            }
        }


        baseAmount = baseAmount.add(sellBaseAmount);


        lastBasePrice = quoteAmount.divide(sellBaseAmount, AmmService.SCALE, AmmService.ROUNDING_MODE);


        this.quoteAmount = this.quoteAmount.subtract(earnQuoteAmount);


        if (!verifyProduct()) {
            log.debug(String.format("Verify product, To do calcProduct(). Product=%s, CalcProduct=%s, baseAmount=%s, quoteAmount=%s", product, baseAmount.multiply(quoteAmount), baseAmount, quoteAmount));

            calcProduct();
        }

        log.info("Sell AMM-> {}", this.toString());
        log.info("Pay{} {}, Buy:{} {}, Product:{}, {} New Price:{}, {}Base Balance:{},{}Quote Balance:{}", earnQuoteAmount, quote, quoteAmount, base, product, base, lastBasePrice, base, baseAmount, quote, quoteAmount);


        saveAmm();

        return quoteAmount;
    }


}
