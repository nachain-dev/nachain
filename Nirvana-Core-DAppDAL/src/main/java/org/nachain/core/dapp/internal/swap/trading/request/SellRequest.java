package org.nachain.core.dapp.internal.swap.trading.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.crypto.Hash;
import org.nachain.core.dapp.internal.swap.trading.AbstractSell;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


public class SellRequest extends AbstractSell {


    private String fromTx;


    private String parentRequestHash;


    private String hash;


    private boolean isTaker;


    private String tradeBuyHash;


    private BigInteger tradeAmount;


    private long tradeBlockHeight;


    private String tradeChildHash;

    public String getFromTx() {
        return fromTx;
    }

    public void setFromTx(String fromTx) {
        this.fromTx = fromTx;
    }

    public String getParentRequestHash() {
        return parentRequestHash;
    }

    public void setParentRequestHash(String parentRequestHash) {
        this.parentRequestHash = parentRequestHash;
    }


    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isTaker() {
        return isTaker;
    }

    public void setTaker(boolean taker) {
        this.isTaker = taker;
    }

    public String getTradeBuyHash() {
        return tradeBuyHash;
    }

    public void setTradeBuyHash(String tradeBuyHash) {
        this.tradeBuyHash = tradeBuyHash;
    }

    public BigInteger getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigInteger tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public long getTradeBlockHeight() {
        return tradeBlockHeight;
    }

    public void setTradeBlockHeight(long tradeBlockHeight) {
        this.tradeBlockHeight = tradeBlockHeight;
    }

    public String getTradeChildHash() {
        return tradeChildHash;
    }

    public void setTradeChildHash(String tradeChildHash) {
        this.tradeChildHash = tradeChildHash;
    }

    @Override
    public String toString() {
        return "SellRequest{" +
                "fromTx='" + fromTx + '\'' +
                ", parentRequestHash='" + parentRequestHash + '\'' +
                ", hash='" + hash + '\'' +
                ", isTaker=" + isTaker +
                ", tradeBuyHash='" + tradeBuyHash + '\'' +
                ", tradeAmount=" + tradeAmount +
                ", tradeBlockHeight=" + tradeBlockHeight +
                ", tradeChildHash='" + tradeChildHash + '\'' +
                ", tradingType=" + tradingType +
                ", swapName='" + pairName + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", sellTokenAmount=" + sellTokenAmount +
                ", earnAmount=" + earnAmount +
                ", timestamp=" + timestamp +
                '}';
    }

    public String toHashString() {
        return "SellRequest{" +
                "fromTx='" + fromTx + '\'' +
                ", parentRequestHash='" + parentRequestHash + '\'' +
                ", tradingType=" + tradingType +
                ", swapName='" + pairName + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", sellTokenAmount=" + sellTokenAmount +
                ", earnAmount=" + earnAmount +
                ", timestamp=" + timestamp +
                '}';
    }


    public byte[] encodeHash() {

        return Hash.h256(this.toHashString().getBytes());
    }


    public String encodeHashString() {
        return Hex.encode0x(encodeHash());
    }


    public static SellRequest to(String json) {
        return JsonUtils.jsonToPojo(json, SellRequest.class);
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }
}
