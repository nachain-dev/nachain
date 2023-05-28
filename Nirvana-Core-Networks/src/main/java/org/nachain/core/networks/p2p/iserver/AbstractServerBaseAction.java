package org.nachain.core.networks.p2p.iserver;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.SCService;
import org.nachain.core.networks.p2p.isc.action.ActionService;
import org.nachain.core.networks.p2p.isc.factory.IAction;
import org.nachain.core.networks.p2p.isc.factory.IBaseAction;
import org.nachain.core.networks.p2p.isc.factory.ISCAction;
import org.nachain.core.util.JsonUtils;
import org.nachain.libs.action.ActionUtil;
import org.nachain.libs.util.CommUtil;


@Slf4j
public abstract class AbstractServerBaseAction implements ISCAction, IBaseAction {


    @Override
    public void connect(ChannelHandlerContext ctx, NetResources netResources) {

        ServerActionService.connection(ctx, netResources);


        actionConnect(ctx, netResources);
    }


    @Override
    public void disconnect(ChannelHandlerContext ctx, NetResources netResources) {

        ActionService.disconnect(ctx, netResources);


        actionDisconnect(ctx, netResources);
    }


    @Override
    public void read(ChannelHandlerContext ctx, Object msg, NetResources netResources) throws Exception {

        NetData netData = JsonUtils.jsonToPojo((String) msg, NetData.class);


        boolean continueFlag = ServerActionService.basic(ctx, netResources, netData);


        if (continueFlag) {
            if (!CommUtil.isEmpty(netData.getCmdName())) {

                String actionClass = ActionUtil.getActionClass(netData.getCmdName(), getClassPath());

                IAction iAction = SCService.instanceAction(actionClass);
                if (iAction != null) {
                    log.debug("Execute Action:" + actionClass);


                    iAction.execute(ctx, netData, netResources);
                }
            }


            actionRead(ctx, netData, netResources);
        }
    }


}