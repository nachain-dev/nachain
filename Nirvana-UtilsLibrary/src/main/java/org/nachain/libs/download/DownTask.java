package org.nachain.libs.download;

public class DownTask {


    private String downFileURL;


    private String saveFilePath;


    private String saveFileName;


    private int threadTotal;

    public DownTask() {
        this("", "", "", 5);
    }

    public DownTask(String downFileURL, String saveFilePath,
                    String saveFileName, int threadTotal) {
        this.downFileURL = downFileURL;
        this.saveFilePath = saveFilePath;
        this.saveFileName = saveFileName;
        this.threadTotal = threadTotal;
    }

    public String getDownFileURL() {
        return downFileURL;
    }

    public void setDownFileURL(String value) {
        downFileURL = value;
    }

    public String getSaveFilePath() {
        return saveFilePath;
    }

    public void setSaveFilePath(String value) {
        saveFilePath = value;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String value) {
        saveFileName = value;
    }

    public int getThreadTotal() {
        return threadTotal;
    }

    public void setThreadTotal(int nCount) {
        threadTotal = nCount;
    }
}