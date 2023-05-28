package org.nachain.libs.httpclient.bean;

import org.nachain.libs.beans.Field;
import org.nachain.libs.httpclient.HeaderHandle;

import java.util.ArrayList;

public class RequestHeaderFields {

    private String Accept;
    private String Referer;
    private String Accept_Language;
    private String User_Agent;
    private String UA_CPU;
    private String Accept_Encoding;
    private String Host;
    private String Pragma;
    private String Cookie;


    private ArrayList<Field> extendHeaderList = new ArrayList<Field>();


    public static final String Accept_Default = "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*";
    public static final String UserAgent_Default = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2824.2 Safari/537.36";
    public static final String UserAgent_Google = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";

    public void setHeaderFields(String name, String value) {
        HeaderHandle.setHeaderFileds(extendHeaderList, name, value);
    }


    public ArrayList<Field> buildRequestHeaderFields() {

        if (Accept == null)
            Accept = "*/*";
        if (Referer == null)
            Referer = null;
        if (Accept_Language == null)
            Accept_Language = "en-us";
        if (User_Agent == null)
            User_Agent = UserAgent_Default;
        if (UA_CPU == null)
            UA_CPU = "x86";
        if (Accept_Encoding == null)
            Accept_Encoding = "gzip, deflate";
        if (Host == null)
            Host = null;
        if (Pragma == null)
            Pragma = "no-cache";
        if (Cookie == null)
            Cookie = null;


        ArrayList<Field> headerFields = new ArrayList<Field>();
        headerFields.add(new Field("Accept", Accept));
        headerFields.add(new Field("Referer", Referer));
        headerFields.add(new Field("Accept-Language", Accept_Language));
        headerFields.add(new Field("User-Agent", User_Agent));
        headerFields.add(new Field("UA-CPU", UA_CPU));
        headerFields.add(new Field("Accept-Encoding", Accept_Encoding));
        headerFields.add(new Field("Host", Host));
        headerFields.add(new Field("Pragma", Pragma));
        headerFields.add(new Field("Cookie", Cookie));

        for (Field field : extendHeaderList) {
            HeaderHandle.setHeaderFileds(headerFields, field.getStringName(), field.getStringValue());
        }

        return headerFields;
    }
}