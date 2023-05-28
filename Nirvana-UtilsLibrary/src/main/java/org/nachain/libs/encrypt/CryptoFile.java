package org.nachain.libs.encrypt;

import org.nachain.libs.util.FileUtil;
import org.nachain.libs.util.IoUtil;
import org.nachain.libs.util.RAFileUtil;
import org.nachain.libs.util.RandomUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class CryptoFile {


    private static final int BUFFER_SIZE = 1 * 1024 * 1024;


    public final static String srcSuffix = ".src";


    public static void readFileHeader() {

    }


    public static String getResourcesConfigFile(String resourcesFile) throws URISyntaxException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(resourcesFile);
        if (url != null) {
            URI uri = url.toURI();
            return new File(uri).getPath();
        }

        return null;
    }


    public static void fileEncrypt(String filePath, byte[] keyByte) throws Exception {

        File file = new File(filePath);

        String tempFileName = filePath + RandomUtil.getRandomStringNum(5);


        CryptoFile.fileEncrypt(file.getPath(), tempFileName, keyByte);

        file.delete();

        new File(tempFileName).renameTo(file);
    }


    public static void fileDecrypt(String filePath, byte[] keyByte) throws Exception {

        File file = new File(filePath);

        String tempFileName = filePath + RandomUtil.getRandomStringNum(5);

        CryptoFile.fileDecrypt(file.getPath(), tempFileName, keyByte);

        file.delete();

        new File(tempFileName).renameTo(file);
    }


    public static void fileEncrypt(String sourceFilePath, String saveFilePath, byte[] keyByte) throws Exception {


        File sourceFile = new File(sourceFilePath);

        File saveFile = new File(saveFilePath);

        if (!sourceFile.exists()) {
            throw new FileNotFoundException("The file to read does not exist:" + sourceFilePath);
        }

        if (saveFile.exists()) {
            saveFile.delete();
        }


        RAFileUtil readFile = new RAFileUtil(sourceFilePath, 0);
        RAFileUtil writeFile = new RAFileUtil(saveFilePath, 0);


        long sourceFileSize = sourceFile.length();


        int bufferSize = BUFFER_SIZE;


        if (bufferSize > sourceFileSize) {
            bufferSize = (int) sourceFileSize;
        }


        int readTotal = 0;
        long readAllTotal = 0;
        byte[] b = new byte[bufferSize];
        while (readAllTotal < sourceFileSize) {

            readTotal = readFile.read(b, 0, bufferSize);

            byte[] tmp = new byte[readTotal];
            System.arraycopy(b, 0, tmp, 0, readTotal);


            byte[] data = CryptoAESUtil.encrypt(tmp, keyByte);


            writeFile.write(data, 0, data.length);


            readAllTotal += readTotal;


        }
        readFile.close();
        writeFile.close();

    }


    public static void fileDecrypt(String sourceFilePath, String saveFilePath, byte[] keyByte) throws Exception {


        File sourceFile = new File(sourceFilePath);

        File saveFile = new File(saveFilePath);

        if (!sourceFile.exists()) {
            throw new FileNotFoundException("The file to read does not exist:" + sourceFilePath);
        }

        if (saveFile.exists()) {
            saveFile.delete();
        }


        RAFileUtil readFile = new RAFileUtil(sourceFilePath, 0);
        RAFileUtil writeFile = new RAFileUtil(saveFilePath, 0);


        long sourceFileSize = sourceFile.length();


        int maxSize = BUFFER_SIZE;
        int bufferSize = maxSize;


        if (sourceFileSize > maxSize) {
            bufferSize += 16;
        }


        if (bufferSize > sourceFileSize) {
            bufferSize = (int) sourceFileSize;
        }


        int readTotal = 0;
        long readAllTotal = 0;
        byte[] b = new byte[bufferSize];
        while (readAllTotal < sourceFileSize) {

            readTotal = readFile.read(b, 0, bufferSize);

            byte[] tmp = new byte[readTotal];
            System.arraycopy(b, 0, tmp, 0, readTotal);


            byte[] data = CryptoAESUtil.decrypt(tmp, keyByte);


            writeFile.write(data, 0, data.length);


            readAllTotal += readTotal;
        }
        readFile.close();
        writeFile.close();
    }


    public static byte[] decodeConfigData(byte[] encodeData, byte[] keyByte) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] decodeData = CryptoAESUtil.decrypt(encodeData, keyByte);

        return decodeData;
    }


    public static byte[] getConfigFileData(String confFilePath, byte[] keyByte) throws Exception {
        byte[] returnValue = null;


        File confFile = new File(confFilePath);
        File srcFile = new File(confFilePath.concat(srcSuffix));


        if (confFile.exists()) {

            if (srcFile.exists()) {

                if (srcFile.length() == 0) {

                    byte[] encodeData = IoUtil.fileToBytes(confFile);
                    byte[] decodeData = CryptoAESUtil.decrypt(encodeData, keyByte);


                    IoUtil.bytesToFile(decodeData, srcFile);


                    returnValue = decodeData;
                } else {

                    byte[] data = IoUtil.fileToBytes(srcFile);
                    byte[] encodeData = CryptoAESUtil.encrypt(data, keyByte);


                    IoUtil.bytesToFile(encodeData, confFile);


                    returnValue = data;
                }
            } else {

                byte[] data = IoUtil.fileToBytes(confFile);
                byte[] decodeData = CryptoAESUtil.decrypt(data, keyByte);


                returnValue = decodeData;
            }
        } else {

            if (srcFile.exists()) {

                byte[] data = IoUtil.fileToBytes(srcFile);

                byte[] encodeData = CryptoAESUtil.encrypt(data, keyByte);


                IoUtil.bytesToFile(encodeData, confFile);


                returnValue = data;
            } else {
                throw new FileNotFoundException("The file to read does not exist:" + confFilePath + ",and" + srcSuffix + " source file does not exist!");
            }
        }

        return returnValue;
    }


    public static String configFileCrypt(String confFilePath, byte[] keyByte) throws Exception {
        String returnValue = null;

        String srcFilePath = confFilePath.concat(srcSuffix);
        File confFile = new File(confFilePath);
        File srcFile = new File(srcFilePath);

        if (confFile.exists()) {

            if (srcFile.exists()) {

                if (srcFile.length() == 0) {

                    fileDecrypt(confFilePath, srcFilePath, keyByte);
                } else {

                    fileEncrypt(srcFilePath, confFilePath, keyByte);
                }
            }
        } else {
            if (srcFile.exists()) {

                fileEncrypt(srcFilePath, confFilePath, keyByte);
            } else {
                throw new FileNotFoundException("The file to read does not exist:" + confFilePath + ",and the.src source file does not exist!");
            }
        }


        if (confFile.exists()) {


            returnValue = FileUtil.getFileFolderPath(confFilePath).concat(FileUtil.getFileName(confFilePath)).concat(".tmp");


            fileDecrypt(confFilePath, returnValue, keyByte);
        }

        return returnValue;
    }


}