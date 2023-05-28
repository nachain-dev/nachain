package org.nachain.core.networks.p2p.iserver.websocket.factory;

import io.netty.channel.Channel;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.bean.Form;


public interface IAction {

    void execute(NetResources netResources, Form form, String data, Channel channel);
}