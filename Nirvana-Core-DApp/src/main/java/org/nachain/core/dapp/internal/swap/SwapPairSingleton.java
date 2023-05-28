package org.nachain.core.dapp.internal.swap;


public class SwapPairSingleton {

    private static SwapPairManager swapPairManager;

    static {
        swapPairManager = new SwapPairManager();
    }

    public static SwapPairManager get() {
        return swapPairManager;
    }

}
