package org.nachain.ftp;

import org.nachain.ftp.api.IFTPListener;
import org.nachain.ftp.api.IUserAuthenticator;

import javax.net.ssl.SSLContext;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FTPServer implements Closeable {

    protected final List<FTPConnection> connections = Collections.synchronizedList(new ArrayList<FTPConnection>());
    protected final List<IFTPListener> listeners = Collections.synchronizedList(new ArrayList<IFTPListener>());

    protected IUserAuthenticator auth = null;
    protected int idleTimeout = 5 * 60 * 1000;
    protected int bufferSize = 1024;
    protected SSLContext ssl = null;
    protected boolean explicitSecurity = true;

    protected ServerSocket socket = null;
    protected ServerThread serverThread = null;


    public FTPServer() {

    }


    public FTPServer(IUserAuthenticator auth) {
        setAuthenticator(auth);
    }


    public InetAddress getAddress() {
        return socket != null ? socket.getInetAddress() : null;
    }


    public int getPort() {
        return socket != null ? socket.getLocalPort() : -1;
    }


    public IUserAuthenticator getAuthenticator() {
        return auth;
    }


    public void setAuthenticator(IUserAuthenticator auth) {
        if (auth == null) throw new NullPointerException("The Authenticator is null");
        this.auth = auth;
    }


    public SSLContext getSSLContext() {
        return ssl;
    }


    public void setSSLContext(SSLContext ssl) {
        this.ssl = ssl;
    }


    public void setExplicitSSL(boolean explicit) {
        this.explicitSecurity = explicit;
    }


    public void setTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }


    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }


    public void addListener(IFTPListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }


    public void removeListener(IFTPListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }


    public void listen(int port) throws IOException {
        listen(null, port);
    }


    public void listen(InetAddress address, int port) throws IOException {
        if (auth == null) throw new NullPointerException("The Authenticator is null");
        if (socket != null) throw new IOException("Server already started");

        socket = Utils.createServer(port, 50, address, ssl, !explicitSecurity);

        serverThread = new ServerThread();
        serverThread.setDaemon(true);
        serverThread.start();
    }


    public void listenSync(int port) throws IOException {
        listenSync(null, port);
    }


    public void listenSync(InetAddress address, int port) throws IOException {
        if (auth == null) throw new NullPointerException("The Authenticator is null");
        if (socket != null) throw new IOException("Server already started");

        socket = Utils.createServer(port, 50, address, ssl, !explicitSecurity);

        while (!socket.isClosed()) {
            update();
        }
    }


    protected void update() {
        try {
            addConnection(socket.accept());
        } catch (IOException ex) {

        }
    }


    protected FTPConnection createConnection(Socket socket) throws IOException {
        return new FTPConnection(this, socket, idleTimeout, bufferSize);
    }


    protected void addConnection(Socket socket) throws IOException {
        FTPConnection con = createConnection(socket);

        synchronized (listeners) {
            for (IFTPListener l : listeners) {
                l.onConnected(con);
            }
        }
        synchronized (connections) {
            connections.add(con);
        }
    }


    protected void removeConnection(FTPConnection con) throws IOException {
        synchronized (listeners) {
            for (IFTPListener l : listeners) {
                l.onDisconnected(con);
            }
        }
        synchronized (connections) {
            connections.remove(con);
        }
    }


    protected void dispose() {

        if (serverThread != null) {
            serverThread.interrupt();
            serverThread = null;
        }


        synchronized (connections) {
            for (FTPConnection con : connections) {
                try {
                    con.stop(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            connections.clear();
        }
    }


    @Override
    public void close() throws IOException {
        dispose();

        if (socket != null) {
            socket.close();
            socket = null;
        }
    }


    private class ServerThread extends Thread {
        @Override
        public void run() {
            while (socket != null && !socket.isClosed()) {
                update();
            }
        }
    }

}
