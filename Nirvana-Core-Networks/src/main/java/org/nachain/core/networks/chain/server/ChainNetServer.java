package org.nachain.core.networks.chain.server;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.iserver.IServer;
import org.nachain.core.server.AbstractServer;


@Slf4j
public class ChainNetServer extends AbstractServer {

    private IServer server;

    @Override
    public String getServerName() {
        return getClass().getSimpleName();
    }

    @Override
    public void starting() {

        server = new IServer(new ServerBaseAction());
        server.start();

        log.debug("ChainServer waitReady...");


        server.waitReady();

        log.debug("ChainServer isReady:" + server.getNetResources().isReady());
    }

    @Override
    public void stopServer() {

        server.stopServer();

        super.stopServer();
    }

    @Override
    public void executes() {
        executeSleepMillis(1000 * 10);
    }
}