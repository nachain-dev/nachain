package org.nachain.libs.servlet;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ResponseOutputStream extends ServletOutputStream {


    private ByteArrayOutputStream bos;

    public ResponseOutputStream() {
        bos = new ByteArrayOutputStream();
    }

    public boolean isReady() {
        return false;
    }

    public void setWriteListener(WriteListener writeListener) {

    }


    public void write(int param) throws IOException {
        bos.write(param);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        bos.write(b, off, len);
    }

    protected byte[] getBytes() {
        return bos.toByteArray();
    }

    public void flush() throws IOException {
        bos.flush();
    }

    public void close() throws IOException {
        bos.close();
    }
}