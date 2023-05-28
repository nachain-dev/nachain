package org.nachain.core.dapp.internal.swap.trading.request;

import org.nachain.core.util.CommUtils;

import java.math.BigInteger;

public class SellRequestService {


    public static SellRequest newSellRequest(SellRequest sellRequest, BigInteger sellTokenAmount, BigInteger earnAmount, long tradeBlockHeight) {
        SellRequest newSellRequest = new SellRequest();

        newSellRequest.setTradingType(sellRequest.getTradingType());

        newSellRequest.setPairName(sellRequest.getPairName());

        newSellRequest.setAddress(sellRequest.getAddress());

        newSellRequest.setPrice(sellRequest.getPrice());

        newSellRequest.setSellTokenAmount(sellTokenAmount);

        newSellRequest.setEarnAmount(earnAmount);

        newSellRequest.setTimestamp(CommUtils.currentTimeMillis());

        newSellRequest.setFromTx("");

        newSellRequest.setParentRequestHash(sellRequest.getHash());

        newSellRequest.setHash(newSellRequest.encodeHashString());

        newSellRequest.setTaker(false);

        newSellRequest.setTradeBuyHash("");

        newSellRequest.setTradeBlockHeight(tradeBlockHeight);

        return newSellRequest;
    }
}
