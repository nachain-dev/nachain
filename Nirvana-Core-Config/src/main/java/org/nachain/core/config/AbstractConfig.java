package org.nachain.core.config;

import org.nachain.core.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class AbstractConfig implements Config {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConfig.class);


    protected String declaredIp;

    static {

    }

    protected AbstractConfig(String rootDir, Network network, short networkVersion) {
        init();
        validate();
    }


    @Override
    public String getClientId() {
        return String.format("%s/v%s-%s/%s/%s",
                Constants.CLIENT_NAME,
                Constants.CLIENT_VERSION,
                Constants.CLIENT_VERSION_NAME,
                SystemUtils.getOsName().toString(),
                SystemUtils.getOsArch());
    }


    protected void init() {
        File f = new File(Constants.CONFIG_DIR, Constants.CONFIG_FILE);
        if (!f.exists()) {

            logger.error("Not found:" + f.getAbsolutePath());
            return;
        }

        try (FileInputStream in = new FileInputStream(f)) {
            Properties props = new Properties();
            props.load(in);
            for (Object k : props.keySet()) {
                String name = (String) k;
                switch (name) {
                    case "p2p.declaredIp":
                        declaredIp = props.getProperty(name).trim();
                        break;
                    default:
                        logger.error("Unsupported option: {} = {}", name, props.getProperty(name));
                        break;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load config file: {}", f, e);
        }
    }

    private void validate() {

    }

}
