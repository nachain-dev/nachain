package org.nachain.libs.config;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.encrypt.CryptoFile;
import org.nachain.libs.util.FilePathUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@Slf4j
public abstract class ConfigSRC {


    private String configFileName;


    private String configFileRealPath;


    private String projectRootPath;


    private String secretKey = "c77ew2bs";


    private ConfigFile configFile = new ConfigFile();

    protected ConfigFile getConfigFile() {
        return configFile;
    }

    protected ConfigSRC() {
        init(getConfigFileName());
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


    protected void init(String configFileName) {
        this.configFileName = configFileName;


        Properties properties = null;
        try {
            properties = getProperties(configFileName + CryptoFile.srcSuffix);
        } catch (Exception e) {
        }


        if (properties != null) {

            configFile.setConfig(properties);

            configFile.setEncode(true);

            configFile.setFileName(configFileName + CryptoFile.srcSuffix);

            initConfig(configFile);
        } else {

            try {

                projectRootPath = new FilePathUtil(ConfigSRC.class).getProjectRootPath();


                configFileRealPath = CryptoFile.getResourcesConfigFile(configFileName + CryptoFile.srcSuffix);
                if (configFileRealPath == null || !new File(configFileRealPath).exists()) {
                    configFileRealPath = projectRootPath + configFileName;
                } else {
                    configFileRealPath = configFileRealPath.substring(0, configFileRealPath.length() - CryptoFile.srcSuffix.length());
                }

                log.debug("Config file:" + configFileRealPath);


                try {
                    byte[] decodeData = CryptoFile.getConfigFileData(configFileRealPath, secretKey.getBytes());

                    configFile.setEncode(true);

                    configFile.setData(decodeData);

                    configFile.setFileName(configFileRealPath);
                } catch (Exception e) {
                    log.error(String.format("Error reading configuration file:\"%s\" or \"%s\"", configFileName, configFileRealPath), e);
                }


                initConfig(configFile);
            } catch (Exception e) {
                log.error("Initialization error:", e);
            }
        }
    }


    public abstract void initConfig(ConfigFile configFile);


    public abstract String getConfigFileName();

}