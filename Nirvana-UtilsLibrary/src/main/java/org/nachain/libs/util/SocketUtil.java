package org.nachain.libs.util;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketUtil {

    public static int checkPort(int port) {
        int returnValue = 0;

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
            returnValue = port;
        } catch (IOException e) {

            returnValue = 0;
        }

        return returnValue;
    }


    public static int getFreePort(int StartPort) {
        int returnValue = 0;

        for (int i = StartPort; i <= 65535; i++) {
            returnValue = checkPort(i);
            if (returnValue != 0)
                break;
        }

        return returnValue;
    }
}