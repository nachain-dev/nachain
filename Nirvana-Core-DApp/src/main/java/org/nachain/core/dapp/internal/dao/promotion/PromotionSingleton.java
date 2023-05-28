package org.nachain.core.dapp.internal.dao.promotion;


public class PromotionSingleton {

    private static PromotionManager promotionManager;

    static {
        promotionManager = new PromotionManager();
    }

    public static PromotionManager get() {
        return promotionManager;
    }

}
