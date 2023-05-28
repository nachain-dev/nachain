package org.nachain.core.networks.p2p.iclient.global;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.config.ProfileService;
import org.nachain.core.networks.p2p.isc.NetClientNode;
import org.nachain.libs.config.ConfigFile;
import org.nachain.libs.config.ConfigSRC;
import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.FilePathUtil;

import java.io.File;


@Slf4j
public class IClientConfig extends ConfigSRC {


    public static String appPath;


    public static String appVersion = "1.0.0";


    public static NetClientNode defaultClientNode;


    public static int maxFrameLength;


    public static String actionPath;


    private static final IClientConfig icConfig;

    static {
        icConfig = new IClientConfig();
    }

    @Override
    public void initConfig(ConfigFile configFile) {

        try {
            IClientConfig.appPath = new FilePathUtil(IClientConfig.class).getProjectRootPath();
            log.debug("IClientConfig.AppPath:" + IClientConfig.appPath);
        } catch (Exception e) {
            log.error("Error obtaining root directory:", e);
        }


        defaultClientNode = new NetClientNode();

        defaultClientNode.setNodeName(configFile.getValue("NodeName"));

        defaultClientNode.setNodeType(configFile.getValue("NodeType"));

        defaultClientNode.setAuthPassword(configFile.getValue("AuthPassword"));

        maxFrameLength = CommUtil.null2Int(configFile.getValue("MaxFrameLength"), 104857600);

        actionPath = configFile.getValue("ActionPath", "org.nachain.core.networks.chain.client.action");

        log.debug("Client Startup:" + defaultClientNode.toString());
    }

    @Override
    public String getConfigFileName() {
        return String.format("%s%s%s", ProfileService.getConfigProfile(), File.separator, "iClient.config");
    }
}