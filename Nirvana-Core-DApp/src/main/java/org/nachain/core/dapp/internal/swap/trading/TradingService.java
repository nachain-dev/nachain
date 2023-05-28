package org.nachain.core.dapp.internal.swap.trading;

import org.nachain.core.dapp.internal.swap.trading.request.BuyRequest;
import org.nachain.core.dapp.internal.swap.trading.request.SellRequest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TradingService {


    public static void sortByBuyPrice(List<BuyRequest> buyRequests) {
        Collections.sort(buyRequests, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                BuyRequest t1 = (BuyRequest) o1;
                BuyRequest t2 = (BuyRequest) o2;
                return t2.getPrice().compareTo(t1.getPrice());
            }
        });
    }


    public static void sortBySellPrice(List<SellRequest> sellRequests) {
        Collections.sort(sellRequests, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                SellRequest t1 = (SellRequest) o1;
                SellRequest t2 = (SellRequest) o2;
                return t1.getPrice().compareTo(t2.getPrice());
            }
        });
    }

}
