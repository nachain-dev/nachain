package org.nachain.core.subscribe.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.iclient.IClient;
import org.nachain.core.server.AbstractServer;
import org.nachain.core.subscribe.client.config.SyncConfig;


@Slf4j
public class SyncServer extends AbstractServer {


    IClient client;

    @Override
    public String getServerName() {
        return getClass().getSimpleName();
    }

    @Override
    public void starting() {
        try {

            client = new IClient(SyncConfig.SyncServer_Ip, SyncConfig.SyncServer_Port, new ClientBaseAction());
            client.start();

            log.debug("SyncDataClient waitReady...");


            client.waitReady();

            log.debug("SyncDataClient:" + client.getNetResources().isReady());
        } catch (Exception e) {
            log.error("SyncDataClient error:", e);
        }
    }

    @Override
    public void executes() {

        executeSleepMillis(1000 * 10);
    }


    public Channel getChanel(String nodeName) {
        Channel channel = client.getNetResources().getChannelByNodeName(nodeName);
        return channel;
    }

    public IClient getClient() {
        return client;
    }


}