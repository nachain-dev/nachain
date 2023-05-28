package org.nachain.libs.httpclient.bean;

import org.nachain.libs.beans.Field;
import org.nachain.libs.httpclient.HeaderHandle;

import java.util.ArrayList;

public class ResponseHeaderFields {

    private String Cookie;


    private ArrayList<Field> extendHeaderList = new ArrayList<Field>();

    public String getCookie() {
        return Cookie;
    }

    public void setCookie(String cookie) {
        Cookie = cookie;
    }


    public void setHeaderFileds(String name, String value) {
        HeaderHandle.setHeaderFileds(extendHeaderList, name, value);
    }

    public void addHeaderFileds(String name, String value) {
        HeaderHandle.addHeaderFileds(extendHeaderList, name, value);
    }


    public ArrayList<Field> buildHeaderFields() {

        if (Cookie == null)
            Cookie = null;


        ArrayList<Field> headerFields = new ArrayList<Field>();
        if (Cookie != null)
            headerFields.add(new Field("Cookie", Cookie));


        for (Field field : extendHeaderList) {


            HeaderHandle.addHeaderFileds(headerFields, field.getStringName(), field.getStringValue());
        }

        return headerFields;
    }

}