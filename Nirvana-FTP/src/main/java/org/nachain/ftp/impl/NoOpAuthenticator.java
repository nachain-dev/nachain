package org.nachain.ftp.impl;

import org.nachain.ftp.FTPConnection;
import org.nachain.ftp.api.IFileSystem;
import org.nachain.ftp.api.IUserAuthenticator;

import java.net.InetAddress;


public class NoOpAuthenticator implements IUserAuthenticator {

    private final IFileSystem fs;


    public NoOpAuthenticator(IFileSystem fs) {
        this.fs = fs;
    }

    @Override
    public boolean needsUsername(FTPConnection con) {
        return false;
    }

    @Override
    public boolean needsPassword(FTPConnection con, String username, InetAddress address) {
        return false;
    }

    @Override
    public IFileSystem authenticate(FTPConnection con, InetAddress address, String username, String password) throws AuthException {
        return fs;
    }
}
