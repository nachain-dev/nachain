package org.nachain.core.chain.transaction;

import org.nachain.core.config.miner.Mining;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ReflectService {


    private static final Method scheduleMethod;

    static {
        try {

            Class<?> scheduleClass = Class.forName("org.nachain.core.miner.schedule.MiningScheduleService");


            scheduleMethod = scheduleClass.getMethod("getMinerAddress", long.class, long.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getMinerAddress() {
        String address;


        long instance = Mining.getInstance();
        long blockHeight = Mining.getBlockHeight();

        try {

            address = (String) scheduleMethod.invoke(null, instance, blockHeight);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return address;
    }

}
