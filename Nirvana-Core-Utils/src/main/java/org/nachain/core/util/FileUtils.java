package org.nachain.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {


    public static String hashFile(String filePath) throws IOException, NoSuchAlgorithmException {
        return Hex.encode0x(hash256File(filePath));
    }


    public static byte[] hash256File(String filePath) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePath);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        int bufferSize = 16384 * 10;
        byte[] buffer = new byte[bufferSize];
        int sizeRead;
        while ((sizeRead = in.read(buffer)) != -1) {
            digest.update(buffer, 0, sizeRead);
        }
        in.close();
        return digest.digest();
    }


    public static boolean isDir(String path) {
        File file = new File(path);
        return file.isDirectory();
    }


    public static void deleteDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.isDirectory()) return;

        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {

                if (".".equals(file.getName()) || "..".equals(file.getName())) {
                    continue;
                }

                if (file.isDirectory()) {
                    deleteDir(file.getAbsolutePath());
                } else {
                    file.delete();
                }
            }
        }

        dir.delete();
    }


    public static List<String> iterDir(String dirPath) {
        List<String> filePathList = new ArrayList<String>();
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            return filePathList;
        }

        iterDir(dir, filePathList);
        return filePathList;
    }


    private static void iterDir(File dir, List<String> filePathList) {
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    iterDir(file, filePathList);
                } else {
                    filePathList.add(file.getName());
                }
            }
        }
    }

}