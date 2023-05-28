package org.nachain.libs.httpclient.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ResponseHttp extends AbstractHttp {

    private String Protocol;

    private int ResponseCode;

    private String ResponseMessage;

    private String URLAddress;

    private String ContentType;

    private String ContentEncoding;

    public int getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(int responseCode) {
        ResponseCode = responseCode;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getProtocol() {
        return Protocol;
    }

    public void setProtocol(String protocol) {
        Protocol = protocol;
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

    public String getURLAddress() {
        return URLAddress;
    }

    public void setURLAddress(String uRLAddress) {
        URLAddress = uRLAddress;
    }

    public String toString() {
        return super.toString() + ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}