package org.nachain.core.networks.chain.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.iclient.IClient;
import org.nachain.core.networks.p2p.isc.SenderService;


@Slf4j
public class SyncDataClient {


    String serverIp = "127.0.0.1";

    int serverPort = 4701;

    IClient client;

    public void start() {
        try {

            client = new IClient(serverIp, serverPort, new ClientBaseAction());
            client.start();

            log.debug("SyncDataClient waitReady...");


            client.waitReady();

            log.debug("SyncDataClient:" + client.getNetResources().isReady());
        } catch (Exception e) {
            log.error("SyncDataClient error:", e);
        }
    }


    public void getInstanceDetail() {

        Channel channel = client.getNetResources().getChannelByNodeName("Server_01");

        SenderService.sendJsonNetData(channel, "/getInstanceDetail", "", 1L);
    }

}