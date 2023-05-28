package org.nachain.core.miner.member.dpos;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DPoSMinerHolder {


    private static final Map<Long, DPoSMinerManager> holder = new ConcurrentHashMap<>();


    public static DPoSMinerManager get(CoreInstanceEnum instance) {
        return get(instance.id);
    }


    public static DPoSMinerManager get(long instance) {
        DPoSMinerManager mm = holder.get(instance);

        if (mm == null) {
            synchronized (DPoSMinerManager.class) {
                mm = holder.get(instance);
                if (mm == null) {
                    mm = new DPoSMinerManager(instance);
                    holder.put(instance, mm);
                }
            }
        }

        return mm;
    }
}
