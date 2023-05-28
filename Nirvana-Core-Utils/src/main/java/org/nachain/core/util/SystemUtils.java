package org.nachain.core.util;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import org.nachain.core.util.exception.UnreachableException;
import org.nachain.version.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.util.Locale;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SystemUtils {

    private static final Logger logger = LoggerFactory.getLogger(SystemUtils.class);
    private static String version = null;

    private static int isAndroid = -1;


    private static final String DEFAULT_USER_AGENT = "Mozilla/4.0";


    private static final int DEFAULT_CONNECT_TIMEOUT = 4000;


    private static final int DEFAULT_READ_TIMEOUT = 4000;


    static {
        System.setProperty("jna.nosys", "true");
    }

    private SystemUtils() {
    }

    public static boolean isAndroidRuntime() {
        if (isAndroid == -1) {
            final String runtime = System.getProperty("java.runtime.name");
            isAndroid = (runtime != null && runtime.equals("Android Runtime")) ? 1 : 0;
        }
        return isAndroid == 1;
    }


    public static OsName getOsName() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (os.contains("win")) {
            return OsName.WINDOWS;
        } else if (os.contains("linux")) {
            return OsName.LINUX;
        } else if (os.contains("mac")) {
            return OsName.MACOS;
        } else {
            return OsName.UNKNOWN;
        }
    }


    public static String getOsArch() {
        return System.getProperty("os.arch");
    }


    public static boolean is32bitJvm() {
        String model = System.getProperty("sun.arch.data.model");
        return model != null && model.contains("32");
    }


    public static boolean is64bitJvm() {
        String model = System.getProperty("sun.arch.data.model");
        return model != null && model.contains("64");
    }


    public static String getIp() {
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


            if (ip.matches("(\\d{1,3}\\.){3}\\d{1,3}")) {
                return ip;
            }
        } catch (IOException e1) {
            logger.warn("Failed to retrieve your public IP address");

            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e2) {
                logger.warn("Failed to retrieve your localhost IP address");
            }
        }

        return InetAddress.getLoopbackAddress().getHostAddress();
    }


    public static int compareVersion(String v1, String v2) {
        return Version.valueOf(v1).compareTo(Version.valueOf(v2));
    }


    public static boolean bench() {

        if (is32bitJvm()) {
            logger.info("You're running 32-bit JVM. Please consider upgrading to 64-bit JVM");
            return false;
        }


        if (getNumberOfProcessors() < 2) {
            logger.info("# of CPU cores = {}", getNumberOfProcessors());
            return false;
        }


        if (getTotalMemorySize() < 3L * 1024L * 1024L * 1024L) {
            logger.info("Total physical memory size = {} MB", getTotalMemorySize() / 1024 / 1024);
            return false;
        }

        return true;
    }


    public static void exit(int code) {
        System.exit(code);
    }


    public static void exitAsync(int code) {
        new Thread(() -> System.exit(code)).start();
    }


    public static int getNumberOfProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }


    public static long getAvailableMemorySize() {
        SystemInfo systemInfo = new SystemInfo();
        return systemInfo.getHardware().getMemory().getAvailable();
    }


    public static long getTotalMemorySize() {
        SystemInfo systemInfo = new SystemInfo();
        return systemInfo.getHardware().getMemory().getTotal();
    }


    public static long getUsedHeapSize() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }


    public static void setLocale(Locale locale) {
        try {
            if (!Locale.getDefault().equals(locale)) {
                Locale.setDefault(locale);
            }
        } catch (SecurityException e) {
            logger.error("Unable to change localization.", e);
        }
    }


    public static Object getImplementationVersion(ClassLoader classLoader) {
        if (version == null) {
            try {
                version = IOUtils.readStreamAsString(classLoader.getResourceAsStream("VERSION")).trim();
            } catch (IOException ex) {
                logger.info("Failed to read version.");
                version = "unknown";
            }
        }

        return version;
    }


    public static boolean isWindowsVCRedist2012Installed() {
        if (SystemUtils.getOsName() != OsName.WINDOWS) {
            throw new UnreachableException();
        }

        try {
            if (Platform.is64Bit()) {
                return Advapi32Util.registryGetIntValue(
                        Advapi32Util.registryGetKey(
                                WinReg.HKEY_LOCAL_MACHINE,
                                "SOFTWARE\\Microsoft\\VisualStudio\\11.0\\VC\\Runtimes\\x64",
                                WinNT.KEY_READ | WinNT.KEY_WOW64_32KEY).getValue(),
                        "Installed") == 1;
            } else {
                return Advapi32Util.registryGetIntValue(
                        WinReg.HKEY_LOCAL_MACHINE,
                        "SOFTWARE\\Microsoft\\VisualStudio\\11.0\\VC\\Runtimes\\x86",
                        "Installed") == 1;
            }
        } catch (Win32Exception e) {
            logger.error("Failed to read windows registry", e);
            return false;
        }
    }


    public static boolean isJavaPlatformModuleSystemAvailable() {
        try {
            Class.forName("java.lang.Module");
            return true;
        } catch (ClassNotFoundException expected) {
            return false;
        }
    }


    public static boolean isPosix() {
        return FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
    }


    public static boolean isJUnitTest() {
        try {
            Class.forName("org.nirvana.TestUtils");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public enum OsName {
        WINDOWS("Windows"),
        LINUX("Linux"),
        MACOS("macOS"),
        UNKNOWN("Unknown");

        private final String name;

        OsName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class Code {

        public static final int OK = 0;


        public static final int FAILED_TO_WRITE_WALLET_FILE = 11;
        public static final int FAILED_TO_UNLOCK_WALLET = 12;
        public static final int ACCOUNT_NOT_EXIST = 13;
        public static final int ACCOUNT_ALREADY_EXISTS = 14;
        public static final int INVALID_PRIVATE_KEY = 15;
        public static final int WALLET_LOCKED = 16;
        public static final int PASSWORD_REPEAT_NOT_MATCH = 17;
        public static final int WALLET_ALREADY_EXISTS = 18;
        public static final int WALLET_ALREADY_UNLOCKED = 19;
        public static final int FAILED_TO_INIT_HD_WALLET = 20;


        public static final int FAILED_TO_INIT_ED25519 = 31;
        public static final int FAILED_TO_LOAD_CONFIG = 32;
        public static final int FAILED_TO_LOAD_GENESIS = 33;
        public static final int FAILED_TO_LAUNCH_KERNEL = 34;
        public static final int INVALID_NETWORK_LABEL = 35;
        public static final int FAILED_TO_SETUP_TRACER = 36;


        public static final int FAILED_TO_OPEN_DB = 51;
        public static final int FAILED_TO_REPAIR_DB = 52;
        public static final int FAILED_TO_WRITE_BATCH_TO_DB = 53;


        public static final int HARDWARE_UPGRADE_NEEDED = 71;
        public static final int CLIENT_UPGRADE_NEEDED = 72;
        public static final int JVM_32_NOT_SUPPORTED = 73;
    }
}
