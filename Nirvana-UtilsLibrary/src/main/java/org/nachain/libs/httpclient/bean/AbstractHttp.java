package org.nachain.libs.httpclient.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.nachain.libs.beans.Field;
import org.nachain.libs.httpclient.HeaderHandle;

import java.util.List;

public class AbstractHttp {

    private List<Field> headerFields;

    private byte[] Content;


    public void setHeader(String name, String value) {
        HeaderHandle.setHeaderFileds(headerFields, name, value);
    }


    public String getHeader(String name) {
        String returnValue = "";

        Field field = HeaderHandle.getField(headerFields, name);
        if (field != null) {
            returnValue = field.getStringValue();
        }

        return returnValue;
    }


    public void delHeader(String name) {
        HeaderHandle.delHeader(headerFields, name);
    }

    public List<Field> getHeaderFields() {
        return headerFields;
    }

    public void setHeaderFields(List<Field> headerFields) {
        this.headerFields = headerFields;
    }

    public byte[] getContent() {
        return Content;
    }

    public void setContent(byte[] content) {
        Content = content;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}