package org.nachain.core.config.net;

import org.nachain.core.config.AbstractConfig;
import org.nachain.core.config.Constants;
import org.nachain.core.config.Network;

public class MainnetConfig extends AbstractConfig {

    public MainnetConfig(String dataDir) {
        super(dataDir, Network.MAINNET, Constants.MAINNET_VERSION);
    }

}