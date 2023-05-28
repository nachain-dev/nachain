package org.nachain.libs.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class NetUtil {

    /**
     * The default user agent for HTTP requests.
     */
    private static final String DEFAULT_USER_AGENT = "Mozilla/4.0";

    /**
     * The default connect timeout.
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 4000;

    /**
     * The default read timeout.
     */
    private static final int DEFAULT_READ_TIMEOUT = 4000;

    /**
     * .UTF-8
     *
     * @param content
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String urlEncoder(String content, String charset) throws UnsupportedEncodingException {
        String returnValue = null;

        if (charset == null || charset.equals(""))
            charset = "UTF-8";
        if (content != null && !content.equals("%")) {
            returnValue = URLEncoder.encode(content, charset);
        }

        return returnValue;
    }

    /**
     * ,.UTF-8
     *
     * @param content
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String urlDecoder(String content, String charset) throws UnsupportedEncodingException {
        String returnValue = null;

        if (charset == null || charset.equals(""))
            charset = "UTF-8";
        if (content != null && !content.equals("%")) {
            returnValue = URLDecoder.decode(new String(content.getBytes("ISO-8859-1"), charset), charset);
        }

        return returnValue;
    }

    /**
     * ,
     *
     * @param requestURL
     * @param relativeURL
     * @return
     */
    public static String fullURL(String requestURL, String relativeURL) {
        String returnValue = null;

        // 
        if (relativeURL == null || relativeURL.equals("")) {
            return returnValue;
        }

        // url
        if (relativeURL.toLowerCase().startsWith("http://") || relativeURL.toLowerCase().startsWith("https://") || relativeURL.toLowerCase().startsWith("tencent://") || relativeURL.toLowerCase().startsWith("ftp://") || relativeURL.toLowerCase().startsWith("mailto://")) {
            return relativeURL;
        }

        // 
        if (!requestURL.toLowerCase().startsWith("http://") && !requestURL.toLowerCase().startsWith("https://")) {
            requestURL = "http://" + requestURL;
        }

        // 
        String protocol = requestURL.substring(0, requestURL.indexOf("://"));
        // URL
        String domain = requestURL.substring(protocol.length() + 3);
        if (domain.indexOf("/") != -1) {
            domain = domain.substring(0, domain.indexOf("/"));
        }

        String rootURL = protocol + "://" + domain + "/";

        // 
        if (relativeURL.startsWith("/")) {
            returnValue = rootURL + relativeURL.substring(1);
        } else {
            // ../,
            if (relativeURL.startsWith("../")) {
                String url = requestURL.substring(protocol.length() + 3);
                String[] pathURL = url.split("/");
                // ../
                int pTotal = CommUtil.countStringTotal(relativeURL, "../");
                // 
                String newPath = "";
                int pathURLLen = pathURL.length - 1;
                for (int i = 0; i < pathURLLen - pTotal; i++) {
                    newPath = newPath + "/" + pathURL[i];
                }
                // ,domain
                if (newPath.equals("")) {
                    newPath = domain;
                }
                // /
                if (newPath.startsWith("/")) {
                    newPath = newPath.substring(1);
                }
                // ../
                String newRelative = relativeURL.replaceAll("../", "");
                // 
                returnValue = protocol + "://" + newPath + "/" + newRelative;
            }
            // 
            else {
                returnValue = requestURL.substring(0, requestURL.lastIndexOf("/") + 1) + relativeURL;
            }
        }

        return returnValue;
    }

    /**
     * @param urlAddress
     * @return
     * @throws MalformedURLException
     */
    public static String getDomain(String urlAddress) throws MalformedURLException {

        // URL,Host
        String host = "";
        if (urlAddress.toLowerCase().startsWith("http://") || urlAddress.toLowerCase().startsWith("https://")) {
            host = new URL(urlAddress).getHost();
        } else {
            host = urlAddress;
            // ,http://
            if (urlAddress.contains("/") || urlAddress.contains(".")) {
                host = "http://" + urlAddress;
                host = new URL(host).getHost();
            }
        }

        String domain = host.toLowerCase();

        return domain;
    }

    /**
     * @param url
     * @return :http://www.domain.com/ :domain.com
     * @throws MalformedURLException
     */
    public static String topLevelDomain(String url) throws MalformedURLException {
        String returnValue = "";

        String host = "";
        // URL,Host
        if (url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://")) {
            host = new URL(url).getHost();
        } else {
            host = url;
            // ,http://
            if (url.contains("/") || url.contains(".")) {
                host = "http://" + url;
                host = new URL(host).getHost();
            }
        }

        String _host = host.toLowerCase();
        String[] suffixs = {".com", ".net", ".org", ".gov", ".com.hk", ".com", ".net", ".org", ".biz", ".info", "", ".mobi", ".name", ".sh", ".ac", ".la", ".travel", ".tm", ".us", ".cc", ".tv", ".jobs", ".asia", ".hn", ".lc", ".hk", ".bz", ".ws", ".tel", ".cm"};

        // 
        for (int i = 0; i < suffixs.length; i++) {
            String suffix = suffixs[i];
            if (_host.endsWith(suffix)) {
                String domainName = _host.substring(0, _host.lastIndexOf(suffix));
                domainName = domainName.substring(domainName.lastIndexOf(".") + 1, domainName.length());
                domainName += suffix;
                returnValue = domainName;
                break;
            }
        }
        return returnValue;
    }

    /**
     * Mac
     *
     * @param mac
     * @return
     */
    public static String mac2String(byte[] mac) {
        String returnValue = "";
        int count = 0;
        for (byte b : mac) {
            count++;
            String unit = Integer.toHexString(b & 0xff);
            if (unit.length() == 1) {
                unit = "0" + unit;
            }
            returnValue += unit;
            if (count < mac.length) {
                returnValue += ":";
            }
        }
        return returnValue;
    }

    /**
     * IP
     *
     * @return
     */
    public static String getLocalIP() {
        String rtv = "";

        InetAddress inet;
        try {
            inet = InetAddress.getLocalHost();
            rtv = inet.getHostAddress();
        } catch (UnknownHostException e) {
            rtv = "";
        }

        return rtv;
    }

    /**
     * @param domain
     * @return
     * @throws UnknownHostException
     */
    public static String getServerIP(String domain) throws UnknownHostException {
        String rtv = "";

        InetAddress inet;
        try {
            inet = InetAddress.getByName(domain);
            rtv = inet.getHostAddress();
        } catch (UnknownHostException e) {
            rtv = "";
        }

        return rtv;
    }

    /**
     * IP
     *
     * @return
     */
    public static String getMyOuterNetIP() {
        String rtv = "";

        try {
            URL url = new URL("http://checkip.amazonaws.com/");
            URLConnection con = url.openConnection();
            con.addRequestProperty("User-Agent", DEFAULT_USER_AGENT);
            con.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
            con.setReadTimeout(DEFAULT_READ_TIMEOUT);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), UTF_8));
            String ip = reader.readLine().trim();
            reader.close();

            // only IPv4 is supported currently
            if (ip.matches("(\\d{1,3}\\.){3}\\d{1,3}")) {
                return ip;
            }
        } catch (IOException e1) {
            log.warn("Failed to retrieve your public IP address");
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e2) {
                log.warn("Failed to retrieve your localhost IP address");
            }
        }

        return InetAddress.getLoopbackAddress().getHostAddress();
    }

    /**
     * MapUrl
     *
     * @param paramMap
     * @return
     */
    public static String toQueryUrl(Map<String, Object> paramMap) throws UnsupportedEncodingException {
        if (paramMap == null || paramMap.size() == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String key : paramMap.keySet()) {
            Object value = paramMap.get(key);
            stringBuilder.append("&");
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(String.valueOf(value), "utf-8"));
        }

        return stringBuilder.toString();
    }

    /**
     * URL
     *
     * @param url
     * @return
     */
    public static String addParameter(String url, String name, String value) {
        String rtv = url;

        // ,
        if (rtv.contains("?")) {
            rtv += "&" + name + "=" + value;
        }
        // ,
        else {
            rtv += "?" + name + "=" + value;
        }

        return rtv;
    }

    /**
     * URL
     */
    public static Map<String, Object> getQueryParameterMap(String strURL) throws MalformedURLException {
        Map<String, Object> returnValue = new HashMap<String, Object>();

        // 
        if (StringUtils.equals(strURL, "")) {
            return returnValue;
        }

        // URL
        URL url = new URL(strURL);

        // 
        String query = url.getQuery();

        // 
        if (StringUtils.equals(query, "")) {
            return returnValue;
        }

        // 
        String[] querys = query.split("&");

        // 
        if (querys.length <= 0) {
            return returnValue;
        }

        // 
        for (String params : querys) {

            // ,"="
            String[] split = params.split("=");

            // map
            returnValue.put(CommUtil.null2String(split[0]), split[1]);
        }

        return returnValue;
    }

    /**
     * URL
     */
    public static boolean checkParamInQueryURL(String checkParamName, String strURL) throws MalformedURLException {
        boolean returnValue = false;

        // URL
        Map<String, Object> queryParameterMap = getQueryParameterMap(strURL);

        // 
        if (queryParameterMap == null || queryParameterMap.isEmpty()) {
            return returnValue;
        }

        // 
        returnValue = queryParameterMap.containsKey(checkParamName);

        return returnValue;
    }

}