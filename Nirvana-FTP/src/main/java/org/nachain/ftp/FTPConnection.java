package org.nachain.ftp;

import org.nachain.ftp.api.CommandInfo;
import org.nachain.ftp.api.CommandInfo.ArgsArrayCommand;
import org.nachain.ftp.api.CommandInfo.Command;
import org.nachain.ftp.api.CommandInfo.NoArgsCommand;
import org.nachain.ftp.api.IFileSystem;
import org.nachain.ftp.api.ResponseException;
import org.nachain.ftp.handler.ConnectionHandler;
import org.nachain.ftp.handler.FileHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;


public class FTPConnection implements Closeable {

    protected final Map<String, CommandInfo> commands = new HashMap<>();
    protected final Map<String, CommandInfo> siteCommands = new HashMap<>();
    protected final List<String> features = new ArrayList<>();
    protected final Map<String, String> options = new HashMap<>();

    protected final FTPServer server;
    protected final ConnectionThread thread;
    protected final ArrayDeque<Socket> dataConnections = new ArrayDeque<>();
    protected Socket con;
    protected BufferedReader reader;
    protected BufferedWriter writer;
    protected ConnectionHandler conHandler;
    protected FileHandler fileHandler;

    protected long bytesTransferred = 0;
    protected boolean responseSent = true;
    protected int timeout = 0;
    protected int bufferSize = 0;
    protected long lastUpdate = 0;


    public FTPConnection(FTPServer server, Socket con, int idleTimeout, int bufferSize) throws IOException {
        this.server = server;
        this.con = con;
        this.reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));

        this.timeout = idleTimeout;
        this.bufferSize = bufferSize;
        this.lastUpdate = System.currentTimeMillis();
        con.setSoTimeout(timeout);

        this.conHandler = new ConnectionHandler(this);
        this.fileHandler = new FileHandler(this);

        this.thread = new ConnectionThread();
        this.thread.start();

        registerCommand("SITE", "SITE <command>", this::site);
        registerCommand("FEAT", "FEAT", this::feat, false);
        registerCommand("OPTS", "OPTS <option> [value]", this::opts);

        registerFeature("feat");
        registerFeature("UTF8");
        registerOption("UTF8", "ON");

        this.conHandler.registerCommands();
        this.fileHandler.registerCommands();
        this.conHandler.onConnected();
    }


    @Deprecated
    public FTPConnection(FTPServer server, Socket con, int idleTimeout) throws IOException {
        this(server, con, idleTimeout, 1024);
    }


    public FTPServer getServer() {
        return server;
    }


    public InetAddress getAddress() {
        return con.getInetAddress();
    }


    public long getBytesTransferred() {
        return bytesTransferred;
    }


    public boolean isAuthenticated() {
        return conHandler.isAuthenticated();
    }


    public String getUsername() {
        return conHandler.getUsername();
    }


    public boolean isAsciiMode() {
        return conHandler.isAsciiMode();
    }


    public IFileSystem getFileSystem() {
        return fileHandler.getFileSystem();
    }


    public void setFileSystem(IFileSystem fs) {
        fileHandler.setFileSystem(fs);
    }

    public boolean isSSLEnabled() {
        return con instanceof SSLSocket;
    }

    public void enableSSL(SSLContext context) throws IOException {
        SSLSocketFactory factory = context.getSocketFactory();
        con = factory.createSocket(con, con.getInetAddress().getHostAddress(), con.getPort(), true);
        ((SSLSocket) con).setUseClientMode(false);

        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
    }


    public void sendResponse(int code, String response) {
        if (con.isClosed()) return;

        if (response == null || response.isEmpty()) {
            response = "Unknown";
        }

        try {
            if (response.charAt(0) == '-') {
                writer.write(code + response + "\r\n");
            } else {
                writer.write(code + " " + response + "\r\n");
            }
            writer.flush();
        } catch (IOException ex) {
            Utils.closeQuietly(this);
        }
        responseSent = true;
    }


    public void sendData(byte[] data) throws ResponseException {
        if (con.isClosed()) return;

        Socket socket = null;
        try {
            socket = conHandler.createDataSocket();
            dataConnections.add(socket);
            OutputStream out = socket.getOutputStream();

            Utils.write(out, data, data.length, conHandler.isAsciiMode());
            bytesTransferred += data.length;

            out.flush();
            Utils.closeQuietly(out);
            Utils.closeQuietly(socket);
        } catch (SocketException ex) {
            throw new ResponseException(426, "Transfer aborted");
        } catch (IOException ex) {
            throw new ResponseException(425, "An error occurred while transferring the data");
        } finally {
            onUpdate();
            if (socket != null) dataConnections.remove(socket);
        }
    }


    public void sendData(InputStream in) throws ResponseException {
        if (con.isClosed()) return;

        Socket socket = null;
        try {
            socket = conHandler.createDataSocket();
            dataConnections.add(socket);
            OutputStream out = socket.getOutputStream();

            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = in.read(buffer)) != -1) {
                Utils.write(out, buffer, len, conHandler.isAsciiMode());
                bytesTransferred += len;
            }

            out.flush();
            Utils.closeQuietly(out);
            Utils.closeQuietly(in);
            Utils.closeQuietly(socket);
        } catch (SocketException ex) {
            throw new ResponseException(426, "Transfer aborted");
        } catch (IOException ex) {
            throw new ResponseException(425, "An error occurred while transferring the data");
        } finally {
            onUpdate();
            if (socket != null) dataConnections.remove(socket);
        }
    }


    public void receiveData(OutputStream out) throws ResponseException {
        if (con.isClosed()) return;

        Socket socket = null;
        try {
            socket = conHandler.createDataSocket();
            dataConnections.add(socket);
            InputStream in = socket.getInputStream();

            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
                bytesTransferred += len;
            }

            out.flush();
            Utils.closeQuietly(out);
            Utils.closeQuietly(in);
            Utils.closeQuietly(socket);
        } catch (SocketException ex) {
            throw new ResponseException(426, "Transfer aborted");
        } catch (IOException ex) {
            throw new ResponseException(425, "An error occurred while transferring the data");
        } finally {
            onUpdate();
            if (socket != null) dataConnections.remove(socket);
        }
    }


    public void abortDataTransfers() {
        while (!dataConnections.isEmpty()) {
            Socket socket = dataConnections.poll();
            if (socket != null) Utils.closeQuietly(socket);
        }
    }


    public void registerFeature(String feat) {
        if (!features.contains(feat)) {
            features.add(feat);
        }
    }


    public void registerOption(String option, String value) {
        options.put(option.toUpperCase(), value);
    }


    public String getOption(String option) {
        return options.get(option.toUpperCase());
    }

    public void registerSiteCommand(String label, String help, Command cmd) {
        addSiteCommand(label, help, cmd);
    }

    public void registerSiteCommand(String label, String help, NoArgsCommand cmd) {
        addSiteCommand(label, help, cmd);
    }

    public void registerSiteCommand(String label, String help, ArgsArrayCommand cmd) {
        addSiteCommand(label, help, cmd);
    }

    public void registerCommand(String label, String help, Command cmd) {
        addCommand(label, help, cmd, true);
    }

    public void registerCommand(String label, String help, NoArgsCommand cmd) {
        addCommand(label, help, cmd, true);
    }

    public void registerCommand(String label, String help, ArgsArrayCommand cmd) {
        addCommand(label, help, cmd, true);
    }

    public void registerCommand(String label, String help, Command cmd, boolean needsAuth) {
        addCommand(label, help, cmd, needsAuth);
    }

    public void registerCommand(String label, String help, NoArgsCommand cmd, boolean needsAuth) {
        addCommand(label, help, cmd, needsAuth);
    }

    public void registerCommand(String label, String help, ArgsArrayCommand cmd, boolean needsAuth) {
        addCommand(label, help, cmd, needsAuth);
    }


    protected void addSiteCommand(String label, String help, Command cmd) {
        siteCommands.put(label.toUpperCase(), new CommandInfo(cmd, help, true));
    }


    protected void addCommand(String label, String help, Command cmd, boolean needsAuth) {
        commands.put(label.toUpperCase(), new CommandInfo(cmd, help, needsAuth));
    }


    public String getSiteHelpMessage(String label) {
        CommandInfo info = siteCommands.get(label);
        return info != null ? info.help : null;
    }


    public String getHelpMessage(String label) {
        CommandInfo info = commands.get(label);
        return info != null ? info.help : null;
    }

    protected void onUpdate() {
        lastUpdate = System.currentTimeMillis();
    }


    protected void process(String cmd) {
        int firstSpace = cmd.indexOf(' ');
        if (firstSpace < 0) firstSpace = cmd.length();

        CommandInfo info = commands.get(cmd.substring(0, firstSpace).toUpperCase());

        if (info == null) {
            sendResponse(502, "Unknown command");
            return;
        }

        processCommand(info, firstSpace != cmd.length() ? cmd.substring(firstSpace + 1) : "");
    }


    protected void site(String cmd) {
        int firstSpace = cmd.indexOf(' ');
        if (firstSpace < 0) firstSpace = cmd.length();

        CommandInfo info = siteCommands.get(cmd.substring(0, firstSpace).toUpperCase());

        if (info == null) {
            sendResponse(504, "Unknown site command");
            return;
        }

        processCommand(info, firstSpace != cmd.length() ? cmd.substring(firstSpace + 1) : "");
    }


    protected void feat() {
        StringBuilder list = new StringBuilder();
        list.append("- Supported Features:\r\n");

        for (String feat : features) {
            list.append(' ').append(feat).append("\r\n");
        }

        sendResponse(211, list.toString());
        sendResponse(211, "End");
    }


    protected void opts(String[] opts) {
        if (opts.length < 1) {
            sendResponse(501, "Missing parameters");
            return;
        }

        String option = opts[0].toUpperCase();

        if (!options.containsKey(option)) {
            sendResponse(501, "No option found");
        } else if (opts.length < 2) {
            sendResponse(200, options.get(option));
        } else {
            options.put(option, opts[1].toUpperCase());
            sendResponse(200, "Option updated");
        }
    }

    protected void processCommand(CommandInfo info, String args) {
        if (info.needsAuth && !conHandler.isAuthenticated()) {
            sendResponse(530, "Needs authentication");
            return;
        }

        responseSent = false;

        try {
            info.command.run(info, args);
        } catch (ResponseException ex) {
            sendResponse(ex.getCode(), ex.getMessage());
        } catch (FileNotFoundException ex) {
            sendResponse(550, ex.getMessage());
        } catch (IOException ex) {
            sendResponse(450, ex.getMessage());
        } catch (Exception ex) {
            sendResponse(451, ex.getMessage());
            ex.printStackTrace();
        }

        if (!responseSent) sendResponse(200, "Done");
    }


    protected void update() {
        if (conHandler.shouldStop()) {
            Utils.closeQuietly(this);
            return;
        }

        String line;

        try {
            line = reader.readLine();
        } catch (SocketTimeoutException ex) {

            if (!dataConnections.isEmpty() && (System.currentTimeMillis() - lastUpdate) >= timeout) {
                Utils.closeQuietly(this);
            }
            return;
        } catch (SocketException e) {
            Utils.closeQuietly(this);
            return;
        } catch (IOException ex) {
            return;
        }

        if (line == null) {
            Utils.closeQuietly(this);
            return;
        }

        if (line.isEmpty()) return;

        process(line);
    }


    protected void stop(boolean close) throws IOException {
        if (!thread.isInterrupted()) {
            thread.interrupt();
        }

        conHandler.onDisconnected();

        if (close) con.close();
    }


    protected void close(boolean close) throws IOException {
        stop(close);

        server.removeConnection(this);
    }


    @Override
    public void close() throws IOException {
        close(true);
    }


    private class ConnectionThread extends Thread {
        @Override
        public void run() {
            while (!con.isClosed()) {
                update();
            }

            try {
                close(false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
