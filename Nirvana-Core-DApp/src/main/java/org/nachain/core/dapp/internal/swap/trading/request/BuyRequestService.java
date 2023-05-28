package org.nachain.core.dapp.internal.swap.trading.request;

import org.nachain.core.util.CommUtils;

import java.math.BigInteger;

public class BuyRequestService {


    public static BuyRequest newBuyRequest(BuyRequest buyRequest, BigInteger buyTokenAmount, BigInteger buyPayAmount, long tradeBlockHeight) {
        BuyRequest newBuyRequest = new BuyRequest();

        newBuyRequest.setTradingType(buyRequest.getTradingType());

        newBuyRequest.setPairName(buyRequest.getPairName());

        newBuyRequest.setAddress(buyRequest.getAddress());

        newBuyRequest.setPrice(buyRequest.getPrice());

        newBuyRequest.setBuyTokenAmount(buyTokenAmount);

        newBuyRequest.setPayAmount(buyPayAmount);

        newBuyRequest.setTimestamp(CommUtils.currentTimeMillis());

        newBuyRequest.setFromTx("");

        newBuyRequest.setParentRequestHash(buyRequest.getHash());

        newBuyRequest.setHash(newBuyRequest.encodeHashString());

        newBuyRequest.setTaker(false);

        newBuyRequest.setTradeSellHash("");

        newBuyRequest.setTradeBlockHeight(tradeBlockHeight);

        return newBuyRequest;
    }
}
