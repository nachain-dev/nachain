package org.nachain.core.config.miner;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.config.ConfigService;
import org.nachain.core.config.KeyStoreHolder;
import org.nachain.core.config.timezone.TimeZoneService;
import org.nachain.core.crypto.Key;
import org.nachain.core.wallet.WalletUtils;

import java.util.Properties;

@Slf4j
public class MinerConfig {


    public static String API_DOMAIN;
    public static String API_DOMAIN_DEFAULT = "localhost";


    public static int API_PORT;
    public static int API_PORT_DEFAULT = 4700;


    public final static String DATA_VIEWER_DOMAIN;


    public final static int DATA_VIEWER_PORT;


    public static Key MINER_KEY;


    public static String WALLET_ADDRESS;


    public static String MINER_ADDRESS;


    public static final int MINING_WAITING_TIME = 3000;

    static {

        TimeZoneService.setDefault();


        Properties properties = ConfigService.getProperties();


        API_DOMAIN = properties.getProperty("MINER.API.DOMAIN", API_DOMAIN_DEFAULT);


        API_PORT = Integer.valueOf(properties.getProperty("MINER.API.PORT", String.valueOf(API_PORT_DEFAULT)));


        DATA_VIEWER_DOMAIN = properties.getProperty("MINER.DATA_VIEWER.DOMAIN", "localhost");


        DATA_VIEWER_PORT = Integer.valueOf(properties.getProperty("MINER.DATA_VIEWER.PORT", "7500"));


        WALLET_ADDRESS = properties.getProperty("MINER.MEMBER.WALLET_ADDRESS");


        MINER_KEY = KeyStoreHolder.getKey(WALLET_ADDRESS);


        if (MINER_KEY != null) {

            MINER_ADDRESS = WalletUtils.generateMinerAddress(MINER_KEY.getPublicKey());

            WALLET_ADDRESS = WalletUtils.generateWalletAddress(MINER_KEY.getPublicKey());
        } else {
            throw new RuntimeException("Config file error. WALLET_ADDRESS find not found \"wallet.keystore\".");
        }

        log.debug("MinerConfig initialization complete.");
    }

}
