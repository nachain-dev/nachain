package org.nachain.core.networks.p2p.iserver.websocket.factory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;


public interface IConnectTrigger {


    public void afterConnect(ChannelHandlerContext ctx, FullHttpRequest req);

}