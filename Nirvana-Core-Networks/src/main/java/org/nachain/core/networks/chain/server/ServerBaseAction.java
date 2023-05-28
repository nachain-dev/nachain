package org.nachain.core.networks.chain.server;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.iserver.AbstractServerBaseAction;


@Slf4j
public class ServerBaseAction extends AbstractServerBaseAction {

    @Override
    public String getClassPath() {
        return "org.nachain.core.networks.chain.server.action";
    }

    @Override
    public void actionConnect(ChannelHandlerContext ctx, NetResources netResources) {

    }

    @Override
    public void actionDisconnect(ChannelHandlerContext ctx, NetResources netResources) {

    }

    @Override
    public void actionRead(ChannelHandlerContext ctx, NetData netData, NetResources netResources) {

    }

    @Override
    public void readComplete(ChannelHandlerContext ctx, NetResources netResources) {

    }

    @Override
    public void exception(ChannelHandlerContext ctx, Throwable cause, NetResources netResources) {
        if (cause.getMessage().equalsIgnoreCase("Connection reset")) {
            log.debug("Connection close.");
        } else {
            log.error("Netty has error, Please check.", cause);
        }
    }
}