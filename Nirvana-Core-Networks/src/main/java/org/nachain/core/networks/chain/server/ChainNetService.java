package org.nachain.core.networks.chain.server;

public class ChainNetService {

    private static ChainNetServer chainNetServer;


    public static ChainNetServer startServer() {

        if (chainNetServer == null) {
            chainNetServer = new ChainNetServer();
        }

        if (chainNetServer.isRun() && !chainNetServer.isEnd()) {
            throw new RuntimeException("ChainNetServer is already running, do not start again");
        }

        chainNetServer.startServer();

        return chainNetServer;
    }
}
