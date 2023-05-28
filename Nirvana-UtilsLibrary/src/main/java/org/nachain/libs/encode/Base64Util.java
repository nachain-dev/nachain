package org.nachain.libs.encode;

import org.spongycastle.util.encoders.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class Base64Util {


    public static String base64Encode(String s) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Base64OutputStream out = new Base64OutputStream(bout);
        try {
            out.write(s.getBytes());
            out.flush();
        } catch (IOException e) {
        }

        return bout.toString();
    }


    public static String httpAuthorizationBasic(String username, String password) {
        return base64Encode(username + ":" + password);
    }

    public static byte[] encode(byte[] data) {
        byte[] encodeBytes = Base64.encode(data);

        return encodeBytes;
    }

    public static String encode(String data) {
        byte[] encodeBytes = encode(data.getBytes());

        return new String(encodeBytes);
    }

    public static byte[] decode(byte[] data) {
        byte[] decodeBytes = Base64.decode(data);

        return decodeBytes;
    }

    public static String decode(String data) {
        byte[] decodeBytes = decode(data.getBytes());

        return new String(decodeBytes);
    }

}