package org.nachain.core.dapp.internal.swap.trading;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TradingManagerHolder {


    private static final Map<String, TradingManager> holder = new ConcurrentHashMap<>();


    public static TradingManager get(String pairName) {
        TradingManager tm = holder.get(pairName);

        if (tm == null) {
            synchronized (TradingManager.class) {
                tm = holder.get(pairName);
                if (tm == null) {
                    tm = new TradingManager(pairName);
                    holder.put(pairName, tm);
                }
            }
        }

        return tm;
    }

}
