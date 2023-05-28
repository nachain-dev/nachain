package org.nachain.core.networks.chain.client;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.iclient.AbstractClientBaseAction;
import org.nachain.core.networks.p2p.iclient.global.IClientConfig;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;


@Slf4j
public class ClientBaseAction extends AbstractClientBaseAction {

    @Override
    public String getClassPath() {
        return IClientConfig.actionPath;
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
        if (cause.getMessage().equalsIgnoreCase("Connection reset")) {
            log.debug("Connection close.");
        } else {

            log.error("Netty has error, Please check.", cause);
        }
    }
}