package org.nachain.libs.remote;

import org.nachain.libs.httpclient.HttpClient;
import org.nachain.libs.httpclient.bean.RequestHttp;
import org.nachain.libs.httpclient.bean.ResponseHttp;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class HttpClientRemote {

    public static Object getObject(String requestURL) throws Exception {
        Object returnValue = null;


        RequestHttp requestHttp = new RequestHttp();
        requestHttp.setURLAddress(requestURL);


        ResponseHttp responseHttp = HttpClient.open(requestHttp);


        ByteArrayInputStream bais = new ByteArrayInputStream(responseHttp.getContent());


        ObjectInputStream ois = new ObjectInputStream(bais);
        returnValue = ois.readObject();
        ois.close();
        bais.close();

        return returnValue;
    }

}