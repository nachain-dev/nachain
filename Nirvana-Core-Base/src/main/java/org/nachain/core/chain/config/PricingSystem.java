package org.nachain.core.chain.config;

import org.nachain.core.config.Constants;

import java.math.BigDecimal;

public class PricingSystem {


    public static class Slippage {

        public final static int REFERENCE_NUMBER = 10;
    }


    public static class Gas {

        public final static double GAS_POWF_USDN = 1;


        public final static double GAS_DPOS_USDN = 0.1;


        public final static double GAS_DAS_USDN = 0.01;


        public final static int GAS_FAILOVER = 10;
    }


    public static class AppChain {

        public final static int DEPLOY_INSTANCE_USDN_PRICE = 50;


        public final static int TOKEN_ICON_USDN_PRICE = 20;
    }


    public static class DeveloperStore {

        public final static int DEPLOY_PRICE_USDN = 50;
    }


    public static class DFS {

        public final static int MINIMUM_BUY_SIZE = 500;


        public final static int EACH_BUY_SIZE = 500;
    }


    public static class Domain {


        public final static int LESSOR_DEPOSIT = 100;


        public final static double LESSOR_AWARD = 0.7;


        public final static double LESSOR_GIVE_AWARD = 0.2;


        public final static double LESSOR_DESTROY_AWARD = 0.1;


        public final static int TENANT_PRICE = 100;
    }


    public static class SuperNode {

        public final static int VOTE_MINIMUM = 1;


        public static int VOTE_MAXIMUM;


        public final static int VOTE_SUBMIT_MAXIMUM = 1000;


        public final static boolean ENABLED_VOTE_MAX_LIMIT = false;


        public final static int VOTE_EACH_MULTIPLE = 1;


        public static long VOTE_CANCEL_INTERVAL;


        public final static long CANDIDATE_LOCKED_BLOCK_HEIGHT = Constants.DPoS_BLOCKS_PER_DAY * 1000;


        public final static long CANDIDATE_PLEDGE_AMOUNT = 10000;


        public final static double SUPER_NODE_REWARD = 0.03;


        public final static double VOTE_REWARD = 0.97;


        static {

            VOTE_MAXIMUM = 500;


            VOTE_CANCEL_INTERVAL = Constants.DPoS_BLOCKS_PER_DAY * 3;
        }

    }


    public static class Swap {

        public final static int SWAP_PAIR_CREATE = 50;


        public final static double LP_DIFFERENCE_PERCENT = 0.01;
    }


    public static class Destroy {

        public final static BigDecimal RATIO = BigDecimal.valueOf(0.10);


        public final static BigDecimal ALL_RATIO = RATIO.add(UninstallAward.RATIO);
    }


    public static class UninstallAward {

        public final static BigDecimal RATIO = BigDecimal.valueOf(0.05);
    }

}
