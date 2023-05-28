package org.nachain.core.networks.p2p.iclient;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ConnectionListener implements ChannelFutureListener {


    private IClient iClient;

    public ConnectionListener(IClient client) {
        this.iClient = client;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {

        if (!channelFuture.isSuccess()) {

            log.debug("The client reconnects");

            iClient.notRun();
            iClient.run();
        }
    }
}