package org.nachain.core.networks.p2p.iserver;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.SenderService;
import org.nachain.core.networks.p2p.isc.action.ActionService;
import org.nachain.core.networks.p2p.isc.global.ISCConfig;
import org.nachain.core.networks.p2p.iserver.global.IServerConfig;


@Slf4j
public class ServerActionService {


    public static void connection(ChannelHandlerContext ctx, NetResources netResources) {

        String channelID = ctx.channel().id().toString();

        log.debug("The new client connects to the server, channelID=" + channelID);


        if (!netResources.isOnline(channelID)) {


            String senderName = ServerService.getNodeName(netResources);


            SenderService.send(ctx, new NetData(ISCConfig.CmdValue.QUERY_PASSWORD, "What is your password?", "").setSenderName(senderName));
            log.debug("What is your password?");
        }
    }


    public static boolean basic(ChannelHandlerContext ctx, NetResources netResources, NetData netData) {


        boolean continueFlag = true;


        String channelID = ctx.channel().id().toString();

        log.debug("Execute CmdValue:" + netData.getCmdValue() + ", Info:" + netData.getCmdInfo());


        ActionService.register(ctx, netResources, netData);


        switch (netData.getCmdValue()) {

            case ISCConfig.CmdValue.LOGIN: {
                String Password = netData.toDataString();


                String AuthPassword = IServerConfig.defaultServerNode.getAuthPassword();


                String nodeName = IServerConfig.defaultServerNode.getNodeName();


                if (!Password.equals(AuthPassword)) {

                    SenderService.send(ctx, new NetData(ISCConfig.CmdValue.REPLY_PASSWORD_ERROR, "Password error.", "").setFlag(false).setSenderName(nodeName));
                    log.debug("Password error:" + Password);
                } else {

                    netResources.addOnline(channelID);


                    SenderService.send(ctx, new NetData(ISCConfig.CmdValue.REPLY_LOGIN_SUCCESSFUL, "Login Successful.", "").setSenderName(nodeName));
                    log.debug("Login Successful!");
                }


                continueFlag = false;

                break;
            }

            case ISCConfig.CmdValue.LOGOUT: {
                log.debug("Client logout:" + channelID);
                netResources.doLogout(channelID);


                continueFlag = false;

                break;
            }
            default: {


                if (!netResources.isOnline(channelID)) {

                    log.debug("Online clients:" + netResources.onlineTotal() + ", online list:" + netResources.onlineList() + ", check client:" + channelID);


                    if (IServerConfig.offlineDisconnection) {

                        SenderService.send(ctx, new NetData(ISCConfig.CmdValue.REPLY_LOGIN_OFFLINE, "Login Offline.", "").setFlag(false));

                        ctx.deregister();
                        ctx.disconnect();


                        continueFlag = false;
                    } else {

                        String senderName = ServerService.getNodeName(netResources);

                        NetData qpNetData = new NetData(ISCConfig.CmdValue.QUERY_PASSWORD, "What is your password?", "");
                        qpNetData.setSenderName(senderName);

                        qpNetData.setFlag(false);

                        qpNetData.setReturnedNetData(netData);

                        SenderService.send(ctx, qpNetData);
                        log.debug("Password for login");


                        continueFlag = false;
                    }
                }

                break;
            }
        }

        return continueFlag;
    }

}