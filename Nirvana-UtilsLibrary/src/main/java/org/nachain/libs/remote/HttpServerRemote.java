package org.nachain.libs.remote;

import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class HttpServerRemote {

    private HttpServletResponse response;

    private ByteArrayOutputStream baos = null;

    private ObjectOutputStream oos = null;

    @SuppressWarnings("unused")
    private HttpServerRemote() {
    }

    public HttpServerRemote(HttpServletResponse response) throws IOException {
        this.response = response;
        this.response.reset();
        this.response.setContentType("application/stream");
        this.response.setHeader("Accept-Ranges", "bytes");

        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
    }


    public void writeObject(Object obj) throws IOException {
        oos.writeObject(obj);
    }


    public void close() throws IOException {
        oos.flush();
        oos.close();
        baos.close();

        byte[] content = baos.toByteArray();

        response.getOutputStream().write(content);
    }

}