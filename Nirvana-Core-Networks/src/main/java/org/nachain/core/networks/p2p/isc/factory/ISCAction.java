package org.nachain.core.networks.p2p.isc.factory;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.networks.p2p.isc.NetResources;


public interface ISCAction {


    void connect(ChannelHandlerContext ctx, NetResources netResources);


    void disconnect(ChannelHandlerContext ctx, NetResources netResources);


    void read(ChannelHandlerContext ctx, Object msg, NetResources netResources) throws Exception;


    void readComplete(ChannelHandlerContext ctx, NetResources netResources);


    void exception(ChannelHandlerContext ctx, Throwable cause, NetResources netResources);
}