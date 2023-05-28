package org.nachain.core.util;

import java.io.IOException;
import java.net.ServerSocket;

public class NetUtils {


    public static int checkPort(int port) {
        ServerSocket serverSocket = null;

        int returnValue;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
            returnValue = port;
        } catch (IOException var4) {
            returnValue = 0;
        }

        return returnValue;
    }

    public static int getFreePort(int startPort) {
        int returnValue = 0;

        for (int i = startPort; i <= 65535; ++i) {
            returnValue = checkPort(i);
            if (returnValue != 0) {
                break;
            }
        }

        return returnValue;
    }

}
