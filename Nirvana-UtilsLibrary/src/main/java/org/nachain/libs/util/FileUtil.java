package org.nachain.libs.util;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.util.sort.FileSortUtil;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FileUtil {

    public final static String IMAGE_FILE_EXT = "jpg;jpeg;png;gif;bmp;ico";

    public final static String EXE_FILE_EXT = "exe;com;bat";

    public final static String BUILDTIME_FORMAT = "yyyy-MM-dd HH:mm";


    public static Date getCreateTime(File f) {

        Date returnValue = null;

        String filePath = f.getPath();
        String fileName = f.getName();

        try {


            String cmd = "cmd /C dir \"" + filePath + "\" /tc";

            Process p = Runtime.getRuntime().exec(cmd);
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String strTime = null;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.endsWith(fileName)) {
                    strTime = line.substring(0, 17);
                    break;
                }
            }

            returnValue = DateUtil.parse(strTime, BUILDTIME_FORMAT);
        } catch (IOException e) {
            log.error("getCreateTime error", e);
        }

        return returnValue;
    }


    public static Date getModifiedTime(File f) {
        long time = f.lastModified();
        Date lastModified = new Date(time);
        return lastModified;
    }


    public static boolean isImgageFile(String fileName) {
        String[] exts = IMAGE_FILE_EXT.split(";");
        String file = fileName.toLowerCase();
        for (int i = 0; i < exts.length; i++)
            if (file.endsWith("." + exts[i]))
                return true;
        return false;
    }


    public static boolean isExeFile(String fileName) {
        String[] exts = EXE_FILE_EXT.split(";");
        String file = fileName.toUpperCase();
        for (int i = 0; i < exts.length; i++)
            if (file.endsWith("." + exts[i]))
                return true;
        return false;
    }


    public static boolean saveFile(InputStream in, String fileName) {
        File outFile = new File(fileName);
        try {
            FileOutputStream out = new FileOutputStream(outFile);
            byte[] temp = new byte[1024];
            int length = -1;
            while ((length = in.read(temp)) > 0) {
                out.write(temp, 0, length);
            }
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            log.error("saveFile error " + outFile.getName(), e);
            return false;
        }
        return true;
    }


    public static boolean saveFile(String data, String fileName) {
        try {
            BufferedWriter buff = new BufferedWriter(new FileWriter(fileName));
            buff.write(data);
            buff.flush();
            buff.close();
        } catch (Exception e) {
            log.error("saveFile error:" + fileName, e);
            return false;
        }
        return true;
    }


    public static void saveFile(String savePath, String data, String chatset) {
        FileOutputStream fos = null;
        OutputStreamWriter writer = null;
        try {
            File file = new File(savePath);
            fos = new FileOutputStream(file);
            writer = new OutputStreamWriter(fos, chatset);
            writer.write(data);
            writer.flush();
        } catch (Exception e) {
            log.error("saveFile error:" + savePath + ", Chatset:" + chatset, e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("close error", e);
                }
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                log.error("close error", e);
            }

        }
    }


    public static String getFileFolderPath(String fileFullPath) {
        String fileName = "";


        fileFullPath = fileFullPath.replaceAll("/", "\\\\");


        String regExp = "(.+)\\\\(.+)$";


        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(fileFullPath);
        boolean result = m.find();
        if (result) {
            fileName = m.group(1);
            if (!fileName.endsWith("\\")) {
                fileName = fileName.concat("\\");
            }
        }


        fileName = CommUtil.replace(fileName, "/", File.separator);
        fileName = CommUtil.replace(fileName, "\\", File.separator);

        return fileName;
    }


    public static String getFileName(String fileFullPath) {
        String fileName = "";


        fileFullPath = fileFullPath.replaceAll("/", "\\\\");


        String regExp = ".+\\\\(.+)$";


        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(fileFullPath);
        boolean result = m.find();
        if (result) {
            fileName = m.group(1);
        }

        return fileName;
    }


    public static String getFileExt(String fileName) {
        String fileExt = "";


        String regExp = "(\\.\\w+)$";


        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(fileName);
        boolean result = m.find();
        if (result) {
            fileExt = m.group(0);
        }

        return fileExt;
    }


    public static String getFileNamePrefix(String fileName) {
        String returnValue = "";

        File file = new File(fileName);
        returnValue = file.getName();
        if (returnValue.contains(".")) {
            returnValue = returnValue.substring(0, returnValue.lastIndexOf("."));
        }

        return returnValue;
    }


    public static void setHidden(File file) {


        String cmd = "attrib +H +S \"" + file.getAbsolutePath() + "\"";

        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            log.error("setHidden error: " + cmd, e);
        }
    }


    public static void setNoHidden(File file) {


        String cmd = "attrib -H \"" + file.getAbsolutePath() + "\"";

        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            log.error("setNoHidden error:" + cmd, e);
        }
    }


    public static boolean delFile(String filePath, long sleepTime) {
        boolean returnValue = false;

        File file = new File(filePath);

        if (!file.exists()) {
            return returnValue;
        }


        if (file.isDirectory()) {
            String[] fileList = file.list();
            for (int i = 0; i < fileList.length; i++) {
                File _delFile = null;

                boolean isDeleted = false;

                if (filePath.endsWith(File.separator)) {
                    _delFile = new File(filePath + fileList[i]);
                } else {
                    _delFile = new File(filePath + File.separator + fileList[i]);
                }


                if (_delFile.isFile()) {
                    _delFile.delete();
                    isDeleted = true;
                } else if (_delFile.isDirectory()) {

                    delFile(filePath + "/" + fileList[i], sleepTime);

                    if (file.exists()) {
                        file.delete();
                    }
                    returnValue = true;
                }


                if (sleepTime > 0) {
                    try {
                        if (isDeleted) {
                            Thread.sleep(sleepTime);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


            if (file.exists()) {
                file.delete();
            }

        } else {
            file.delete();
        }

        return returnValue;
    }


    public static void copyFolder(String srcDirector, String desDirector) throws IOException {
        File srcFile = new File(srcDirector);
        File desFile = new File(desDirector);

        if (!srcFile.exists()) {
            return;
        }

        if (!desFile.exists()) {
            desFile.mkdirs();
        }
        File[] file = srcFile.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                log.debug("copy:" + file[i].getAbsolutePath() + "-->" + desDirector + "/" + file[i].getName());
                FileInputStream fis = new FileInputStream(file[i]);
                FileOutputStream fos = new FileOutputStream(desDirector + "/" + file[i].getName());
                byte[] b = new byte[1024 * 5];
                int len;
                while ((len = fis.read(b)) != -1) {
                    fos.write(b, 0, len);
                }
                fos.flush();
                fos.close();
                fis.close();
            }

            if (file[i].isDirectory()) {
                log.debug("copy:" + file[i].getAbsolutePath() + "-->" + desDirector + "/" + file[i].getName());
                copyFolder(srcDirector + "/" + file[i].getName(), desDirector + "/" + file[i].getName());
            }
        }
    }


    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {

            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));


            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));


            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }

            outBuff.flush();
        } finally {

            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }


    public static boolean copyFileFast(File srcFile, File destFile) {
        if (!srcFile.exists()) {
            log.error("source file not exists");
            return false;
        } else {

            File folder = destFile.getParentFile();
            if (!folder.exists()) {
                folder.mkdirs();
            }

            FileChannel fci = null;
            FileChannel fco = null;
            try {
                fci = new FileInputStream(srcFile).getChannel();
                fco = new FileOutputStream(destFile).getChannel();
                fci.transferTo(0, fci.size(), fco);
            } catch (FileNotFoundException e) {
                log.error("file not found", e);
            } catch (IOException e) {
                log.error("copy file error", e);
            } finally {
                try {
                    if (fci != null)
                        fci.close();
                    if (fco != null)
                        fco.close();
                } catch (IOException e) {
                    log.error("close file stream error", e);
                }
            }
        }
        return true;
    }


    public static boolean moveFile(File srcFile, File destFile) {
        boolean returnValue = false;


        boolean state = copyFileFast(srcFile, destFile);
        if (state) {

            returnValue = srcFile.delete();
        }

        return returnValue;
    }


    public static Object readSerialize(String serializeFilePath) {
        Object obj = null;

        File file = new File(serializeFilePath);
        if (file.exists()) {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            ObjectInputStream ois = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                ois = new ObjectInputStream(bis);
                obj = ois.readObject();
            } catch (ClassNotFoundException e) {
                log.error("file not found:" + file.getPath(), e);
            } catch (IOException e) {
                log.error("file serialize error", e);
            } finally {
                if (ois != null)
                    try {
                        ois.close();
                    } catch (IOException e) {
                        log.error("file serialize error", e);
                    }
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException e) {
                        log.error("file serialize error", e);
                    }
            }
        }

        return obj;
    }


    public static void saveSerialize(String serializeFilePath, Object obj) {
        File serFile = new File(serializeFilePath);

        File serFolder = serFile.getParentFile();
        if (!serFolder.exists()) {
            serFolder.mkdirs();
        }
        if (serFile.exists()) {
            serFile.delete();
        }

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {

            fos = new FileOutputStream(serFile);
            bos = new BufferedOutputStream(fos);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
        } catch (FileNotFoundException e) {
            log.error("file not found::" + serFile.getPath(), e);
        } catch (IOException e) {
            log.error("file serialize error", e);
        } finally {
            if (oos != null)
                try {
                    oos.close();
                } catch (IOException e) {
                    log.error("file serialize error", e);
                }
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("file serialize error", e);
                }
        }
    }


    public static void buildFolder(String fileFullPath) {

        String folderPath = FileUtil.getFileFolderPath(fileFullPath);

        File folderFile = new File(folderPath);

        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
    }


    public static String rename(String oldFileFullPatn, String newFileName) {


        String folderPath = FileUtil.getFileFolderPath(oldFileFullPatn);
        String newFileFullPath = folderPath + newFileName;


        return renameTo(oldFileFullPatn, newFileFullPath);
    }


    public static String renameTo(String oldFileFullPatn, String newFileFullPath) {

        if (!oldFileFullPatn.equals(newFileFullPath)) {
            File oldfile = new File(oldFileFullPatn);
            File newfile = new File(newFileFullPath);


            if (!oldfile.exists()) {
                return newFileFullPath;
            }

            if (!newfile.exists())
                oldfile.renameTo(newfile);
        }

        return newFileFullPath;
    }


    public static PrintWriter getFileStream(String filePath, long fileMaxSize) throws Exception {
        PrintWriter pw = null;


        File folder = new File(filePath.substring(0, filePath.lastIndexOf("/") + 1));
        if (!folder.exists()) {
            folder.mkdirs();
        }


        File currentFile = new File(filePath);

        if (currentFile.length() > fileMaxSize) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String currentTime = sdf.format(new Date(System.currentTimeMillis()));

            String fileExtName = FileUtil.getFileExt(filePath);

            String newBakFileName = filePath.substring(0, filePath.length() - fileExtName.length());

            newBakFileName = newBakFileName + "_bak" + currentTime + fileExtName;

            File backupFile = new File(newBakFileName);
            try {

                currentFile.renameTo(backupFile);
            } catch (Exception e) {
                throw new Exception("getFileStream error", e);
            }
        }
        try {

            FileOutputStream os = new FileOutputStream(filePath, true);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            pw = new PrintWriter(bos, true);
        } catch (Exception e) {
            throw new Exception("getFileStream error", e);
        }

        return pw;
    }


    public static boolean mkdirs(String filePath) {
        boolean rtv = false;

        File serFolder = new File(filePath).getParentFile();
        if (!serFolder.exists()) {
            rtv = serFolder.mkdirs();
        }

        return rtv;
    }


    public static int deleteCycle(String folderPath, int keepTotal) {
        return deleteCycle(folderPath, keepTotal, "*");
    }


    public static int deleteCycle(String folderPath, int keepTotal, String delFileExt) {
        int delCount = 0;


        List<File> fileList = FileSortUtil.upByDate(folderPath);

        int fileTotal = 0;


        if (!delFileExt.equals("*")) {
            for (File file : fileList) {
                if (file.getName().toLowerCase().endsWith(delFileExt.toLowerCase())) {
                    fileTotal++;
                }
            }
        } else {
            fileTotal = fileList.size();
        }


        if (fileTotal > keepTotal) {

            int delTotal = fileTotal - keepTotal;


            if (delTotal > 0) {
                for (File file : fileList) {

                    if (delFileExt.equals("*") || file.getName().toLowerCase().endsWith(delFileExt.toLowerCase())) {

                        delCount++;
                        file.delete();
                    }


                    if (delCount >= delTotal) {
                        break;
                    }
                }
            }
        }

        return delCount;
    }


    @Deprecated
    public static String toShortPath(String path) {
        String rtv = path;


        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            String shortPath = "";

            File file = new File(path);
            String fullPath = file.getPath();


            String[] folderArray = fullPath.split("\\\\");
            for (String folder : folderArray) {

                if (folder.contains(":")) {
                    shortPath += folder;
                    shortPath += File.separator;
                    continue;
                }

                if (folder.contains(" ") || folder.length() >= 8) {

                    int shortIndex = getShortIndex(shortPath, folder);

                    if (shortIndex != 0) {

                        shortPath += folder.substring(0, 6) + "~" + shortIndex;
                        shortPath += File.separator;
                        continue;
                    }
                }

                shortPath += folder;
                shortPath += File.separator;
            }

            rtv = shortPath;
        }

        return rtv;
    }


    @Deprecated
    public static int getShortIndex(String parentFolder, String findFolder) {

        int index = 0;


        String checkPrefix = findFolder.substring(0, 6);


        File folderFile = new File(parentFolder);

        if (!folderFile.exists()) {
            throw new RuntimeException("folder not exist:" + parentFolder);
        }

        File[] fileList = folderFile.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            File file = fileList[i];


            if (file.getName().startsWith(checkPrefix) && file.isDirectory()) {
                index++;
            }


            if (file.getName().equals(findFolder)) {
                break;
            }
        }


        if (index > 4) {
            log.warn("max short path count:" + index);

            index = 0;
        }

        return index;
    }

    public static void main(String args[]) {


    }

}