package org.nachain.core.networks.p2p.isc.factory;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;


public interface IBaseAction {


    String getClassPath();


    void actionConnect(ChannelHandlerContext ctx, NetResources netResources);


    void actionDisconnect(ChannelHandlerContext ctx, NetResources netResources);


    void actionRead(ChannelHandlerContext ctx, NetData netData, NetResources netResources) throws Exception;

}