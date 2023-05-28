package org.nachain.ftp.api;

import org.nachain.ftp.FTPConnection;

import java.net.InetAddress;


public interface IUserAuthenticator {


    default boolean acceptsHost(FTPConnection con, InetAddress host) {
        return true;
    }


    boolean needsUsername(FTPConnection con);


    boolean needsPassword(FTPConnection con, String username, InetAddress host);


    IFileSystem authenticate(FTPConnection con, InetAddress host, String username, String password) throws AuthException;


    class AuthException extends RuntimeException {
    }

}
