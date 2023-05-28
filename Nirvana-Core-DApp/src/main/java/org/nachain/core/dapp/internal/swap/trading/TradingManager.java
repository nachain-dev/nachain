package org.nachain.core.dapp.internal.swap.trading;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.dapp.internal.swap.SwapType;
import org.nachain.core.dapp.internal.swap.trading.request.*;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class TradingManager {


    private String pairName;


    private SwapType swapType;


    private BigInteger buyTotal = BigInteger.ZERO;


    private BigInteger sellTotal = BigInteger.ZERO;


    private BigDecimal maxBuyPrice = BigDecimal.ZERO;


    private BigDecimal minSellPrice = BigDecimal.ZERO;


    private List<BuyRequest> buyList = new ArrayList<>();


    private List<SellRequest> sellList = new ArrayList<>();

    private BuyRequestDAO buyRequestDAO;
    private SellRequestDAO sellRequestDAO;
    private UnBuyRequestDAO unBuyRequestDAO;
    private UnSellRequestDAO unSellRequestDAO;

    public TradingManager(String pairName) {
        this.pairName = pairName;

        try {
            buyRequestDAO = new BuyRequestDAO();
            sellRequestDAO = new SellRequestDAO();
            unBuyRequestDAO = new UnBuyRequestDAO();
            unSellRequestDAO = new UnSellRequestDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        loadDB();
    }

    public String getPairName() {
        return pairName;
    }

    public BigInteger getBuyTotal() {
        return buyTotal;
    }

    public BigInteger getSellTotal() {
        return sellTotal;
    }

    public List<BuyRequest> getBuyList() {
        return buyList;
    }

    public List<SellRequest> getSellList() {
        return sellList;
    }


    private void loadDB() {
        try {

            List<BuyRequest> unBuyRequestAll = unBuyRequestDAO.findAll();
            for (BuyRequest buyRequest : unBuyRequestAll) {
                addBuy(buyRequest);
            }

            List<SellRequest> unSellRequestAll = unSellRequestDAO.findAll();
            for (SellRequest sellRequest : unSellRequestAll) {
                addSell(sellRequest);
            }
            log.info("Initialize UnBuyRequests={}, UnSellRequests={}", unBuyRequestAll.size(), unSellRequestAll.size());
        } catch (Exception e) {
            log.error("Initialize TradingManager error", e);
        }
    }


    public void addBuy(BuyRequest buyRequest) {

        if (!buyRequest.getPairName().equals(pairName)) {
            return;
        }


        buyList.add(buyRequest);


        TradingService.sortByBuyPrice(buyList);


        calcBuyTotal();


        if (buyRequest.getPrice().compareTo(maxBuyPrice) == 1) {
            maxBuyPrice = buyRequest.getPrice();
        }
    }


    public void removeBuy(BuyRequest buyRequest) {
        buyList.remove(buyRequest);


        calcBuyTotal();
    }


    public void addSell(SellRequest sellRequest) {

        if (!sellRequest.getPairName().equals(pairName)) {
            return;
        }


        sellList.add(sellRequest);


        TradingService.sortBySellPrice(sellList);


        calcSellTotal();


        if (sellRequest.getPrice().compareTo(maxBuyPrice) == -1) {
            minSellPrice = sellRequest.getPrice();
        }
    }


    public void removeSell(SellRequest sellRequest) {
        sellList.remove(sellRequest);


        calcSellTotal();
    }


    private void calcBuyTotal() {
        buyTotal = BigInteger.ZERO;
        for (BuyRequest buyRequest : buyList) {
            buyTotal = buyTotal.add(buyRequest.getBuyTokenAmount());
        }
    }


    private void calcSellTotal() {
        sellTotal = BigInteger.ZERO;
        for (SellRequest sellRequest : sellList) {
            sellTotal = sellTotal.add(sellRequest.getSellTokenAmount());
        }
    }


    private void matchmakingTradeoff(BuyRequest buyRequest, long blockHeight) throws RocksDBException {

        TradingType tradingType = buyRequest.getTradingType();


        switch (tradingType) {

            case BUY_LIMITED_PRICE:

                BuyRequest newBuyRequest = buyRequest;
                while (newBuyRequest != null) {
                    newBuyRequest = BUY_LIMITED_PRICE(newBuyRequest, blockHeight);
                }
                break;

            case BUY_MARKET_PRICE:

                break;
            default:
                break;
        }
    }


    private void matchmakingTradeoff(SellRequest sellRequest, long blockHeight) throws RocksDBException {

        TradingType tradingType = sellRequest.getTradingType();


        switch (tradingType) {

            case SALE_LIMITED_PRICE:

                SellRequest newSellRequest = sellRequest;
                while (newSellRequest != null) {
                    newSellRequest = SELL_LIMITED_PRICE(newSellRequest, blockHeight);
                }
                break;

            case SALE_MARKET_PRICE:

                break;
            default:
                break;
        }
    }


    private BuyRequest BUY_LIMITED_PRICE(BuyRequest buyRequest, long blockHeight) throws RocksDBException {

        BuyRequest newBuyRequest = null;


        BigDecimal buyPrice = buyRequest.getPrice();

        BigInteger buyTokenAmount = buyRequest.getBuyTokenAmount();

        BigInteger buyPayAmount = buyRequest.getPayAmount();


        for (SellRequest sellRequest : sellList) {

            BigDecimal sellPrice = sellRequest.getPrice();

            BigInteger sellTokenAmount = sellRequest.getSellTokenAmount();

            BigInteger sellEarnAmount = sellRequest.getEarnAmount();


            TradeFreeBack tradeFreeback = tradeOff(buyRequest, sellRequest, buyPrice, buyTokenAmount, buyPayAmount, sellPrice, sellTokenAmount, sellEarnAmount, blockHeight);
            if (tradeFreeback.getNewBuyRequest() != null) {
                newBuyRequest = tradeFreeback.getNewBuyRequest();
            }


            if (buyTokenAmount.compareTo(BigInteger.ZERO) == 0) {
                break;
            }
        }

        return newBuyRequest;
    }


    private SellRequest SELL_LIMITED_PRICE(SellRequest sellRequest, long blockHeight) throws RocksDBException {

        SellRequest newSellRequest = null;


        BigDecimal sellPrice = sellRequest.getPrice();

        BigInteger sellTokenAmount = sellRequest.getSellTokenAmount();

        BigInteger sellEarnAmount = sellRequest.getEarnAmount();


        for (BuyRequest buyRequest : buyList) {

            BigDecimal buyPrice = buyRequest.getPrice();

            BigInteger buyTokenAmount = buyRequest.getBuyTokenAmount();

            BigInteger buyPayAmount = buyRequest.getPayAmount();


            TradeFreeBack tradeFreeback = tradeOff(buyRequest, sellRequest, buyPrice, buyTokenAmount, buyPayAmount, sellPrice, sellTokenAmount, sellEarnAmount, blockHeight);
            if (tradeFreeback.getNewSellRequest() != null) {
                newSellRequest = tradeFreeback.getNewSellRequest();
            }


            if (sellTokenAmount.compareTo(BigInteger.ZERO) == 0) {
                break;
            }
        }

        return newSellRequest;
    }


    private TradeFreeBack tradeOff(BuyRequest buyRequest, SellRequest sellRequest, BigDecimal buyPrice, BigInteger buyTokenAmount, BigInteger buyPayAmount, BigDecimal sellPrice, BigInteger sellTokenAmount, BigInteger sellEarnAmount, long blockHeight) throws RocksDBException {
        TradeFreeBack tradeFreeback = new TradeFreeBack();


        BigInteger tradingAmount;

        if (buyPrice.compareTo(sellPrice) != -1) {

            if (buyTokenAmount.compareTo(sellTokenAmount) != -1) {

                tradingAmount = sellTokenAmount;
            } else {

                tradingAmount = buyTokenAmount;
            }

            buyTokenAmount = buyTokenAmount.subtract(tradingAmount);

            sellTokenAmount = sellTokenAmount.subtract(tradingAmount);

            BigInteger buyPay = sellPrice.multiply(new BigDecimal(tradingAmount)).toBigInteger();

            buyPayAmount = buyPayAmount.subtract(buyPay);

            sellEarnAmount = sellEarnAmount.subtract(buyPay);


            buyRequest.setTaker(true);
            buyRequest.setTradeBlockHeight(blockHeight);
            buyRequest.setTradeSellHash(sellRequest.getHash());
            buyRequest.setTradeAmount(tradingAmount);


            sellRequest.setTaker(false);
            sellRequest.setTradeBlockHeight(blockHeight);
            sellRequest.setTradeBuyHash(buyRequest.getHash());
            sellRequest.setTradeAmount(tradingAmount);


            if (buyTokenAmount.compareTo(BigInteger.ZERO) == 1) {
                BuyRequest newBuyRequest = BuyRequestService.newBuyRequest(buyRequest, buyTokenAmount, buyPayAmount, blockHeight);
                unBuyRequestDAO.add(newBuyRequest);

                buyRequest.setTradeChildHash(newBuyRequest.getHash());

                addBuy(newBuyRequest);

                tradeFreeback.setNewBuyRequest(newBuyRequest);
            }

            if (sellTokenAmount.compareTo(BigInteger.ZERO) == 1) {
                SellRequest newSellRequest = SellRequestService.newSellRequest(sellRequest, sellTokenAmount, sellEarnAmount, blockHeight);
                unSellRequestDAO.add(newSellRequest);

                sellRequest.setTradeChildHash(newSellRequest.getHash());

                addSell(newSellRequest);

                tradeFreeback.setNewSellRequest(newSellRequest);
            }


            unBuyRequestDAO.delete(buyRequest.getHash());
            buyRequestDAO.add(buyRequest);

            removeBuy(buyRequest);


            unSellRequestDAO.delete(sellRequest.getHash());
            sellRequestDAO.add(sellRequest);

            removeSell(sellRequest);


            tradeFreeback.setTradingAmount(tradingAmount);
        }

        return tradeFreeback;
    }


    private static class TradeFreeBack {


        BuyRequest newBuyRequest;


        SellRequest newSellRequest;


        BigInteger tradingAmount;

        public BuyRequest getNewBuyRequest() {
            return newBuyRequest;
        }

        public void setNewBuyRequest(BuyRequest newBuyRequest) {
            this.newBuyRequest = newBuyRequest;
        }

        public SellRequest getNewSellRequest() {
            return newSellRequest;
        }

        public void setNewSellRequest(SellRequest newSellRequest) {
            this.newSellRequest = newSellRequest;
        }

        public BigInteger getTradingAmount() {
            return tradingAmount;
        }

        public void setTradingAmount(BigInteger tradingAmount) {
            this.tradingAmount = tradingAmount;
        }

        @Override
        public String toString() {
            return "TradeFreeBack{" +
                    "newBuyRequest=" + newBuyRequest +
                    ", newSellRequest=" + newSellRequest +
                    ", tradingAmount=" + tradingAmount +
                    '}';
        }
    }
}
