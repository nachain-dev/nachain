package org.nachain.core.config;

import javax.management.timer.Timer;

public class Constants {


    public static final String DEFAULT_ROOT_DIR = ".";


    public static final String WALLET_FILE = "wallet.keystore";


    public static final String CONFIG_FILE = "application.properties";


    public static final String CONFIG_DEV_FILE = "application-dev.properties";


    public static final String CONFIG_DIR = ProfileService.getConfigProfile();


    public static final String CHAIN_DIR = "chaindata";


    public static final String WALLET_DIR = "wallet";


    public static final String LOG_DIR = "log";


    public static final short MAINNET_VERSION = 1;
    public static final short TESTNET_VERSION = 1;
    public static final short DEVNET_VERSION = 1;


    public static final String CLIENT_NAME = "Nirvana";


    public static final String CLIENT_VERSION = "1.2.0";


    public static final String CLIENT_VERSION_NAME = "Beta";


    public static final String HASH_ALGORITHM = "BLAKE2B-256";


    public static final String DEFAULT_TIMEZONE = "America/New_York";


    public static final int DEFAULT_P2P_PORT = 4700;


    public static final int DEFAULT_API_PORT = 14700;


    public static final String DEFAULT_USER_AGENT = "Mozilla/4.0";


    public static final int DEFAULT_CONNECT_TIMEOUT = 4000;


    public static final int DEFAULT_READ_TIMEOUT = 4000;


    public static final long DAYS_PER_YEAR = 365;


    public static final long DPoS_MINED_BLOCK_MILL = Timer.ONE_SECOND * 6;


    public static final long PoWF_MINED_BLOCK_MILL = Timer.ONE_MINUTE;


    public static final long PoWF_BLOCKS_PER_DAY = 1440;


    public static final long DPoS_BLOCKS_PER_DAY = 14400;


    public static final long DPoS_BLOCKS_PER_YEAR = DPoS_BLOCKS_PER_DAY * DAYS_PER_YEAR;


    public static final long PoWF_BLOCKS_PER_YEAR = PoWF_BLOCKS_PER_DAY * DAYS_PER_YEAR;


    public static final int SUPER_NODE_MAX_AMOUNT = 21;


    public final static String BLACK_HOLE_ADDRESS = "Na00000000000000000000000000000000";


    static {


    }

    private Constants() {
    }


}
