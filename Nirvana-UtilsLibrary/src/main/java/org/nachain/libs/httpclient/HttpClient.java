package org.nachain.libs.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.beans.Field;
import org.nachain.libs.exception.ExceptionHandle;
import org.nachain.libs.httpclient.bean.RequestHeaderFields;
import org.nachain.libs.httpclient.bean.RequestHttp;
import org.nachain.libs.httpclient.bean.ResponseHttp;
import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.IoUtil;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

@Slf4j
public class HttpClient {


    private static int readBufferSize = 1024;

    private HttpClient() {
    }


    public static class Method {
        public static String POST = "POST";
        public static String GET = "GET";
        public static String PUT = "PUT";
        public static String DELETE = "DELETE";
        public static String CONNECT = "CONNECT";
        public static String OPTIONS = "OPTIONS";
        public static String PATCH = "PATCH";
        public static String PROPFIND = "PROPFIND";
        public static String PROPPATCH = "PROPPATCH";
        public static String MKCOL = "MKCOL";
        public static String COPY = "COPY";
        public static String MOVE = "MOVE";
        public static String LOCK = "LOCK";
        public static String UNLOCK = "UNLOCK";

    }

    public static int getReadBufferSize() {
        return readBufferSize;
    }

    public static void setReadBufferSize(int readBufferSize) {
        HttpClient.readBufferSize = readBufferSize;
    }


    public static ResponseHttp openBasic(RequestHttp requestHttp) throws Exception {
        return openBasic(requestHttp, Long.MAX_VALUE);
    }


    public static byte[] requestSocketHttp(RequestHttp requestHttp, long maxLen) throws IOException {

        byte[] responseData;


        URL url = new URL(requestHttp.getURLAddress());


        String requestURL = CommUtil.contentSubstring(requestHttp.getURLAddress(), "//", "");
        requestURL = "/" + CommUtil.contentSubstring(requestURL, "/", "");


        Socket socket = new Socket(InetAddress.getByName(url.getHost()), url.getPort());


        StringBuffer reqHeader = new StringBuffer();
        {
            reqHeader.append(requestHttp.getMethod() + " " + requestURL + " " + requestHttp.getProtocol());
            reqHeader.append("\n");
            List<Field> reqHeaderList = requestHttp.getHeaderFields();
            for (Field f : reqHeaderList) {
                reqHeader.append(f.getStringName() + ": " + f.getStringValue());
                reqHeader.append("\n");
            }
            reqHeader.append("\r\n");
        }

        log.debug("Socket RequestHttp -> Request:\n" + reqHeader + new String(requestHttp.getContent()));


        OutputStream os = socket.getOutputStream();
        os.write(reqHeader.toString().getBytes());
        os.write(requestHttp.getContent());
        os.flush();


        socket.shutdownOutput();


        InputStream is = socket.getInputStream();


        responseData = IoUtil.inputStreamToBytes(is, readBufferSize, maxLen);
        if (responseData == null) {
            responseData = new byte[0];
        }

        log.debug("Socket RequestHttp -> Response:\n" + new String(responseData));


        socket.shutdownInput();


        socket.close();

        return responseData;
    }


    public static ResponseHttp openBasic(RequestHttp requestHttp, long maxLen) throws Exception {
        ResponseHttp responseHttp;

        String method = requestHttp.getMethod().toUpperCase();


        if (method.equals(Method.GET) || method.equals(Method.POST)) {
            responseHttp = openURLConn(requestHttp, maxLen);
        } else {

            try {

                byte[] responseData = requestSocketHttp(requestHttp, maxLen);


                responseHttp = HttpClientService.analyze2ResponseHttp(responseData);

            } catch (IOException e) {
                throw ExceptionHandle.getException(e, "HttpURLConnection error while opening URL:" + requestHttp.getURLAddress());
            }
        }

        return responseHttp;
    }


    @SuppressWarnings("unchecked")
    public static ResponseHttp openURLConn(RequestHttp requestHttp, long maxLen) throws Exception {
        ResponseHttp responseHttp = new ResponseHttp();

        String requestURL = requestHttp.getURLAddress();
        byte[] requestContent = requestHttp.getContent();


        if (requestHttp.getMethod() == null)
            requestHttp.setMethod("");
        requestHttp.setMethod(requestHttp.getMethod().toUpperCase());

        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            throw ExceptionHandle.getException(e, "Error creating URL object, URL:" + requestURL);
        }
        HttpURLConnection httpURLConn = null;
        try {
            httpURLConn = (HttpURLConnection) url.openConnection();

            httpURLConn.setInstanceFollowRedirects(false);


            if (requestHttp.getConnectTimeout() > 0) {
                httpURLConn.setConnectTimeout(requestHttp.getConnectTimeout());
            }

            if (requestHttp.getReadTimeout() > 0) {
                httpURLConn.setReadTimeout(requestHttp.getReadTimeout());
            }
        } catch (IOException e) {
            throw ExceptionHandle.getException(e, "Error getting HttpURLConnection from URL, URL opened:" + requestURL);
        }

        if (requestHttp.getHeaderFields() != null) {
            ListIterator<Field> listIter = requestHttp.getHeaderFields().listIterator();
            while (listIter.hasNext()) {
                Field file = listIter.next();
                if (file.getValue() != null)
                    httpURLConn.addRequestProperty(file.getStringName(), file.getStringValue());
            }
        }


        if (requestHttp.getMethod().toUpperCase().equals(Method.POST)) {
            log.debug("Submit by POST.");
            try {
                httpURLConn.setRequestMethod(requestHttp.getMethod());
                if (requestContent != null) {
                    httpURLConn.setDoOutput(true);
                }

                httpURLConn.connect();

                if (requestContent != null) {


                    DataOutputStream dos = new DataOutputStream(httpURLConn.getOutputStream());
                    dos.write(requestContent);
                    dos.flush();
                    dos.close();
                }

            } catch (ProtocolException e) {
                throw ExceptionHandle.getException(e, "HttpURLConnectionError while submitting data in POST mode,URL:" + requestURL);
            } catch (IOException e) {
                throw ExceptionHandle.getException(e, "HttpURLConnectionError while submitting data in POST mode,URL:" + requestURL);
            }

        } else {
            log.debug("Submit using GET.");

            try {
                httpURLConn.setRequestMethod(requestHttp.getMethod());
                httpURLConn.connect();
            } catch (IOException e) {
                throw ExceptionHandle.getException(e, "HttpURLConnection Link error while opening URL:" + requestURL);
            }
        }


        String URI = url.getFile();
        if (URI.equals("")) {
            URI = "/";
        }
        int Port = url.getPort();
        String Host = url.getHost();
        if (Port != 80) {
            Host += ":" + Port;
        }
        requestHttp.setURI(URI);
        requestHttp.setHost(Host);
        requestHttp.setHeader("Host", Host);


        int responseCode = httpURLConn.getResponseCode();
        String responseMessage = httpURLConn.getResponseMessage();
        String URLAddress = httpURLConn.getURL().toString();
        String contentType = httpURLConn.getContentType();
        String contentEncoding = httpURLConn.getContentEncoding();

        responseHttp.setURLAddress(URLAddress);
        responseHttp.setContentType(contentType);

        responseHttp.setContentEncoding(contentEncoding);


        responseHttp.setProtocol(requestHttp.getProtocol());
        responseHttp.setResponseCode(responseCode);
        responseHttp.setResponseMessage(responseMessage);


        List<Field> responseHeaderFields = new ArrayList<Field>();

        Map headerMap = httpURLConn.getHeaderFields();
        Iterator<Map.Entry> iter = headerMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            Object key = entry.getKey();
            String value = "";
            List<?> valueList = (List<?>) entry.getValue();
            for (int i = 0; i < valueList.size(); i++) {
                value += valueList.get(i);
                if (i + 1 < valueList.size())
                    value += "; ";
            }
            if (key != null) {
                Field field = new Field();
                field.setName(key.toString());
                field.setValue(value);

                responseHeaderFields.add(field);
            }
        }
        responseHttp.setHeaderFields(responseHeaderFields);


        InputStream urlStream;
        try {
            urlStream = httpURLConn.getInputStream();
        } catch (IOException e) {
            urlStream = httpURLConn.getErrorStream();
        }
        try {
            byte[] data = IoUtil.inputStreamToBytes(urlStream, readBufferSize, maxLen);
            if (data == null)
                data = new byte[0];

            responseHttp.setContent(data);
        } catch (FileNotFoundException e) {
            throw ExceptionHandle.getException(e, "Error reading body content, file not found:" + requestHttp.getURLAddress());
        } catch (IOException e) {
            throw ExceptionHandle.getException(e, "Error reading body content:" + requestHttp.getURLAddress());
        } finally {

            if (httpURLConn != null)
                httpURLConn.disconnect();
        }


        responseHttp.setHeader("Content-Length", String.valueOf(responseHttp.getContent() == null ? 0 : responseHttp.getContent().length));


        {
            StringBuffer debugInfo = new StringBuffer();
            debugInfo.append("--------------------------------- Request " + requestHttp.getURLAddress() + " ---------------------------------\r\n");
            debugInfo.append(requestHttp.getMethod() + " " + requestHttp.getURI() + " " + requestHttp.getProtocol() + "\r\n");

            if (requestHttp.getHeaderFields() != null) {
                ListIterator<Field> listIter = requestHttp.getHeaderFields().listIterator();
                while (listIter.hasNext()) {
                    Field file = listIter.next();
                    if (file.getValue() != null)
                        debugInfo.append(file.getName() + ": " + file.getValue() + "\r\n");
                }
            }
            debugInfo.append("\r\n");
            if (requestHttp.getContent() != null)
                debugInfo.append(new String(requestHttp.getContent()));
            debugInfo.append("\r\n");
            debugInfo.append("--------------------------------- Response ---------------------------------\r\n");
            debugInfo.append(responseHttp.getProtocol() + " " + responseHttp.getResponseCode() + " " + responseHttp.getResponseMessage() + "\r\n");

            if (responseHttp.getHeaderFields() != null) {
                ListIterator<Field> listIter = responseHttp.getHeaderFields().listIterator();
                while (listIter.hasNext()) {
                    Field file = listIter.next();
                    if (file.getValue() != null)
                        debugInfo.append(file.getName() + ": " + file.getValue() + "\r\n");
                }
            }
            debugInfo.append("\r\n");
            debugInfo.append(CommUtil.null2String(responseHttp.getContent()));
            debugInfo.append("\r\n");
            log.debug(debugInfo.toString());
        }


        return responseHttp;
    }


    public static ResponseHttp open(RequestHttp requestHttp) throws Exception {
        return open(requestHttp, Long.MAX_VALUE);
    }


    public static ResponseHttp open(RequestHttp requestHttp, long maxLen) throws Exception {

        ResponseHttp responseHttp = openBasic(requestHttp, maxLen);


        byte[] content = responseHttp.getContent();

        String ContentType = CommUtil.null2String(responseHttp.getContentType());
        String ContentEncoding = CommUtil.null2String(responseHttp.getContentEncoding());
        long ContentLength = (content != null ? content.length : 0);


        if (ContentType.startsWith("text")) {

            if (ContentEncoding.toLowerCase().equals("gzip")) {
                if (requestHttp.getContentEncoding() != null) {
                    responseHttp.setContentEncoding(requestHttp.getContentEncoding());
                }
                GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(content));
                content = IoUtil.gzipInputStreamToBytes(in);
                responseHttp.setContent(content);
            }


            if (CommUtil.isEmpty(ContentEncoding)) {
                if (!CommUtil.isEmpty(requestHttp.getContentEncoding())) {
                    ContentEncoding = requestHttp.getContentEncoding();
                }
            }


            if (CommUtil.isEmpty(ContentEncoding)) {
                throw new Exception("The program cannot automatically get the ContentEncoding from the page,Can try to manually RequestHttp. SetContentEncoding () to solve this problem.");
            }


            responseHttp.setContentEncoding(ContentEncoding);


            String strContent = "";


            if (content != null && content.length > 0 && !CommUtil.isEmpty(ContentEncoding))
                strContent = new String(content, ContentEncoding);


            strContent = strContent.replaceAll("\r", "").replaceAll("\n", "\r\n");


            if (CommUtil.isEmpty(ContentEncoding)) {
                content = strContent.getBytes();
            } else {
                content = strContent.getBytes(ContentEncoding);
            }
        } else if (ContentEncoding.toLowerCase().equals("gzip")) {
            GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(content));
            content = IoUtil.gzipInputStreamToBytes(in);

            ContentEncoding = requestHttp.getContentEncoding();
            responseHttp.setContentEncoding(ContentEncoding);
        } else {

            ContentEncoding = requestHttp.getContentEncoding();
            responseHttp.setContentEncoding(ContentEncoding);
        }


        responseHttp.setContent(content);

        return responseHttp;
    }


    public static ResponseHttp openURL(String urlAddress) throws Exception {
        return openURL(urlAddress, null, RequestHeaderFields.UserAgent_Default);
    }


    public static ResponseHttp openURL(String urlAddress, String encoding) throws Exception {
        return openURL(urlAddress, encoding, RequestHeaderFields.UserAgent_Default);
    }


    public static ResponseHttp openURL(String urlAddress, String contentEncoding, String userAgent) throws Exception {
        return openURL(urlAddress, contentEncoding, userAgent, 0, 0);
    }


    public static ResponseHttp openURL(String urlAddress, String contentEncoding, int ConnectTimeout, int ReadTimeout) throws Exception {
        return openURL(urlAddress, contentEncoding, RequestHeaderFields.UserAgent_Default, 0, 0);
    }


    public static ResponseHttp openURL(String urlAddress, String contentEncoding, String userAgent, int ConnectTimeout, int ReadTimeout) throws Exception {

        RequestHeaderFields headerFields = new RequestHeaderFields();
        //headerFields.setUserAgent(userAgent);

        RequestHttp requestHttp = new RequestHttp();
        requestHttp.setMethod("GET");
        requestHttp.setProtocol("HTTP/1.1");
        requestHttp.setURLAddress(urlAddress);
        requestHttp.setHeaderFields(headerFields.buildRequestHeaderFields());
        requestHttp.setContent(null);
        requestHttp.setContentEncoding(contentEncoding);
        requestHttp.setConnectTimeout(ConnectTimeout);
        requestHttp.setReadTimeout(ReadTimeout);

        ResponseHttp responseHttp = HttpClient.open(requestHttp);

        return responseHttp;
    }


    public static String requestHttp(String urlAddress) throws Exception {

        return requestHttp(urlAddress, null, RequestHeaderFields.UserAgent_Default);
    }


    public static String requestHttp(String urlAddress, String contentEncoding) throws Exception {

        return requestHttp(urlAddress, contentEncoding, RequestHeaderFields.UserAgent_Default);
    }


    public static String requestHttp(String urlAddress, String contentEncoding, String userAgent) throws Exception {
        String returnValue = "";


        ResponseHttp responseHttp = openURL(urlAddress, contentEncoding, userAgent);

        returnValue = HttpClientService.getHTML(responseHttp);

        return returnValue;
    }


    public static String requestHttp(RequestHttp requestHttp) throws Exception {
        return requestHttp(requestHttp, Long.MAX_VALUE);
    }


    public static String requestHttp(RequestHttp requestHttp, long maxLen) throws Exception {
        String returnValue = "";


        ResponseHttp responseHttp = open(requestHttp, maxLen);

        returnValue = HttpClientService.getHTML(responseHttp);

        return returnValue;
    }


    public static boolean downloadFile(String downURL, String saveURL, int connectTimeout, int readTimeout) {
        boolean rtv = true;

        RequestHttp requestHttp = HttpClientService.buildRequestHttp(downURL, "");
        requestHttp.setConnectTimeout(connectTimeout);
        requestHttp.setReadTimeout(readTimeout);

        try {
            ResponseHttp responseHttp = HttpClient.openBasic(requestHttp);


            if (responseHttp.getResponseCode() == 200) {

                byte[] dataArray = responseHttp.getContent();
                IoUtil.bytesToFile(dataArray, saveURL);
            } else {
                rtv = false;
            }
        } catch (Exception e) {
            rtv = false;
        }

        return rtv;
    }


    public static boolean downloadFile(String downURL, String saveURL) {
        boolean rtv = true;

        RequestHttp requestHttp = HttpClientService.buildRequestHttp(downURL, "");

        try {
            ResponseHttp responseHttp = HttpClient.openBasic(requestHttp);


            if (responseHttp.getResponseCode() == 200) {

                byte[] dataArray = responseHttp.getContent();
                IoUtil.bytesToFile(dataArray, saveURL);
            } else {
                rtv = false;
            }
        } catch (Exception e) {
            rtv = false;
        }

        return rtv;
    }


    public static byte[] downloadFile(String downURL) {
        byte[] data = null;

        RequestHttp requestHttp = HttpClientService.buildRequestHttp(downURL, "");

        try {
            ResponseHttp responseHttp = HttpClient.openBasic(requestHttp);


            data = responseHttp.getContent();
        } catch (Exception e) {
            log.error("Error downloading data:" + downURL, e);
        }

        return data;
    }


}