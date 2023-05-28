package org.nachain.core.config;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class ConfigService {


    public static Properties getProperties() {


        String devConfigFile = Constants.CONFIG_DIR + File.separator + Constants.CONFIG_DEV_FILE;

        String configFile = Constants.CONFIG_DIR + File.separator + Constants.CONFIG_FILE;


        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        InputStream inStream = loader.getResourceAsStream(devConfigFile);
        try {

            if (inStream == null) {
                inStream = loader.getResourceAsStream(configFile);
            }
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


    public static Properties getProperties(String configFile) {

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
