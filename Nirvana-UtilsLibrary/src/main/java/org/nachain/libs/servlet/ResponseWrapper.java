package org.nachain.libs.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.StringUtil;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ResponseWrapper extends HttpServletResponseWrapper {


    private PrintWriter pw;

    private CharArrayWriter charArrayWriter;


    private ResponseOutputStream ros;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        charArrayWriter = new CharArrayWriter();
    }


    public PrintWriter getWriter() {
        if (pw == null) {
            pw = new PrintWriter(charArrayWriter);
        }

        return pw;
    }


    public ServletOutputStream getOutputStream() {


        if (ros == null) {
            ros = new ResponseOutputStream();
        }

        return ros;
    }


    public String getWriterData() {
        return charArrayWriter.toString();
    }


    public byte[] getOutputStreamData() {
        if (ros != null)
            return ros.getBytes();

        return null;
    }


    public boolean isStringData() {
        if (charArrayWriter.size() != 0)
            return true;

        return false;
    }


    public boolean isByteData() {
        if (ros != null)
            return true;

        return false;
    }


    public String getStringData() throws UnsupportedEncodingException {
        String CharacterEncoding = CommUtil.null2String(this.getCharacterEncoding());
        if (CharacterEncoding.equals("")) {
            CharacterEncoding = StringUtil.CHATSET_UTF_8;
        }
        return getStringData(CharacterEncoding);
    }


    public String getStringData(String CharacterEncoding) throws UnsupportedEncodingException {
        String returnValue = "";


        if (isByteData()) {
            byte[] dataArray = getOutputStreamData();
            if (dataArray != null) {
                returnValue = new String(dataArray, CharacterEncoding);
            }
        } else if (isStringData()) {
            returnValue = getWriterData();
        }

        return returnValue;
    }


    public void finalize() throws Throwable {
        super.finalize();
        charArrayWriter.close();
    }

}