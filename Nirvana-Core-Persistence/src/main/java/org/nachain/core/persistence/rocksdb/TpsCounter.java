package org.nachain.core.persistence.rocksdb;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class TpsCounter {


    private static Map<String, Counter> counterMap;


    private static final String COLLECT_CHAIN = "COLLECT_CHAIN";


    private static final long GLOBAL_CHAIN_ID = -1;

    static {
        counterMap = Maps.newConcurrentMap();
    }


    private static String key(long groupId, String name) {
        return String.format("%d.%s", groupId, name);
    }


    public static void addCounter(long groupId, String name) {

        String key = key(groupId, name);

        Counter counter = counterMap.get(key);
        if (counter == null)
            counter = new Counter(key);

        counter.addCounter();

        counterMap.put(key, counter);


        if (!name.equals(COLLECT_CHAIN)) {

            addCounter(groupId, COLLECT_CHAIN);

            addCounter(GLOBAL_CHAIN_ID, COLLECT_CHAIN);
        }
    }


    public static long getTps(long groupId, String name) {

        String key = key(groupId, name);
        Counter counter = counterMap.get(key);
        if (counter == null) {
            return 0;
        }
        return counter.getTps();
    }


    public static long getMaxTps(long groupId, String name) {

        String key = key(groupId, name);
        Counter counter = counterMap.get(key);
        if (counter == null) {
            return 0;
        }
        return counter.getMaxTpsHistory();
    }


    public static long getGlobalChainTps(long groupId) {
        return getTps(groupId, COLLECT_CHAIN);
    }


    public static long getGlobalChainMaxTps(long groupId) {
        return getMaxTps(groupId, COLLECT_CHAIN);
    }


    public static long getGlobalChainTps() {
        return getTps(GLOBAL_CHAIN_ID, COLLECT_CHAIN);
    }


    public static long getGlobalChainMaxTps() {
        return getMaxTps(GLOBAL_CHAIN_ID, COLLECT_CHAIN);
    }


    public static class Counter {

        private String name;


        private long startCollectionTime;


        private AtomicInteger counter;


        private long tps;


        private long maxTpsHistory;

        public String getName() {
            return name;
        }

        public AtomicInteger getCounter() {
            return counter;
        }

        public long getTps() {

            calcTps();
            return tps;
        }

        public long getMaxTpsHistory() {
            return maxTpsHistory;
        }

        public Counter(String name) {
            this.name = name;
            this.counter = new AtomicInteger(0);
            this.startCollectionTime = System.currentTimeMillis();
        }


        public void addCounter() {

            counter.incrementAndGet();

            calcTps();
        }


        private void calcTps() {
            long diffTime = System.currentTimeMillis() - startCollectionTime;


            if (diffTime > 0) {
                diffTime = diffTime / 1000;
            }


            if (diffTime == 0) {
                diffTime = 1;
            }


            tps = counter.get() / diffTime;


            if (tps > maxTpsHistory) {
                maxTpsHistory = tps;
            }
        }
    }

}
