package org.nachain.core.dapp.internal.swap.amm;

import org.nachain.core.dapp.internal.swap.PairService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AmmHolder {


    private static final Map<String, AmmManager> holder = new ConcurrentHashMap<>();


    public static AmmManager getPair(String pairName) {
        AmmManager tm = holder.get(pairName);

        if (tm == null) {
            synchronized (AmmManager.class) {
                tm = holder.get(pairName);
                if (tm == null) {

                    tm = new AmmManager(pairName);
                    holder.put(pairName, tm);
                }
            }
        }

        return tm;
    }


    public static AmmManager get(String pairName) {
        AmmManager tm = get(pairName);

        if (tm == null) {
            String reversePairName = PairService.reverse(pairName);
            tm = get(reversePairName);
        }

        return tm;
    }

}
