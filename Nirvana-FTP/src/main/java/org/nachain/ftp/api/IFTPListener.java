package org.nachain.ftp.api;

import org.nachain.ftp.FTPConnection;


public interface IFTPListener {


    void onConnected(FTPConnection con);


    void onDisconnected(FTPConnection con);

}
