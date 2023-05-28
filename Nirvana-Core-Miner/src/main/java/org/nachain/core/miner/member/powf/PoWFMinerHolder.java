package org.nachain.core.miner.member.powf;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class PoWFMinerHolder {


    private static final Map<Long, PoWFMinerManager> holder = new ConcurrentHashMap<>();


    public static PoWFMinerManager get() {
        return get(CoreInstanceEnum.APPCHAIN.id);
    }


    private static PoWFMinerManager get(long instance) {
        PoWFMinerManager mm = holder.get(instance);

        if (mm == null) {
            synchronized (holder) {
                mm = holder.get(instance);
                if (mm == null) {
                    mm = new PoWFMinerManager(instance);
                    holder.put(instance, mm);
                }
            }
        }

        return mm;
    }

}
