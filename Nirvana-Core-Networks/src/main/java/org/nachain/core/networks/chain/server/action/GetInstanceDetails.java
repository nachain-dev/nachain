package org.nachain.core.networks.chain.server.action;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.chain.structure.instance.InstanceDetail;
import org.nachain.core.chain.structure.instance.InstanceDetailDAO;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.SenderService;
import org.nachain.core.networks.p2p.isc.factory.IAction;
import org.nachain.core.networks.p2p.iserver.ServerService;

import java.util.List;


public class GetInstanceDetails implements IAction {

    @Override
    public void execute(ChannelHandlerContext ctx, NetData netData, NetResources netResources) {


        InstanceDetailDAO instanceDetailDAO = new InstanceDetailDAO();

        List<InstanceDetail> instanceDetailList = instanceDetailDAO.db().findAll(InstanceDetail.class);


        SenderService.sendJsonNetData(ctx, "/getInstanceDetailsResult", "Read all instance details", ServerService.getNodeName(netResources), instanceDetailList);
    }

}
