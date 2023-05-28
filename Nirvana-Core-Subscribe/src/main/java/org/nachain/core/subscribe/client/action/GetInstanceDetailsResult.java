package org.nachain.core.subscribe.client.action;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.chain.structure.instance.InstanceDetail;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.factory.IAction;
import org.nachain.core.util.JsonUtils;

import java.util.List;


public class GetInstanceDetailsResult implements IAction {

    @Override
    public void execute(ChannelHandlerContext ctx, NetData netData, NetResources netResources) {

        List<InstanceDetail> InstanceDetailList = JsonUtils.jsonToList(netData.getData().toString(), InstanceDetail.class);


        for (InstanceDetail instanceDetail : InstanceDetailList) {
            System.out.println(instanceDetail);
        }
    }

}
