package org.nachain.core.crypto;

import org.nachain.core.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;


public class Native {

    private static final Logger logger = LoggerFactory.getLogger(Native.class);

    protected static File nativeDir;
    protected static boolean enabled = false;


    static {
        init();
    }


    protected static void init() {
        if (SystemUtils.is32bitJvm()) {

            return;
        }

        SystemUtils.OsName os = SystemUtils.getOsName();
        switch (os) {
            case LINUX:
                if (SystemUtils.getOsArch().equals("aarch64")) {
                    enabled = loadLibrary("/native/Linux-aarch64/nirvana-crypto.so");
                } else {
                    enabled = loadLibrary("/native/Linux-x86_64/nirvana-crypto.so");
                }
                break;
            case MACOS:
                enabled = loadLibrary("/native/Darwin-x86_64/nirvana-crypto.dylib");
                break;
            case WINDOWS:
                enabled = loadLibrary("/native/Windows-x86_64/nirvana-crypto.dll");
                break;
            default:
                break;
        }
    }


    protected static boolean loadLibrary(String resource) {
        try {
            if (nativeDir == null) {
                nativeDir = Files.createTempDirectory("native").toFile();
                nativeDir.deleteOnExit();
            }

            String name = resource.contains("/") ? resource.substring(resource.lastIndexOf('/') + 1) : resource;
            File file = new File(nativeDir, name);

            if (!file.exists()) {
                InputStream in = Native.class.getResourceAsStream(resource);
                if (in != null) {
                    OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                    for (int c; (c = in.read()) != -1; ) {
                        out.write(c);
                    }
                    out.close();
                    in.close();
                }
            }
            if (file.exists()) {
                System.load(file.getAbsolutePath());
                return true;
            } else {
                return false;
            }

        } catch (Exception | UnsatisfiedLinkError e) {
            logger.warn("Failed to load native library: {}", resource, e);
            return false;
        }
    }


    public static boolean isEnabled() {
        return enabled;
    }


    public static void disable() {
        enabled = false;
    }


    public static void enable() {
        init();
    }


    public static native byte[] h256(byte[] data);


    public static native byte[] h160(byte[] data);


    public static native byte[] sign(byte[] message, byte[] privateKey);


    public static native boolean verify(byte[] message, byte[] signature, byte[] publicKey);


    public static native boolean verifyBatch(byte[][] messages, byte[][] signatures, byte[][] publicKeys);
}
