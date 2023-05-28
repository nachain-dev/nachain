package org.nachain.libs.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.beans.Field;
import org.nachain.libs.httpclient.bean.RequestHeaderFields;
import org.nachain.libs.httpclient.bean.RequestHttp;
import org.nachain.libs.httpclient.bean.ResponseHeaderFields;
import org.nachain.libs.httpclient.bean.ResponseHttp;
import org.nachain.libs.util.CommUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
public class HttpClientService {


    public static RequestHttp buildRequestHttp() {
        return buildRequestHttp("", "utf-8");
    }


    public static RequestHttp buildRequestHttp(String URLAddress, String ContentEncoding) {
        RequestHttp requestHttp = new RequestHttp();
        requestHttp.setMethod("GET");
        requestHttp.setProtocol("HTTP/1.1");
        requestHttp.setURLAddress(URLAddress);
        requestHttp.setHeaderFields(buildHeaderFields());
        requestHttp.setContent(null);
        requestHttp.setContentEncoding(ContentEncoding);

        return requestHttp;
    }


    public static RequestHttp buildRequestHttp(String URLAddress) {
        return buildRequestHttp(URLAddress, null);
    }


    public static ArrayList<Field> buildHeaderFields() {
        RequestHeaderFields hf = new RequestHeaderFields();
        return hf.buildRequestHeaderFields();
    }


    public static String getHTML(ResponseHttp responseHttp) throws UnsupportedEncodingException {
        String returnValue = "";
        byte[] Content = responseHttp.getContent();
        String ContentEncoding = responseHttp.getContentEncoding();
        if (Content != null) {
            if (ContentEncoding == null) {
                returnValue = new String(Content);
            } else {
                returnValue = new String(Content, ContentEncoding);
            }
        }
        return returnValue;
    }


    public static Map<String, Object> analyzeHeaderContent(byte[] data) throws IOException {
        Map<String, Object> returnValue = new HashMap<String, Object>();
        String Header = "";
        byte[] Content = new byte[0];

        ByteArrayInputStream is = new ByteArrayInputStream(data);


        int step = 1;

        boolean isSpace;

        int readTotal = 0;

        int newline = 0;

        while (true) {

            int c = is.read();
            readTotal++;


            if (c == -1)
                break;


            isSpace = Character.isWhitespace((char) c);

            switch (step) {

                case 1:
                    if (isSpace) {

                        continue;
                    }
                    step = 2;


                case 2:

                    if ((char) c == '\n') {
                        newline++;

                    } else if ((char) c != '\r') {
                        newline = 0;
                    }
                    if (newline >= 2) {
                        step = 3;
                        continue;
                    }
                    Header = Header + (char) c;

                    break;

                case 3:
                    Content = new byte[data.length - (readTotal - 1)];
                    ByteArrayOutputStream contentBAOS = new ByteArrayOutputStream();
                    contentBAOS.write(c);

                    step = 4;
                    int ir;
                    byte bytes[] = new byte[4086];
                    while (true) {

                        if ((ir = is.read(bytes)) > 0) {
                            contentBAOS.write(bytes, 0, ir);
                        } else if (ir < 0)
                            break;
                    }
                    Content = contentBAOS.toByteArray();

                    break;
            }
        }
        log.debug("Header:\r\n" + Header);
        log.debug("Content:\r\n" + new String(Content));

        returnValue.put("Header", Header);
        returnValue.put("Content", Content);

        return returnValue;
    }


    public static byte[] analyzeRequest2Byte(RequestHttp http) throws IOException {
        return analyze2RequestByte(http);
    }


    public static byte[] analyzeResponse2Byte(ResponseHttp http) throws IOException {
        return analyze2ResponseByte(http);
    }


    private static byte[] analyze2RequestByte(RequestHttp http) throws IOException {
        byte[] returnValue = null;

        ByteArrayOutputStream dateBAOS = new ByteArrayOutputStream();


        String Header = http.getMethod() + " " + http.getURI() + " " + http.getProtocol() + "\r\n";

        List<?> headerList = http.getHeaderFields();
        ListIterator<?> listIter = headerList.listIterator();
        while (listIter.hasNext()) {
            Field field = (Field) listIter.next();
            if (field.getValue() != null)
                Header += field.getName() + ":" + field.getValue() + "\r\n";
        }
        Header += "\r\n";

        dateBAOS.write(Header.getBytes());


        dateBAOS.write(http.getContent());

        returnValue = dateBAOS.toByteArray();

        return returnValue;
    }


    private static byte[] analyze2ResponseByte(ResponseHttp http) throws IOException {
        byte[] returnValue = null;

        ByteArrayOutputStream dateBAOS = new ByteArrayOutputStream();


        String Header = http.getProtocol() + " " + http.getResponseCode() + " " + http.getResponseMessage() + "\r\n";
        List<?> headerList = http.getHeaderFields();
        ListIterator<?> listIter = headerList.listIterator();
        while (listIter.hasNext()) {
            Field field = (Field) listIter.next();
            if (field.getValue() != null)
                Header += field.getName() + ":" + field.getValue() + "\r\n";
        }
        Header += "\r\n";

        dateBAOS.write(Header.getBytes());


        dateBAOS.write(http.getContent());

        returnValue = dateBAOS.toByteArray();

        return returnValue;
    }


    /**
     * @return
     * @throws IOException
     */
    public static RequestHttp analyze2RequestHttp(byte[] data) throws IOException {
        RequestHttp http = new RequestHttp();

        Map analyzeMap = analyzeHeaderContent(data);
        String header = (String) analyzeMap.get("Header");
        byte[] content = (byte[]) analyzeMap.get("Content");

        RequestHeaderFields headerFields = new RequestHeaderFields();

        header = header.replaceAll("\r", "");
        String[] lines = header.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (i == 0) {
                String[] Command = line.split(" ");
                String URI = Command[1];
                if (URI.indexOf("://") != -1) {
                    URI = URI.substring(URI.indexOf("://") + 3);
                    URI = URI.substring(URI.indexOf("/"));
                }

                http.setMethod(Command[0]);
                http.setURI(URI);
                http.setProtocol(Command[2]);
            } else {
                String name = line.substring(0, line.indexOf(":"));
                String value = line.substring(line.indexOf(":") + 1);

                name = name.trim();
                value = value.trim();

                Field field = new Field();
                field.setName(name);
                field.setValue(value);

                //if (name.equals("Accept")) {
                //	headerFields.setAccept(value);
                //} else if (name.equals("Referer")) {
                //	headerFields.setReferer(value);
                //} else if (name.equals("Accept_Language")) {
                //	headerFields.setAccept_Language(value);
                //} else if (name.equals("User_Agent")) {
                //	headerFields.setUser_Agent(value);
                //} else if (name.equals("UA_CPU")) {
                //	headerFields.setUA_CPU(value);
                //} else if (name.equals("Accept_Encoding")) {
                //	headerFields.setAccept_Encoding(value);
                //} else if (name.equals("Host")) {
                //	headerFields.setHost(value);
                //} else if (name.equals("Pragma")) {
                //	headerFields.setPragma(value);
                //} else if (name.equals("Cookie")) {
                //	headerFields.setCookie(value);
                //}
            }
        }
        http.setContentType(null);
        http.setContentEncoding(null);
        //http.setHost(headerFields.getHost());

        String URLAddress = http.getURI();
        if (!URLAddress.startsWith("http://") && !URLAddress.startsWith("https://")) {
            //URLAddress = "http://" + headerFields.getHost() + (URLAddress.startsWith("/") ? "" : "/") + URLAddress;
        }
        http.setURLAddress(URLAddress);

        http.setHeaderFields(headerFields.buildRequestHeaderFields());

        http.setContent(content);

        return http;
    }

    /**
     * @return
     * @throws IOException
     */
    public static ResponseHttp analyze2ResponseHttp(byte[] data) throws IOException {
        ResponseHttp rspHttp = new ResponseHttp();

        Map analyzeMap = analyzeHeaderContent(data);
        String header = (String) analyzeMap.get("Header");
        byte[] content = (byte[]) analyzeMap.get("Content");

        ResponseHeaderFields headerFields = new ResponseHeaderFields();

        header = header.replaceAll("\r", "");
        String[] lines = header.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (i == 0) {
                String[] Command = line.split(" ");
                rspHttp.setProtocol(Command[0]);
                rspHttp.setResponseCode(CommUtil.null2Int(Command[1]));
                rspHttp.setResponseMessage(Command[2]);
            } else {
                String name = line.substring(0, line.indexOf(":"));
                String value = line.substring(line.indexOf(":") + 1);

                name = name.trim();
                value = value.trim();

                // Field field = new Field();
                // field.setName(name);
                // field.setValue(value);

                headerFields.addHeaderFileds(name, value);

                if (name.equals("Cookie")) {
                    headerFields.setCookie(value);
                }
            }
        }
        // rspHttp.setContentType(null);
        // rspHttp.setContentEncoding(null);

        rspHttp.setHeaderFields(headerFields.buildHeaderFields());

        rspHttp.setContent(content);

        return rspHttp;
    }

}