package org.nachain.libs.thread;

import java.util.concurrent.*;


public class ThreadPoolManager {


    private static int corePoolSize = 4;


    private static int maxPoolSize = Runtime.getRuntime().availableProcessors();


    private static int keepAliveTime = 1;


    private static TimeUnit timeUtilData = TimeUnit.MINUTES;


    public static ExecutorService newCacheThreadPool() {
        BlockingQueue blockingQueue = new LinkedBlockingQueue<Runnable>();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUtilData, blockingQueue);
    }


    public static ExecutorService newCacheThreadPool(int corePoolSize, int maxPoolSize, int keepAliveTime) {
        BlockingQueue blockingQueue = new LinkedBlockingQueue<Runnable>();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUtilData, blockingQueue);
    }


    public static ExecutorService newCacheThreadPool(int corePoolSize, int maxPoolSize, int keepAliveTime, TimeUnit timeUtilData) {
        BlockingQueue blockingQueue = new LinkedBlockingQueue<Runnable>();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUtilData, blockingQueue);
    }
}