package org.nachain.core.networks.p2p.iserver.global;

import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.config.ProfileService;
import org.nachain.core.networks.p2p.isc.NetServerNode;
import org.nachain.libs.config.ConfigFile;
import org.nachain.libs.config.ConfigSRC;
import org.nachain.libs.util.CommUtil;
import org.nachain.libs.util.FilePathUtil;

import java.io.File;


@Slf4j
public class IServerConfig extends ConfigSRC {


    public static final String LogName = "iServer";


    public static String AppPath;


    public static String AppVersion = "1.0.0";


    public static NetServerNode defaultServerNode;


    public static String webSocketReadyGo;


    public static String webSocketClassPath;


    public static String connectTriggerClassPath;


    public static String eventTriggerClassPath;


    public static boolean offlineDisconnection = true;


    public static int maxFrameLength;


    public static int coreThreadNumber;


    private static final IServerConfig iServerConfig;


    public static LogLevel nettyLog = LogLevel.ERROR;

    static {
        iServerConfig = new IServerConfig();
    }


    @Override
    public void initConfig(ConfigFile configFile) {


        try {
            IServerConfig.AppPath = new FilePathUtil(IServerConfig.class).getProjectRootPath();
            log.debug("IServerConfig.AppPath:" + IServerConfig.AppPath);
        } catch (Exception e) {
            log.error("Error obtaining root directory:", e);
        }


        defaultServerNode = new NetServerNode();

        defaultServerNode.setNodeName(configFile.getValue("NodeName"));

        defaultServerNode.setNodeType(configFile.getValue("NodeType"));

        defaultServerNode.setNodeId(configFile.getDoubleValue("NodeId"));

        defaultServerNode.setIP(configFile.getValue("IP"));

        defaultServerNode.setPort(configFile.getIntValue("Port"));

        defaultServerNode.setAuthPassword(configFile.getValue("AuthPassword"));


        webSocketReadyGo = configFile.getValue("WebSocket.ReadyGo");

        webSocketClassPath = configFile.getValue("WebSocket.ActionClassPath");

        connectTriggerClassPath = configFile.getValue("WebSocket.ConnectedClass");

        eventTriggerClassPath = configFile.getValue("WebSocket.EventTriggerClassPath");


        maxFrameLength = CommUtil.null2Int(configFile.getValue("MaxFrameLength"), 1048576);

        coreThreadNumber = CommUtil.null2Int(configFile.getValue("CoreThreadNumber"), 100);
    }

    @Override
    public String getConfigFileName() {
        return String.format("%s%s%s", ProfileService.getConfigProfile(), File.separator, "iServer.config");
    }
}