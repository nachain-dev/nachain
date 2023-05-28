package org.nachain.core.networks.p2p.iserver;

import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.NetServerNode;


public class ServerService {


    public static String getNodeName(NetResources netResources) {
        return getNetServerNode(netResources).getNodeName();
    }


    public static NetServerNode getNetServerNode(NetResources netResources) {
        return ((IServer) netResources.getIServerClient()).getNetServerNode();
    }
}