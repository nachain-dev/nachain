package org.nachain.core.networks.p2p.isc.action;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;


public class ActionService {


    public static void disconnect(ChannelHandlerContext ctx, NetResources netResources) {

        String channelID = ctx.channel().id().toString();


        netResources.removeMapping(channelID);
    }


    public static void register(ChannelHandlerContext ctx, NetResources netResources, NetData netData) {

        String channelID = ctx.channel().id().toString();


        netResources.addMapping(channelID, netResources, netData);
    }


}