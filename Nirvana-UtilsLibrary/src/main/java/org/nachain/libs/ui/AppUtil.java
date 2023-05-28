package org.nachain.libs.ui;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.httpclient.HttpClient;
import org.nachain.libs.httpclient.HttpClientService;
import org.nachain.libs.httpclient.bean.RequestHttp;
import org.nachain.libs.util.CommUtil;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

@Slf4j
public class AppUtil {


    public static String getTempDirectory() {
        String returnValue = null;

        returnValue = System.getProperty("java.io.tmpdir");
        returnValue += "fptmp";

        return returnValue;
    }


    public static void delAllTempFile() {
        String filePath = getTempDirectory();
        File delFolder = new File(filePath);
        if (delFolder.exists() && delFolder.isDirectory()) {
            File[] files = delFolder.listFiles();
            for (int i = 0; i < files.length; i++) {
                File delFile = files[i];
                delFile.delete();
            }
        }
    }


    public static String byte2hex(byte bytes[]) {
        StringBuffer retString = new StringBuffer();
        for (int i = 0; i < bytes.length; ++i) {
            retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1).toUpperCase());
        }
        return retString.toString();
    }


    public static byte[] hex2byte(String hex) {
        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bts;
    }

    public static String shellResponse(String command, int skipLine) {
        StringBuffer returnValue = new StringBuffer();

        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedInputStream in = new BufferedInputStream(process.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            for (int i = 1; i <= skipLine; i++) {
                br.readLine();
            }

            String s;
            while ((s = br.readLine()) != null) {
                returnValue.append(s);
                returnValue.append("\r\n");
            }
        } catch (IOException e) {
            log.error("Error running external program:", e);
        }

        return returnValue.toString();
    }

    public static boolean checkVersion(String AppVersion, String url) {

        try {
            RequestHttp http = HttpClientService.buildRequestHttp(url);
            http.setReadTimeout(2000);
            String data = HttpClient.requestHttp(http);
            data = data.trim();


            if (data.length() > 10) {
                return true;
            }


            if (!data.contains(".")) {
                return true;
            }

            if (CommUtil.null2Double(data) == 0) {
                return true;
            }

            double myVersion = CommUtil.null2Double(AppVersion) * 100;
            double newVersion = CommUtil.null2Double(data) * 100;

            if (newVersion > myVersion) {
                return false;
            }
        } catch (Exception e) {
            log.warn("Error reading software version:" + url + ", " + e.toString());
        }

        return true;
    }


    public static boolean isInstanceRunning(String RootRealPath) {
        boolean returnValue = false;

        final String pidFileName = RootRealPath + "instance.pid";
        RandomAccessFile raf = null;
        FileChannel fc = null;
        FileLock fl = null;

        File pidFile = new File(pidFileName);
        log.debug(pidFile.getAbsolutePath());


        if (pidFile == null || !pidFile.exists()) {
            try {
                pidFile.createNewFile();
            } catch (IOException e) {
                log.error("Error creating file:" + pidFile.getAbsolutePath(), e);
            }
        }


        try {
            raf = new RandomAccessFile(pidFile, "rwd");
        } catch (FileNotFoundException e) {
            log.error("Unable to find file:" + pidFile.getAbsolutePath(), e);
        }
        fc = raf.getChannel();
        try {
            fl = fc.tryLock();
        } catch (IOException e) {
            log.error("An error occurred while trying to lock the file:" + pidFile.getAbsolutePath(), e);
        }
        if (fl == null || !fl.isValid()) {
            log.debug("Another instance is already running.");
            returnValue = true;
            try {
                fc.close();
                raf.close();
            } catch (IOException e) {
                log.error("Error closing file:" + pidFile.getAbsolutePath(), e);
            }
        }

        return returnValue;
    }


    public static String toGBK(String unicodeStr) {
        try {
            String gbkStr = new String(unicodeStr.getBytes("UTF-8"), "UTF-8");
            return gbkStr;
        } catch (UnsupportedEncodingException e) {
            return unicodeStr;
        }
    }

}