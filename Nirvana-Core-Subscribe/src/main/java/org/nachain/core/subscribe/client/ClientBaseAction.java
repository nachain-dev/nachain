package org.nachain.core.subscribe.client;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.networks.p2p.iclient.AbstractClientBaseAction;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.subscribe.client.config.SyncConfig;


public class ClientBaseAction extends AbstractClientBaseAction {
    @Override
    public String getClassPath() {
        return SyncConfig.SyncAction_Path;
    }

    @Override
    public void actionConnect(ChannelHandlerContext ctx, NetResources netResources) {
    }


    @Override
    public void actionDisconnect(ChannelHandlerContext ctx, NetResources netResources) {
    }

    @Override
    public void actionRead(ChannelHandlerContext ctx, NetData netData, NetResources netResources) throws Exception {

    }

    @Override
    public void readComplete(ChannelHandlerContext ctx, NetResources netResources) {

    }


    @Override
    public void exception(ChannelHandlerContext ctx, Throwable cause, NetResources netResources) {
    }
}