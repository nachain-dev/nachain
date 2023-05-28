package org.nachain.libs.zip;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


@Slf4j
public class GZIPUtil {
    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    public static byte[] compress(String str, String encoding) {
        byte[] data = null;
        try {
            data = compress(str.getBytes(encoding));
        } catch (Exception e) {
            log.error("compress error", e);
        }
        return data;
    }

    public static byte[] compress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        byte[] result = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
            gzip.close();

            result = out.toByteArray();

            out.close();
        } catch (Exception e) {
            log.error("compress error", e);
        }
        return result;
    }

    public static byte[] compress(String str) throws IOException {
        return compress(str, GZIP_ENCODE_UTF_8);
    }

    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        byte[] result = null;
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            ungzip.close();
            result = out.toByteArray();
            out.close();
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }

    public static String uncompressToString(byte[] bytes, String encoding) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            String result = out.toString(encoding);
            ungzip.close();
            out.close();
            return result;
        } catch (Exception e) {
            log.error("uncompressToString error" + new String(bytes, StandardCharsets.UTF_8), e);
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    public static String uncompressToString(byte[] bytes) {
        return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
    }

}