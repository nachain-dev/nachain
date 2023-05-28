package org.nachain.libs.ftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;
import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.FileUtil;

import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FtpUtil {

    public static final String ANONYMOUS_LOGIN = "anonymous";
    private FTPClient ftp;
    private boolean isLogin;

    public FtpUtil() {
        ftp = new FTPClient();
        isLogin = false;
    }

    public FtpUtil(int defaultTimeoutSecond, int connectTimeoutSecond, int dataTimeoutSecond) {
        ftp = new FTPClient();
        isLogin = false;

        ftp.setDefaultTimeout(defaultTimeoutSecond * 1000);
        ftp.setConnectTimeout(connectTimeoutSecond * 1000);
        ftp.setDataTimeout(dataTimeoutSecond * 1000);
    }


    public void connect(String host, int port, String user, String password, boolean isTextMode) throws IOException {

        try {
            ftp.connect(host, port);
        } catch (UnknownHostException ex) {
            throw new IOException("Unable to find FTP server" + host);
        }


        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            disconnect();
            throw new IOException("Failed to connect to the FTP server:" + host);
        }

        if (user == "") {
            user = ANONYMOUS_LOGIN;
        }


        if (!ftp.login(user, password)) {
            isLogin = false;
            disconnect();
            throw new IOException("Failed to log in to the FTP server" + host);
        } else {
            isLogin = true;
        }


        if (isTextMode) {
            ftp.setFileType(FTP.ASCII_FILE_TYPE);
        } else {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
        }
    }


    public void upload(String defaultHome, String ftpFileName, File localFile) throws IOException {

        if (!localFile.exists()) {
            throw new IOException("Can't upload '" + localFile.getAbsolutePath() + "'. This file does not exist");
        }


        String saveFilePath = ftpFileName;


        if (!defaultHome.equals("")) {

            if (!ftp.changeWorkingDirectory(defaultHome)) {
                log.error("The default FTP directory cannot be accessed:" + defaultHome);

                return;
            }
        }


        {


            String saveFolderPath = FileUtil.getFileFolderPath(ftpFileName).trim();
            if (!saveFolderPath.equals("")) {


                String saveFileName = FileUtil.getFileName(ftpFileName);
                saveFileName = CommUtil.replace(saveFileName, "\\", "/").trim();

                saveFilePath = saveFileName;


                String folderPath = CommUtil.replace(saveFolderPath, "\\", "/").trim();

                String[] folderArray = folderPath.split("/");

                for (int i = 0; i < folderArray.length; i++) {
                    String folderUnit = folderArray[i];

                    folderUnit = new String(folderUnit.getBytes("GBK"), "iso-8859-1");

                    if (!folderUnit.equals("")) {

                        if (!ftp.changeWorkingDirectory(folderUnit)) {
                            boolean isMake = ftp.makeDirectory(folderUnit);
                            if (isMake) {

                                ftp.changeWorkingDirectory(folderUnit);
                                log.debug("FTP Folder created successfully:" + folderUnit);
                            } else {
                                log.warn("FTP Failed to create file(defaultHome:" + defaultHome + "):" + folderUnit);
                            }
                        }
                    }
                }


            }
        }


        if (saveFilePath.startsWith("/")) {
            saveFilePath = saveFilePath.substring(1);
        }


        InputStream in = null;
        try {


            ftp.enterLocalPassiveMode();

            in = new BufferedInputStream(new FileInputStream(localFile));
            if (!ftp.storeFile(saveFilePath, in)) {
                throw new IOException("Unable to upload files '" + ftpFileName + "' To the FTP server. Check whether the FTP path and permission are correct." + "\r\nWorkingDirectory:" + ftp.printWorkingDirectory() + "\r\nsaveFilePath:" + saveFilePath + "\r\nlocalFile:" + localFile.getPath());
            }

        } catch (FTPConnectionClosedException e) {

            isLogin = false;
            disconnect();
            throw new IOException("FTP The connection has been closed. Please log in again.");
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ex) {
            }
        }
    }


    public void download(String ftpFileName, File localFile) throws IOException {

        OutputStream out = null;
        try {

            ftp.enterLocalPassiveMode();


            FTPFile[] fileInfoArray = ftp.listFiles(ftpFileName);
            if (fileInfoArray == null) {
                throw new FileNotFoundException("File " + ftpFileName + " Cannot be found on the FTP server.");
            }


            FTPFile fileInfo = fileInfoArray[0];
            long size = fileInfo.getSize();
            if (size > Integer.MAX_VALUE) {
                throw new IOException("File " + ftpFileName + " too big.");
            }


            out = new BufferedOutputStream(new FileOutputStream(localFile));
            if (!ftp.retrieveFile(ftpFileName, out)) {
                throw new IOException("Obtain files from the FTP server " + ftpFileName + " check whether the FTP path and permissions are correct.");
            }

            out.flush();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
    }


    public void remove(String ftpFileName) throws IOException {
        if (!ftp.deleteFile(ftpFileName)) {
            throw new IOException("Cannot FTP remote server files '" + ftpFileName + "' remove.");
        }
    }


    public void disConnection() throws IOException {
        ftp.disconnect();
    }


    public void changeWorkingDirectory(String path) throws IOException {
        ftp.changeWorkingDirectory(path);
    }


    public List<String> list(String filePath) throws IOException {
        List<String> fileList = new ArrayList<String>();


        ftp.enterLocalPassiveMode();

        FTPFile[] ftpFiles = ftp.listFiles(filePath);
        int size = (ftpFiles == null) ? 0 : ftpFiles.length;
        for (int i = 0; i < size; i++) {
            FTPFile ftpFile = ftpFiles[i];
            if (ftpFile.isFile()) {
                fileList.add(ftpFile.getName());
            }
        }

        return fileList;
    }


    public void sendSiteCommand(String args) throws IOException {
        if (ftp.isConnected()) {
            try {
                ftp.sendSiteCommand(args);
            } catch (IOException ex) {
            }
        }
    }


    public void disconnect() throws IOException {
        if (ftp.isConnected()) {
            try {
                isLogin = false;
                ftp.logout();
            } catch (IOException e) {


            }
            try {
                ftp.disconnect();
            } catch (IOException e) {
                throw new IOException("Failed to disconnect the FTP connection", e);
            }
        }
    }


    public boolean isAvailable() {
        return ftp.isAvailable();
    }


    public boolean isConnected() {
        return ftp.isConnected();
    }


    public boolean isLogin() {
        return isLogin;
    }


    public String makeFTPFileName(String ftpPath, File localFile) {
        if (ftpPath == "") {
            return localFile.getName();
        } else {
            String path = ftpPath.trim();
            if (path.charAt(path.length() - 1) != '/') {
                path = path + "/";
            }

            return path + localFile.getName();
        }
    }


    public String getWorkingDirectory() {
        if (!isLogin) {
            return "";
        }

        try {
            return ftp.printWorkingDirectory();
        } catch (IOException e) {
        }

        return "";
    }


    public boolean setWorkingDirectory(String dir) {
        if (!isLogin) {
            return false;
        }

        try {
            return ftp.changeWorkingDirectory(dir);
        } catch (IOException e) {
        }

        return false;
    }


    public boolean setParentDirectory() {
        if (!isLogin) {
            return false;
        }

        try {
            return ftp.changeToParentDirectory();
        } catch (IOException e) {
        }

        return false;
    }


    public String getParentDirectory() {
        if (!isLogin) {
            return "";
        }

        String w = getWorkingDirectory();
        setParentDirectory();
        String p = getWorkingDirectory();
        setWorkingDirectory(w);

        return p;
    }


    public List<FfpFileInfo> listFiles(String filePath) throws IOException {
        List<FfpFileInfo> fileList = new ArrayList<FfpFileInfo>();


        ftp.enterLocalPassiveMode();
        FTPFile[] ftpFiles = ftp.listFiles(filePath);
        int size = (ftpFiles == null) ? 0 : ftpFiles.length;
        for (int i = 0; i < size; i++) {
            FTPFile ftpFile = ftpFiles[i];
            FfpFileInfo fi = new FfpFileInfo();
            fi.setName(ftpFile.getName());
            fi.setSize(ftpFile.getSize());
            fi.setTimestamp(ftpFile.getTimestamp());
            fi.setType(ftpFile.isDirectory());
            fileList.add(fi);
        }

        return fileList;
    }


    public void getFile(String ftpFileName, OutputStream out) throws IOException {
        try {

            ftp.enterLocalPassiveMode();


            FTPFile[] fileInfoArray = ftp.listFiles(ftpFileName);
            if (fileInfoArray == null) {
                throw new FileNotFoundException("File '" + ftpFileName + "' cannot be found on the FTP server.");
            }


            FTPFile fileInfo = fileInfoArray[0];
            long size = fileInfo.getSize();
            if (size > Integer.MAX_VALUE) {
                throw new IOException("File '" + ftpFileName + "' too large for the program to handle.");
            }


            if (!ftp.retrieveFile(ftpFileName, out)) {
                throw new IOException("Download file '" + ftpFileName + "' an error. Check whether the FTP path and permission are correct.");
            }

            out.flush();

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
    }


    public void putFile(String ftpFileName, InputStream in) throws IOException {
        try {

            ftp.enterLocalPassiveMode();

            if (!ftp.storeFile(ftpFileName, in)) {
                throw new IOException("Unable to upload files '" + ftpFileName + "' to the FTP Server. Check whether the FTP path and permission are correct.");
            }
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
        }
    }


}