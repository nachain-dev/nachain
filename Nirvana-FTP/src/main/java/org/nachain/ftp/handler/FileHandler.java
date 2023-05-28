package org.nachain.ftp.handler;

import org.nachain.ftp.FTPConnection;
import org.nachain.ftp.Utils;
import org.nachain.ftp.api.IFileSystem;
import org.nachain.ftp.api.ResponseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.UUID;


@SuppressWarnings("unchecked")
public class FileHandler {

    private final FTPConnection con;

    private IFileSystem fs = null;
    private Object cwd = null;

    private Object rnFile = null;
    private long start = 0;

    public FileHandler(FTPConnection connection) {
        this.con = connection;
    }

    public IFileSystem getFileSystem() {
        return fs;
    }

    public void setFileSystem(IFileSystem fs) {
        this.fs = fs;
        this.cwd = fs.getRoot();
    }

    public void registerCommands() {
        con.registerCommand("CWD", "CWD <file>", this::cwd);
        con.registerCommand("CDUP", "CDUP", this::cdup);
        con.registerCommand("PWD", "PWD", this::pwd);
        con.registerCommand("MKD", "MKD <file>", this::mkd);
        con.registerCommand("RMD", "RMD <file>", this::rmd);
        con.registerCommand("DELE", "DELE <file>", this::dele);
        con.registerCommand("LIST", "LIST [file]", this::list);
        con.registerCommand("NLST", "NLST [file]", this::nlst);
        con.registerCommand("RETR", "RETR <file>", this::retr);
        con.registerCommand("STOR", "STOR <file>", this::stor);
        con.registerCommand("STOU", "STOU [file]", this::stou);
        con.registerCommand("APPE", "APPE <file>", this::appe);
        con.registerCommand("REST", "REST <bytes>", this::rest);
        con.registerCommand("ABOR", "ABOR", this::abor);
        con.registerCommand("ALLO", "ALLO <size>", this::allo);
        con.registerCommand("RNFR", "RNFR <file>", this::rnfr);
        con.registerCommand("RNTO", "RNTO <file>", this::rnto);
        con.registerCommand("SMNT", "SMNT <file>", this::smnt);

        con.registerSiteCommand("CHMOD", "CHMOD <perm> <file>", this::site_chmod);

        con.registerCommand("MDTM", "MDTM <file>", this::mdtm);
        con.registerCommand("SIZE", "SIZE <file>", this::size);
        con.registerCommand("MLST", "MLST <file>", this::mlst);
        con.registerCommand("MLSD", "MLSD <file>", this::mlsd);

        con.registerCommand("XCWD", "XCWD <file>", this::cwd);
        con.registerCommand("XCUP", "XCUP", this::cdup);
        con.registerCommand("XPWD", "XPWD", this::pwd);
        con.registerCommand("XMKD", "XMKD <file>", this::mkd);
        con.registerCommand("XRMD", "XRMD <file>", this::rmd);

        con.registerCommand("MFMT", "MFMT <time> <file>", this::mfmt);

        con.registerCommand("MD5", "MD5 <file>", this::md5);
        con.registerCommand("MMD5", "MMD5 <file1, file2, ...>", this::mmd5);

        con.registerCommand("HASH", "HASH <file>", this::hash);

        con.registerFeature("base");
        con.registerFeature("hist");
        con.registerFeature("REST STREAM");
        con.registerFeature("MDTM");
        con.registerFeature("SIZE");
        con.registerFeature("MLST Type*;Size*;Modify*;Perm*;");
        con.registerFeature("TVFS");
        con.registerFeature("MFMT");
        con.registerFeature("MD5");
        con.registerFeature("HASH MD5;SHA-1;SHA-256");

        con.registerOption("MLST", "Type;Size;Modify;Perm;");
        con.registerOption("HASH", "MD5");
    }

    private Object getFile(String path) throws IOException {
        if (path.equals("...") || path.equals("..")) {
            return fs.getParent(cwd);
        } else if (path.equals("/")) {
            return fs.getRoot();
        } else if (path.startsWith("/")) {
            return fs.findFile(fs.getRoot(), path.substring(1));
        } else {
            return fs.findFile(cwd, path);
        }
    }

    private void cwd(String path) throws IOException {
        Object dir = getFile(path);

        if (fs.isDirectory(dir)) {
            cwd = dir;
            con.sendResponse(250, "The working directory was changed");
        } else {
            con.sendResponse(550, "Not a valid directory");
        }
    }

    private void cdup() throws IOException {
        cwd = fs.getParent(cwd);
        con.sendResponse(200, "The working directory was changed");
    }

    private void pwd() {
        String path = "/" + fs.getPath(cwd);
        con.sendResponse(257, '"' + path + '"' + " CWD Name");
    }

    private void allo() {

        con.sendResponse(200, "There's no need to allocate space");
    }

    private void rnfr(String path) throws IOException {
        rnFile = getFile(path);
        con.sendResponse(350, "Rename request received");
    }

    private void rnto(String path) throws IOException {
        if (rnFile == null) {
            con.sendResponse(503, "No rename request was received");
            return;
        }

        fs.rename(rnFile, getFile(path));
        rnFile = null;

        con.sendResponse(250, "File successfully renamed");
    }

    private void stor(String path) throws IOException {
        Object file = getFile(path);

        con.sendResponse(150, "Receiving a file stream for " + path);

        receiveStream(fs.writeFile(file, start));
        start = 0;
    }

    private void stou(String[] args) throws IOException {
        Object file = null;
        String ext = ".tmp";

        if (args.length > 0) {
            file = getFile(args[0]);
            int i = args[0].lastIndexOf('.');
            if (i > 0) ext = args[0].substring(i);
        }

        while (file != null && fs.exists(file)) {


            String name = UUID.randomUUID().toString().replace("-", "");
            file = fs.findFile(cwd, name + ext);
        }

        con.sendResponse(150, "File: " + fs.getPath(file));
        receiveStream(fs.writeFile(file, 0));
    }

    private void appe(String path) throws IOException {
        Object file = getFile(path);

        con.sendResponse(150, "Receiving a file stream for " + path);
        receiveStream(fs.writeFile(file, fs.exists(file) ? fs.getSize(file) : 0));
    }

    private void retr(String path) throws IOException {
        Object file = getFile(path);

        con.sendResponse(150, "Sending the file stream for " + path + " (" + fs.getSize(file) + " bytes)");
        sendStream(Utils.readFileSystem(fs, file, start, con.isAsciiMode()));
        start = 0;
    }

    private void rest(String byteStr) {
        long bytes = Long.parseLong(byteStr);
        if (bytes >= 0) {
            start = bytes;
            con.sendResponse(350, "Restarting at " + bytes + ". Ready to receive a RETR or STOR command");
        } else {
            con.sendResponse(501, "The number of bytes should be greater or equal to 0");
        }
    }

    private void abor() throws IOException {
        con.abortDataTransfers();
        con.sendResponse(226, "All transfers were aborted successfully");
    }

    private void list(String[] args) throws IOException {
        con.sendResponse(150, "Sending file list...");


        Object dir = args.length > 0 && !args[0].equals("-l") ? getFile(args[0]) : cwd;

        if (!fs.isDirectory(dir)) {
            con.sendResponse(550, "Not a directory");
            return;
        }

        StringBuilder data = new StringBuilder();

        for (Object file : fs.listFiles(dir)) {
            data.append(Utils.format(fs, file));
        }

        con.sendData(data.toString().getBytes("UTF-8"));
        con.sendResponse(226, "The list was sent");
    }

    private void nlst(String[] args) throws IOException {
        con.sendResponse(150, "Sending file list...");


        Object dir = args.length > 0 && !args[0].equals("-l") ? getFile(args[0]) : cwd;

        if (!fs.isDirectory(dir)) {
            con.sendResponse(550, "Not a directory");
            return;
        }

        StringBuilder data = new StringBuilder();

        for (Object file : fs.listFiles(dir)) {
            data.append(fs.getName(file)).append("\r\n");
        }

        con.sendData(data.toString().getBytes("UTF-8"));
        con.sendResponse(226, "The list was sent");
    }

    private void rmd(String path) throws IOException {
        Object file = getFile(path);

        if (!fs.isDirectory(file)) {
            con.sendResponse(550, "Not a directory");
            return;
        }

        fs.delete(file);
        con.sendResponse(250, '"' + path + '"' + " Directory Deleted");
    }

    private void dele(String path) throws IOException {
        Object file = getFile(path);

        if (fs.isDirectory(file)) {
            con.sendResponse(550, "Not a file");
            return;
        }

        fs.delete(file);
        con.sendResponse(250, '"' + path + '"' + " File Deleted");
    }

    private void mkd(String path) throws IOException {
        Object file = getFile(path);

        fs.mkdirs(file);
        con.sendResponse(257, '"' + path + '"' + " Directory Created");
    }

    private void smnt() {

        con.sendResponse(502, "SMNT is not implemented in this server");
    }

    private void site_chmod(String[] cmd) throws IOException {
        if (cmd.length <= 1) {
            con.sendResponse(501, "Missing parameters");
            return;
        }

        fs.chmod(getFile(cmd[1]), Utils.fromOctal(cmd[0]));
        con.sendResponse(200, "The file permissions were successfully changed");
    }

    private void mdtm(String path) throws IOException {
        Object file = getFile(path);

        con.sendResponse(213, Utils.toMdtmTimestamp(fs.getLastModified(file)));
    }

    private void size(String path) throws IOException {
        Object file = getFile(path);

        con.sendResponse(213, Long.toString(fs.getSize(file)));
    }

    private void mlst(String[] args) throws IOException {
        Object file = args.length > 0 ? getFile(args[0]) : cwd;

        if (!fs.exists(file)) {
            con.sendResponse(550, "File not found");
            return;
        }

        String[] options = con.getOption("MLST").split(";");
        String facts = Utils.getFacts(fs, file, options);

        con.sendResponse(250, "- Listing " + fs.getName(file) + "\r\n" + facts);
        con.sendResponse(250, "End");
    }

    private void mlsd(String[] args) throws IOException {
        Object file = args.length > 0 ? getFile(args[0]) : cwd;

        if (!fs.isDirectory(file)) {
            con.sendResponse(550, "Not a directory");
            return;
        }

        con.sendResponse(150, "Sending file information list...");

        String[] options = con.getOption("MLST").split(";");
        StringBuilder data = new StringBuilder();

        for (Object f : fs.listFiles(file)) {
            data.append(Utils.getFacts(fs, f, options));
        }

        con.sendData(data.toString().getBytes("UTF-8"));
        con.sendResponse(226, "The file list was sent!");
    }

    private void mfmt(String[] args) throws IOException {
        if (args.length < 2) {
            con.sendResponse(501, "Missing arguments");
            return;
        }

        Object file = getFile(args[1]);
        long time;

        if (!fs.exists(file)) {
            con.sendResponse(550, "File not found");
            return;
        }

        try {
            time = Utils.fromMdtmTimestamp(args[0]);
        } catch (ParseException ex) {
            con.sendResponse(500, "Couldn't parse the time");
            return;
        }

        fs.touch(file, time);
        con.sendResponse(213, "Modify=" + args[0] + "; " + fs.getPath(file));
    }

    private void md5(String path) throws IOException {
        String p = path = path.trim();

        if (p.length() > 2 && p.startsWith("\"") && p.endsWith("\"")) {

            p = p.substring(1, p.length() - 1).trim();
        }

        try {
            Object file = getFile(p);
            byte[] digest = fs.getDigest(file, "MD5");
            String md5 = new BigInteger(1, digest).toString(16);

            con.sendResponse(251, path + " " + md5);
        } catch (NoSuchAlgorithmException ex) {

            con.sendResponse(504, ex.getMessage());
        }
    }

    private void mmd5(String args) throws IOException {
        String[] paths = args.split(",");
        StringBuilder response = new StringBuilder();

        try {
            for (String path : paths) {
                String p = path = path.trim();

                if (p.length() > 2 && p.startsWith("\"") && p.endsWith("\"")) {

                    p = p.substring(1, p.length() - 1).trim();
                }

                Object file = getFile(p);
                byte[] digest = fs.getDigest(file, "MD5");
                String md5 = new BigInteger(1, digest).toString(16);

                if (response.length() > 0) response.append(", ");
                response.append(path).append(" ").append(md5);
            }

            con.sendResponse(paths.length == 1 ? 251 : 252, response.toString());
        } catch (NoSuchAlgorithmException ex) {

            con.sendResponse(504, ex.getMessage());
        }
    }

    private void hash(String path) throws IOException {
        try {
            Object file = getFile(path);
            String hash = con.getOption("HASH");
            byte[] digest = fs.getDigest(file, hash);
            String hex = new BigInteger(1, digest).toString(16);


            con.sendResponse(213, String.format("%s 0-%s %s %s", hash, fs.getSize(file), hex, fs.getName(file)));
        } catch (NoSuchAlgorithmException ex) {
            con.sendResponse(504, ex.getMessage());
        }
    }


    private void sendStream(InputStream in) {
        new Thread(() -> {
            try {
                con.sendData(in);
                con.sendResponse(226, "File sent!");
            } catch (ResponseException ex) {
                con.sendResponse(ex.getCode(), ex.getMessage());
            } catch (Exception ex) {
                con.sendResponse(451, ex.getMessage());
            }
        }).start();
    }


    private void receiveStream(OutputStream out) {
        new Thread(() -> {
            try {
                con.receiveData(out);
                con.sendResponse(226, "File received!");
            } catch (ResponseException ex) {
                con.sendResponse(ex.getCode(), ex.getMessage());
            } catch (Exception ex) {
                con.sendResponse(451, ex.getMessage());
            }
        }).start();
    }

}
