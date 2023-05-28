package org.nachain.core.networks.p2p.iclient;

import org.nachain.core.networks.p2p.isc.NetResources;


public class ClientService {


    public static String getClientName(NetResources netResources) {
        return getIClient(netResources).getNodeName();
    }


    public static IClient getIClient(NetResources netResources) {
        return ((IClient) netResources.getIServerClient());
    }
}