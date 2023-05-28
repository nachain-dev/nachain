package org.nachain.core.config.net;

import org.nachain.core.config.AbstractConfig;
import org.nachain.core.config.Constants;
import org.nachain.core.config.Network;

public class TestnetConfig extends AbstractConfig {

    public TestnetConfig(String dataDir) {
        super(dataDir, Network.TESTNET, Constants.TESTNET_VERSION);
    }

}
