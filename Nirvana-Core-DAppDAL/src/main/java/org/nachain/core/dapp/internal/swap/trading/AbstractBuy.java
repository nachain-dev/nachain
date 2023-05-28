package org.nachain.core.dapp.internal.swap.trading;

import java.math.BigDecimal;
import java.math.BigInteger;


public abstract class AbstractBuy {


    protected TradingType tradingType;


    protected String pairName;


    protected String address;


    protected BigDecimal price;


    protected BigInteger buyTokenAmount;


    protected BigInteger payAmount;


    protected long timestamp;

    public TradingType getTradingType() {
        return tradingType;
    }

    public void setTradingType(TradingType tradingType) {
        this.tradingType = tradingType;
    }

    public String getPairName() {
        return pairName;
    }

    public void setPairName(String pairName) {
        this.pairName = pairName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigInteger getBuyTokenAmount() {
        return buyTokenAmount;
    }

    public void setBuyTokenAmount(BigInteger buyTokenAmount) {
        this.buyTokenAmount = buyTokenAmount;
    }

    public BigInteger getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigInteger payAmount) {
        this.payAmount = payAmount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
