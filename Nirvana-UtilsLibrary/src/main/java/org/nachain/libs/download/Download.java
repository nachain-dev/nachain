package org.nachain.libs.download;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.download.global.DownloadConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class Download extends Thread {


    private DownThread[] downThread;

    private long[] startPosition;

    private long[] endPosition;

    private long fileSize;

    private boolean isFirstDown = true;

    private boolean isStop = false;

    private boolean isFinished = false;

    private boolean hasError = false;


    private String downFileURL;

    private int threadTotal;

    private File saveFile;
    private File infoFile;
    private File downFile;

    private String downFilePostfix = ".down";
    private String infoFilePostfix = ".info";

    private String saveFileFullPath;
    private String downFileFullPath;
    private String infoFileFullPath;

    public Download(DownTask downTask) {
        downFileURL = downTask.getDownFileURL();
        threadTotal = downTask.getThreadTotal();
        saveFileFullPath = downTask.getSaveFilePath() + File.separator + downTask.getSaveFileName();
        downFileFullPath = saveFileFullPath + downFilePostfix;
        infoFileFullPath = saveFileFullPath + infoFilePostfix;
        saveFile = new File(saveFileFullPath);
        downFile = new File(downFileFullPath);
        infoFile = new File(infoFileFullPath);


        if (infoFile.exists()) {
            isFirstDown = false;

            readPosition();
        } else {

            startPosition = new long[threadTotal];
            endPosition = new long[threadTotal];
        }
    }

    public void run() {
        try {

            if (isFirstDown) {

                fileSize = getFileSize(downFileURL);
                if (fileSize == -1) {
                    log.error("Unknown file length!", new Exception("Unknown file length!"));

                    isStop = true;

                    hasError = true;
                    return;
                } else if (fileSize == -2) {
                    log.error("The file cannot be accessed. The file may not exist!", new Exception("The file cannot be accessed. The file may not exist!"));

                    isStop = true;

                    hasError = true;
                    return;
                } else {

                    for (int i = 0; i < threadTotal; i++) {
                        startPosition[i] = (long) (i * (fileSize / threadTotal));
                    }

                    for (int i = 0; i < threadTotal - 1; i++) {
                        endPosition[i] = startPosition[i + 1] - 1;
                    }

                    endPosition[threadTotal - 1] = fileSize;
                }
            }


            String folder = saveFileFullPath.replaceAll("\\\\", "/");
            folder = saveFileFullPath.substring(0, folder.lastIndexOf("/"));
            log.debug("saveFileFullPath:" + saveFileFullPath + "\r\nfolder:" + folder);
            File fileFolder = new File(folder);
            if (!fileFolder.exists()) {
                fileFolder.mkdirs();
            }


            downThread = new DownThread[threadTotal];

            for (int i = 0; i < threadTotal; i++) {

                downThread[i] = new DownThread(downFileURL, downFileFullPath, startPosition[i], endPosition[i], i);
                downThread[i].start();

                log.debug("Thread" + i + " Download task , initial position(StartPos): = " + startPosition[i] + ",  end position(EndPos): = " + endPosition[i]);
            }


            boolean breakWhile = false;


            int sleep = 1000;

            long oldDownTotal = 0;

            long nowDownTotal = 0;

            long perSecDownTotal = 0;

            long startTime = System.currentTimeMillis();
            long useTime = 0;

            long averageDownTotal = 0;

            while (!isStop) {

                writePosition();
                try {
                    Thread.sleep(sleep);
                } catch (Exception e) {
                    log.error("Thread sleep error:", e);
                }


                useTime = System.currentTimeMillis() - startTime;

                nowDownTotal = getDownTotal();

                perSecDownTotal = (nowDownTotal - oldDownTotal) * (1000 / sleep);

                oldDownTotal = nowDownTotal;

                averageDownTotal = nowDownTotal / (useTime / 1000);
                log.debug("Download per second:" + perSecDownTotal / 1024 + " kb/" + averageDownTotal / 1024 + " kb" + " Have downloaded:" + getDownPercent() * 100 + "%");


                breakWhile = true;
                for (int i = 0; i < threadTotal; i++) {

                    if (!downThread[i].isFinished()) {
                        breakWhile = false;
                        break;
                    }
                }

                if (breakWhile) {
                    isFinished = true;
                    break;
                }
            }

            log.debug("Total download time:" + useTime + "(" + useTime / 1000 + "second),Overall average download speed:" + averageDownTotal + "(" + averageDownTotal / 1024 + "kb),Total file size:" + endPosition[downThread.length - 1] / 1024 + " kb,");


            if (isFinished) {

                if (saveFile.exists()) {
                    log.info("The file already exists and is deleted:" + saveFile.getAbsolutePath());
                    saveFile.delete();
                }

                downFile.renameTo(saveFile);

                infoFile.delete();
                log.debug("File download end!");
            }
        } catch (Exception e) {

            hasError = true;
            log.error("Error downloading file:", e);
        }
    }


    public void stopDownload() {
        isStop = true;
        for (int i = 0; i < threadTotal; i++)
            downThread[i].stopDownload();
    }


    private long getFileSize(String fileURL) {
        int returnValue = -1;
        try {

            URL url = new URL(fileURL);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", DownloadConfig.UserAgent);

            int responseCode = httpConnection.getResponseCode();
            if (responseCode >= 400) {

                hasError = true;
                log.debug("The file is unreachable and the HTTP response status code is:" + responseCode);
                return -2;
            }

            String header;
            for (int i = 1; ; i++) {
                header = httpConnection.getHeaderFieldKey(i);
                if (header != null) {
                    if (header.equals("Content-Length")) {
                        returnValue = Integer.parseInt(httpConnection.getHeaderField(header));
                        break;
                    }
                } else
                    break;
            }
        } catch (IOException e) {

            hasError = true;
            log.error("Error getting file size:", e);
        } catch (Exception e) {

            hasError = true;
            log.error("Error getting file size:", e);
        }

        return returnValue;
    }


    public double getDownPercent() {
        double returnValue = 0;


        if (downThread == null)
            return returnValue;

        double total = 0;
        for (int i = 0; i < threadTotal; i++) {
            if (i == 0) {
                total += downThread[i].downPosition;
            } else {
                total += downThread[i].downPosition - downThread[i].startPosition;
            }
        }


        returnValue = ((int) (total / downThread[threadTotal - 1].endPosition * 10000)) / 10000d;

        return returnValue;
    }


    public long getDownTotal() {
        long returnValue = 0;


        if (downThread == null)
            return returnValue;


        long total = 0;
        for (int i = 0; i < threadTotal; i++) {
            total += downThread[i].downPosition - downThread[i].startPosition;
        }

        returnValue = total;

        return returnValue;
    }


    private void writePosition() {
        try {

            DataOutputStream output = new DataOutputStream(new FileOutputStream(infoFile));
            output.writeInt(threadTotal);
            for (int i = 0; i < threadTotal; i++) {
                output.writeLong(downThread[i].downPosition);
                output.writeLong(downThread[i].endPosition);
            }
            output.close();
        } catch (IOException e) {

            hasError = true;
            log.error("Error saving downloaded data amount:", e);
        } catch (Exception e) {

            hasError = true;
            log.error("Error saving downloaded data amount:", e);
        }
    }


    private void readPosition() {
        try {
            DataInputStream input = new DataInputStream(new FileInputStream(infoFile));

            int threadTotal = input.readInt();
            startPosition = new long[threadTotal];
            endPosition = new long[threadTotal];
            for (int i = 0; i < threadTotal; i++) {
                startPosition[i] = input.readLong();
                endPosition[i] = input.readLong();
            }
            input.close();
        } catch (IOException e) {

            hasError = true;
            log.error("Error reading downloaded data amount:", e);
        } catch (Exception e) {

            hasError = true;
            log.error("Error reading downloaded data amount:", e);
        }
    }


    public boolean isFinished() {
        return isFinished;
    }


    public boolean isStop() {
        return isStop;
    }


    public boolean hasError() {
        return hasError;
    }


}