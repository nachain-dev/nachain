package org.nachain.libs.download;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.download.global.DownloadConfig;
import org.nachain.libs.util.RAFileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


@Slf4j
public class DownThread extends Thread {


    private String fileURL;


    public long startPosition = 0;


    public long endPosition;


    public long downPosition;


    private int threadID;


    private RAFileUtil fileHandle = null;


    private boolean isFinished = false;


    private boolean isStop = false;


    public DownThread(String fileURl, String fileFullPath,
                      long startPosition, long endPosition, int threadID)
            throws IOException {
        this.fileURL = fileURl;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.threadID = threadID;
        this.downPosition = startPosition;
        this.fileHandle = new RAFileUtil(fileFullPath, startPosition);
    }

    public void run() {
        try {

            int bufferSize = 1024;
            while (downPosition < endPosition && !isStop) {

                URL url = new URL(fileURL);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestProperty("User-Agent", DownloadConfig.UserAgent);

                httpConnection.setRequestProperty("RANGE", "bytes=" + downPosition + "-" + endPosition);

                InputStream is = httpConnection.getInputStream();

                byte[] b = new byte[bufferSize];
                int nRead;

                while ((nRead = is.read(b, 0, bufferSize)) > 0 && downPosition < endPosition && !isStop) {

                    downPosition += fileHandle.write(b, 0, nRead);
                }
                log.debug("Thread " + threadID + " Has been downloaded:" + downPosition + "-" + endPosition);
            }
            fileHandle.close();


            if (downPosition >= endPosition) {
                isFinished = true;
            }

            log.debug("Thread " + threadID + " Download the end!");

        } catch (Exception e) {
            log.error("Error downloading file:", e);
        }
    }


    public void stopDownload() {
        isStop = true;
    }


    public boolean isFinished() {
        return isFinished;
    }


}