package org.nachain.libs.util;


import jakarta.servlet.http.Cookie;

public class CookieUtil {


    public static String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie != null && cookieName.equals(cookie.getName()) && cookie.getValue() != null)
                    return cookie.getValue();
            }
        }
        return (defaultValue);
    }

}