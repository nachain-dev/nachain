package org.nachain.libs.util;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RAFileUtil {

    private RandomAccessFile file;

    @SuppressWarnings("unused")
    public RAFileUtil() {
    }


    public RAFileUtil(String fileName) throws IOException {
        file = new RandomAccessFile(fileName, "rw");
    }


    public RAFileUtil(String fileName, long position) throws IOException {
        file = new RandomAccessFile(fileName, "rw");

        file.seek(position);
    }


    public synchronized int write(byte[] b, int nStart, int nLen) throws IOException {
        int n = -1;
        try {
            file.write(b, nStart, nLen);
            n = nLen;
        } catch (IOException e) {
            throw new IOException("Error writing file:", e);
        }
        return n;
    }


    public synchronized int read(byte[] b, int nStart, int nLen) throws IOException {
        int n = -1;
        try {
            n = file.read(b, nStart, nLen);
        } catch (IOException e) {
            throw new IOException("Error reading file:", e);
        }
        return n;
    }

    public void close() throws IOException {
        try {
            file.close();
        } catch (IOException e) {
            throw new IOException("Error closing file:", e);
        }
    }
}