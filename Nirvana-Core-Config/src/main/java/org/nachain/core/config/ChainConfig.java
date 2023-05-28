package org.nachain.core.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.config.timezone.TimeZoneService;

import java.io.File;
import java.util.List;
import java.util.Properties;

@Slf4j
public class ChainConfig {


    public final static String APP_VERSION;


    public final static int BLOCK_VERSION;


    public final static int TX_VERSION;


    public final static int DAS_TX_VERSION;


    public final static int BLOCK_MAX_TXS;


    public final static String DATA_CENTER;


    public final static String WALLET_PASSWORD;


    public final static String GENESIS_WALLET_ADDRESS;


    public final static int MAX_TX_BLOCK_CONDITION_PER_ADDRESS;


    public final static String DATA_PATH;


    public static final String WALLET_KEYSTORE_PATH;


    public final static long EMPTY_BLOCK_SIZE = 2;


    public final static boolean ENABLED_NODE_SYNERGIA = false;


    public final static boolean ENABLED_SLACK_AUDIT_POLICY = false;


    public final static boolean ENABLED_SLACK_AUDIT_TX_HEIGHT = true;


    public final static List<String> FORKS = Lists.newArrayList(

            "0", "0", "false", "0x0000",

            "1", "0", "false", "0x0000"
    );


    public static String NODE_GATEWAY_DOMAIN;


    public static String ORACLE_GATEWAY_DOMAIN;

    static {

        TimeZoneService.setDefault();


        APP_VERSION = Constants.CLIENT_NAME + " Chain(" + Constants.CLIENT_VERSION + " " + Constants.CLIENT_VERSION_NAME + ")";


        BLOCK_VERSION = 0x02;


        TX_VERSION = 0x02;


        DAS_TX_VERSION = 0x02;


        BLOCK_MAX_TXS = 6 * 5000;


        GENESIS_WALLET_ADDRESS = "NacqYpEAwb6ojnE8KrHQjnP6J5LUq3DuZF";


        MAX_TX_BLOCK_CONDITION_PER_ADDRESS = 100;

        Properties properties = null;
        try {
            properties = ConfigService.getProperties();
        } catch (Exception e) {
            log.error("Load config error", e);
        } finally {


            if (properties == null) {
                properties = new Properties();
                properties.put("CHAIN.DATA.PATH", "Nirvana-Data");
                properties.put("CHAIN.EXTRA_DATA.DATACENTER", "The Citadel");
                properties.put("WALLET.PASSWORD", "");
            }
        }


        DATA_PATH = properties.getProperty("CHAIN.DATA.PATH");


        DATA_CENTER = properties.getProperty("CHAIN.EXTRA_DATA.DATACENTER");


        WALLET_PASSWORD = properties.getProperty("WALLET.PASSWORD");


        WALLET_KEYSTORE_PATH = DATA_PATH + File.separator + Constants.WALLET_DIR + File.separator + Constants.WALLET_FILE;


        NODE_GATEWAY_DOMAIN = properties.getProperty("CHAIN.NODE.GATEWAY.DOMAIN");


        ORACLE_GATEWAY_DOMAIN = properties.getProperty("CHAIN.ORACLE.GATEWAY.DOMAIN");
    }

}
