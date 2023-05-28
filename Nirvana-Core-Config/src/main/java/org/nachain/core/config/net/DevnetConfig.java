package org.nachain.core.config.net;

import org.nachain.core.config.AbstractConfig;
import org.nachain.core.config.Constants;
import org.nachain.core.config.Network;

public class DevnetConfig extends AbstractConfig {

    public DevnetConfig(String dataDir) {
        super(dataDir, Network.DEVNET, Constants.DEVNET_VERSION);
    }


}
