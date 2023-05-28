package org.nachain.core.networks.p2p.isc.factory;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;


public interface IAction {

    void execute(ChannelHandlerContext ctx, NetData netData, NetResources netResources);

}