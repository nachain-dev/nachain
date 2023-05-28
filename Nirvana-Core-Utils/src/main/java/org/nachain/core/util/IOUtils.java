package org.nachain.core.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {

    private IOUtils() {
    }


    public static byte[] readStream(InputStream in) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();

        BufferedInputStream bin = new BufferedInputStream(in);
        for (int c; (c = bin.read()) != -1; ) {
            buf.write(c);
        }

        return buf.toByteArray();
    }


    public static String readStreamAsString(InputStream in) throws IOException {
        return ByteUtils.toString(readStream(in));
    }


    public static byte[] readFile(File file) throws IOException {
        if (file.exists()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
                for (int c; (c = in.read()) != -1; ) {
                    out.write(c);
                }
            }

            return out.toByteArray();
        } else {
            return ByteUtils.EMPTY_BYTES;
        }
    }


    public static String readFileAsString(File file) throws IOException {
        return ByteUtils.toString(readFile(file));
    }


    public static void writeToFile(byte[] bytes, File file) throws IOException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            out.write(bytes);
        }
    }


    public static void writeToFile(String str, File file) throws IOException {
        writeToFile(ByteUtils.of(str), file);
    }


    public static List<String> readFileAsLines(File file, Charset charset) throws IOException {
        List<String> lines = new ArrayList<>();

        if (file.isFile()) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
                for (String line; (line = in.readLine()) != null; ) {
                    lines.add(line);
                }
            }
        }

        return lines;
    }


    public static void copyFile(File src, File dst, boolean replaceExisting) throws IOException {
        if (replaceExisting || !dst.exists()) {
            Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
