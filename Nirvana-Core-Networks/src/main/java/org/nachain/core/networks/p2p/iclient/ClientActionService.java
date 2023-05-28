package org.nachain.core.networks.p2p.iclient;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.iclient.global.IClientConfig;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.SenderService;
import org.nachain.core.networks.p2p.isc.action.ActionService;
import org.nachain.core.networks.p2p.isc.global.ISCConfig;

import java.util.Vector;


@Slf4j
public class ClientActionService {


    public static boolean basic(ChannelHandlerContext ctx, NetResources netResources, NetData netData) {


        boolean continueFlag = true;

        log.debug("Execute Command=" + netData.getCmdValue() + ", info=" + netData.getCmdInfo());


        ActionService.register(ctx, netResources, netData);


        if (!netData.isFlag()) {

            if (netData.getReturnedNetData() != null) {
                netResources.getReturnedVector().add(netData.getReturnedNetData());

                log.debug("ReturnedNetData:" + netData.getReturnedNetData().getCmdInfo());
            }
        } else {
            Vector<NetData> returnedVector = netResources.getReturnedVector();
            if (returnedVector.size() > 0) {

                while (!returnedVector.isEmpty()) {

                    NetData returnedNetData = returnedVector.remove(0);
                    SenderService.send(ctx, returnedNetData);

                    log.debug("returnedNetData:" + returnedNetData.getCmdInfo());
                }
            }
        }


        try {

            switch (netData.getCmdValue()) {

                case ISCConfig.CmdValue.QUERY_PASSWORD: {


                    log.debug("Tell the server my password:" + IClientConfig.defaultClientNode.getAuthPassword());


                    IClient iClient = (IClient) netResources.getIServerClient();

                    NetData loginND = new NetData(ISCConfig.CmdValue.LOGIN, "Auth Login, Reply to the password.", iClient.getNodeName(), IClientConfig.defaultClientNode.getAuthPassword());


                    SenderService.send(ctx, loginND);


                    continueFlag = false;

                    break;
                }

                case ISCConfig.CmdValue.REPLY_PASSWORD_ERROR: {

                    log.warn("Login password error, disconnect the server!");


                    ctx.deregister();
                    ctx.disconnect();


                    continueFlag = false;

                    break;
                }

                case ISCConfig.CmdValue.REPLY_LOGIN_SUCCESSFUL: {

                    log.warn("Login success!");

                    netResources.asReady();


                    continueFlag = false;

                    break;
                }

                case ISCConfig.CmdValue.REPLY_LOGIN_OFFLINE: {

                    log.warn("This client is offline on the server, so take the initiative to disconnect!");

                    ctx.deregister();
                    ctx.disconnect();


                    continueFlag = false;

                    break;
                }
                default: {

                    log.debug("Undefined command:" + netData.getCmdValue());
                    break;
                }
            }
        } catch (Exception e) {

            log.error("Basic Event Handling", e);
        }

        return continueFlag;

    }
}