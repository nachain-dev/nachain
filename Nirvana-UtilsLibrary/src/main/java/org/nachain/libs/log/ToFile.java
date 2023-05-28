package org.nachain.libs.log;

import org.nachain.libs.util.FileUtil;

import java.io.PrintWriter;


public class ToFile {


    private static PrintWriter getFileStream(String filePath) {
        PrintWriter pw;

        try {

            pw = FileUtil.getFileStream(filePath, 1024 * 1024);
        } catch (Exception e) {
            pw = new PrintWriter(System.err, true);
            pw.println("Log failed to get the file write stream object:\r\n");
            pw.println(ContentService.getException(e));
        }

        return pw;
    }


    public static void writeFile(String logName, String loggerLevel, String filePath, String info) {

        String content = ContentService.getContent(logName, loggerLevel, info);


        PrintWriter pw = getFileStream(filePath);
        pw.println(content);
        pw.flush();
        pw.close();
    }
}