package org.nachain.libs.httpclient.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RequestHttp extends AbstractHttp {

    private String Method;

    private String URI;

    private String Protocol;

    private String ContentType;

    private String ContentEncoding;

    private String Host;

    private String URLAddress;

    private int ConnectTimeout;

    private int ReadTimeout;

    public int getConnectTimeout() {
        return ConnectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        ConnectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return ReadTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        ReadTimeout = readTimeout;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getContentEncoding() {
        return ContentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        ContentEncoding = contentEncoding;
    }

    public String getProtocol() {
        return Protocol;
    }

    public void setProtocol(String protocol) {
        Protocol = protocol;
    }

    public String getURLAddress() {
        return URLAddress;
    }

    public void setURLAddress(String address) {
        URLAddress = address;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String uri) {
        URI = uri;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String toString() {
        return super.toString() + ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}