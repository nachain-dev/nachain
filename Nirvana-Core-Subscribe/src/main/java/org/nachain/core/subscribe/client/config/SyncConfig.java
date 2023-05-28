package org.nachain.core.subscribe.client.config;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.config.ProfileService;
import org.nachain.libs.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class SyncConfig {


    public static String SyncServer_Ip;

    public static int SyncServer_Port;

    public static String SyncAction_Path;

    public static long[] Subscribe_Instances;

    static {

        Properties config = getProperties();
        SyncServer_Ip = (String) config.get("Subscribe.SyncServer.Ip");
        SyncServer_Port = Integer.valueOf(config.get("Subscribe.SyncServer.Port").toString());
        SyncAction_Path = (String) config.get("Subscribe.SyncAction.Path");
        Subscribe_Instances = StringUtil.toLongs(config.get("Subscribe.Instances").toString().split(","));
    }


    public static Properties getProperties() {


        String configFile = "config" + ProfileService.getProfile() + File.separator + "subscribe.properties";


        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        InputStream inStream = loader.getResourceAsStream(configFile);
        try {
            if (inStream != null) {

                properties.load(inStream);
                inStream.close();
            } else {
                throw new RuntimeException(String.format("Config file not found. FilePath=%s", configFile));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }
}
