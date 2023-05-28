package org.nachain.core.networks.chain.server.action;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.chain.structure.instance.InstanceDetail;
import org.nachain.core.chain.structure.instance.InstanceDetailDAO;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.SenderService;
import org.nachain.core.networks.p2p.isc.factory.IAction;
import org.nachain.core.networks.p2p.iserver.ServerService;
import org.rocksdb.RocksDBException;


public class GetInstanceDetail implements IAction {

    @Override
    public void execute(ChannelHandlerContext ctx, NetData netData, NetResources netResources) {


        InstanceDetailDAO instanceDetailDAO = new InstanceDetailDAO();

        long instance = netData.toDataLong();

        InstanceDetail instanceDetail;
        try {
            instanceDetail = instanceDetailDAO.get(instance);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }


        SenderService.sendJsonNetData(ctx, "/getInstanceDetailResult", "Read instance detail", ServerService.getNodeName(netResources), instanceDetail);
    }

}
