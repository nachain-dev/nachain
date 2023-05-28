package org.nachain.libs.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.zip.GZIPInputStream;

@Slf4j
public class IoUtil {

    private static final int BUFF_SIZE = 1024;


    public static byte[] inputStreamToBytes(InputStream is) throws IOException {
        return inputStreamToBytes(is, Long.MAX_VALUE);
    }


    public static byte[] inputStreamToBytes(InputStream is, long maxLen) throws IOException {
        return inputStreamToBytes(is, BUFF_SIZE, maxLen);
    }


    public static byte[] inputStreamToBytes(InputStream is, int buffMAX, long maxLen) throws IOException {
        byte[] returnValue = null;


        ByteArrayOutputStream bytestream = new ByteArrayOutputStream(buffMAX);
        byte[] buff = new byte[buffMAX];
        int len = 0;

        long readTotal = 0;

        while (len >= 0) {
            readTotal += len;
            len = is.read(buff);
            if (len >= 0) {
                bytestream.write(buff, 0, len);

                if (maxLen > 0 && readTotal >= maxLen) {
                    break;
                }
            }
        }

        returnValue = bytestream.toByteArray();
        bytestream.close();

        return returnValue;
    }


    public static byte[] gzipInputStreamToBytes(GZIPInputStream in) throws IOException {
        byte[] returnValue = null;

        final int buffMAX = 1024 * 4;
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream(buffMAX);
        byte[] buff = new byte[buffMAX];
        int len;
        while ((len = in.read(buff)) >= 0) {
            bytestream.write(buff, 0, len);
        }
        returnValue = bytestream.toByteArray();
        bytestream.close();
        in.close();

        return returnValue;
    }


    public static byte[] fileToBytes(String filePath) throws IOException {
        byte[] context = fileToBytes(new File(filePath));
        return context;
    }


    public static byte[] fileToBytes(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4086];
        int count = 0;
        while ((count = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, count);
        }
        byte[] context = baos.toByteArray();
        baos.close();
        fis.close();

        return context;
    }


    public static void bytesToFile(byte[] data, String filePath) throws IOException {
        bytesToFile(data, new File(filePath));
    }


    public static void bytesToFile(byte[] data, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.close();
    }


    public static boolean isUTF8(byte[] bytes) {
        boolean returnValue = false;

        if (bytes == null || bytes.length < 3) {
            return false;
        }


        if (bytes[0] == -17 && bytes[1] == -69 && bytes[2] == -65) {
            returnValue = true;
        } else {
            returnValue = false;
        }
        return returnValue;
    }


    public static Object cloneObject(Object obj) {
        Object returnValue = null;
        try {

            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            returnValue = oi.readObject();
        } catch (Exception e) {
            log.error("clone object error", e);
        }
        return returnValue;
    }


    public static int countObjectSize(Serializable o) {

        int ret = 0;
        class DumbOutputStream extends OutputStream {
            int count = 0;

            public void write(int b) throws IOException {
                count++;
            }
        }
        DumbOutputStream buf = new DumbOutputStream();
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(buf);
            os.writeObject(o);
            ret = buf.count;
        } catch (IOException e) {

            e.printStackTrace();
            ret = -1;
        } finally {
            try {
                os.close();
            } catch (Exception e) {
            }
        }
        return ret;
    }

}