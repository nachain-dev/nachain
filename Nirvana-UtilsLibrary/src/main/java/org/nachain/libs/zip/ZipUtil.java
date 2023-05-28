package org.nachain.libs.zip;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.beans.Bean;
import org.nachain.libs.util.IoUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtil {


    public static int ZIP_LEVEL_0 = 0;
    public static int ZIP_LEVEL_1 = 1;
    public static int ZIP_LEVEL_2 = 2;
    public static int ZIP_LEVEL_3 = 3;
    public static int ZIP_LEVEL_4 = 4;
    public static int ZIP_LEVEL_5 = 5;
    public static int ZIP_LEVEL_6 = 6;
    public static int ZIP_LEVEL_7 = 7;
    public static int ZIP_LEVEL_8 = 8;
    public static int ZIP_LEVEL_9 = 9;


    static final int BUFFER = 8192;

    public static void zip(String inputFileName, String outputZipFileName) {
        zip(inputFileName, outputZipFileName, ZIP_LEVEL_6);
    }


    public static void zip(String inputFileName, String outputZipFileName, int level) {
        ZipOutputStream zos;
        try {
            zos = new ZipOutputStream(new FileOutputStream(outputZipFileName), Charset.forName("GBK"));

            zos.setLevel(level);


            boolean skipFolderEntry = true;

            zip(zos, new File(inputFileName), "", skipFolderEntry);
            zos.closeEntry();
            zos.close();
        } catch (FileNotFoundException e) {
            log.error("The file cannot be found:" + outputZipFileName, e);
        } catch (Exception e) {
            log.error("Error compressing file:", e);
        }

        log.debug("Compression done!");
    }


    private static void zip(ZipOutputStream zos, File inFile, String baseDir, boolean skipFolderEntry) {


        if (!baseDir.equals("") && !baseDir.endsWith(File.separator)) {
            baseDir = baseDir + File.separator;
        }


        if (inFile.isDirectory()) {

            File[] fileArray = inFile.listFiles();

            for (int i = 0; i < fileArray.length; i++) {

                String subBaseDir = baseDir;

                if (!skipFolderEntry) {
                    subBaseDir += inFile.getName();
                }

                zip(zos, fileArray[i], subBaseDir, false);
            }
        } else {

            try {

                String entryName = baseDir + inFile.getName();
                ZipEntry entry = new ZipEntry(entryName);

                zos.putNextEntry(entry);


                int Mode = 2;


                if (Mode == 1) {

                    byte[] data = IoUtil.inputStreamToBytes(new FileInputStream(inFile));

                    zos.write(data);
                } else if (Mode == 2) {
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFile));
                    int count;
                    byte data[] = new byte[BUFFER];
                    while ((count = bis.read(data, 0, BUFFER)) != -1) {
                        zos.write(data, 0, count);
                    }
                    bis.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static List<File> unZip(String zipFileName, String outputDirectory) throws Exception {

        List<File> fileList = new ArrayList<File>();


        ZipFile zipFile = new ZipFile(zipFileName);

        File outputDirectoryFile = new File(outputDirectory);
        if (!outputDirectoryFile.exists()) {
            outputDirectoryFile.mkdirs();
        }


        outputDirectory = outputDirectory.replace('/', File.separatorChar);
        outputDirectory = outputDirectory.replace('\\', File.separatorChar);
        try {
            Enumeration<?> e = zipFile.entries();

            File outputFolder = new File(outputDirectory);
            if (!outputFolder.exists()) {
                outputFolder.mkdirs();
            }
            ZipEntry zipEntry = null;
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
                log.debug("Extract:" + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputDirectory + File.separator + name);

                    f.mkdir();
                    log.debug("Create a directory:" + outputDirectory + File.separator + name);
                } else {

                    String folderName = outputDirectory + File.separator + zipEntry.getName().replace('/', '\\');
                    folderName = folderName.substring(0, folderName.lastIndexOf("\\") + 1);
                    File folderFile = new File(folderName);
                    if (!folderFile.exists()) {
                        folderFile.mkdirs();
                        log.debug("Create LOG_FOLDER_NAME:" + folderName);
                    }

                    File newFile = new File(outputDirectory + File.separator + zipEntry.getName());
                    newFile.createNewFile();
                    InputStream in = zipFile.getInputStream(zipEntry);
                    FileOutputStream out = new FileOutputStream(newFile);


                    int Mode = 2;


                    if (Mode == 1) {

                        byte[] data = IoUtil.inputStreamToBytes(in);
                        out.write(data);
                    } else if (Mode == 2) {
                        BufferedInputStream bis = new BufferedInputStream(in);
                        int count;
                        byte data[] = new byte[BUFFER];
                        while ((count = bis.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        bis.close();
                    }
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException ioe) {
                        log.error("Error closing unzip stream:" + zipFile, ioe);
                    }


                    fileList.add(newFile);
                }
            }
        } catch (Exception e) {
            log.error("Error extracting file:" + zipFileName, e);
        } finally {
            zipFile.close();
            log.debug("Decompression complete!");
        }

        return fileList;
    }


    public static ArrayList<Bean> getAllFile(String zipFile) {
        ArrayList<Bean> returnValue = new ArrayList<Bean>();

        FileInputStream fis = null;
        ZipInputStream zis = null;
        try {
            fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry zipEntry = null;
            String fileName = "";
            while ((zipEntry = zis.getNextEntry()) != null) {
                byte[] data = IoUtil.inputStreamToBytes(zis);
                fileName = zipEntry.getName();
                Bean b = new Bean();
                b.set("FileName", fileName);
                b.set("Data", data);
                returnValue.add(b);
            }

        } catch (FileNotFoundException e) {
            log.error("Unable to find file:" + zipFile, e);
        } catch (IOException e) {
            log.error("Error reading file in zip package:" + zipFile, e);
        } finally {
            try {
                if (zis != null) {
                    zis.close();
                }
                if (zis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                log.error("Error closing file in zip package:" + zipFile, e);
            }
        }

        return returnValue;
    }


    public static void delFile(String zipFile, String delFileName) {

    }


    public static void writeFile() {
    }


}