package org.nachain.core.server;

public interface IServer {


    String getServerName();


    void startServer();


    void stopServer();


    void starting();


    void executes();

}
