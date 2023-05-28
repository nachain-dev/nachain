package org.nachain.core.networks.chain.client.action;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.networks.chain.BlocksResultDTO;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.factory.IAction;
import org.nachain.core.util.JsonUtils;


public class _GetBlocksResult implements IAction {

    @Override
    public void execute(ChannelHandlerContext ctx, NetData netData, NetResources netResources) {

        BlocksResultDTO blocksResultDTO = JsonUtils.jsonToPojo(netData.getData().toString(), BlocksResultDTO.class);
    }

}

