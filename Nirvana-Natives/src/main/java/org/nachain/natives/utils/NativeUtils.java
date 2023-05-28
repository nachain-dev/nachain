package org.nachain.natives.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class NativeUtils {

    public static void loadLibrary(String resourcePath) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        loadLibrary(resourcePath, classLoader);
    }

    public static void loadLibrary(String resourcePath, ClassLoader classLoader) throws IOException {
        File tmpDir = new File(System.getProperty("user.home"));
        if (!tmpDir.exists() || !tmpDir.isDirectory()) {
            throw new IOException("user dir unavailable");
        }
        File nacDir = new File(new File(tmpDir, ".nac"), "nativeutils");

        if (!nacDir.exists() || !nacDir.isDirectory()) {
            if (!nacDir.mkdirs()) {
                throw new IOException("failed to create temp folder");
            }
        }

        String fileName = deduceFileName(resourcePath);
        File tmpFile = new File(nacDir, fileName);


        File lockFile = new File(nacDir, "native.lock");
        lockFile.deleteOnExit();
        FileLock lock = null;
        try (FileChannel c = new FileOutputStream(lockFile, true).getChannel()) {
            lock = c.lock();

            try (InputStream input = classLoader.getResourceAsStream(resourcePath);) {
                if (input == null) {
                    throw new IOException("Resource not found:" + resourcePath + " for classloader " + classLoader.toString());
                }
                Files.copy(input, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (AccessDeniedException e) {

            }

            System.load(tmpFile.getAbsolutePath());
        } finally {
            if (lock != null) {
                try {
                    lock.release();
                    lockFile.delete();
                } catch (Exception e) {
                }
            }
        }
    }


    private static String deduceFileName(String path) {
        String[] parts = path.split("/");
        if (parts.length > 0) {
            return parts[parts.length - 1];
        }
        throw new IllegalArgumentException("invalid path " + path);
    }

    private NativeUtils() {
    }
}
