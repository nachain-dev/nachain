package org.nachain.core.networks.chain.server.action;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.networks.chain.BlockDTO;
import org.nachain.core.networks.chain.BlockResultDTO;
import org.nachain.core.networks.chain.server.service.BlockResultDTOService;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.SenderService;
import org.nachain.core.networks.p2p.isc.factory.IAction;
import org.nachain.core.networks.p2p.iserver.ServerService;
import org.nachain.core.util.JsonUtils;


public class GetBlock implements IAction {

    @Override
    public void execute(ChannelHandlerContext ctx, NetData netData, NetResources netResources) {

        BlockDTO blockDTO = JsonUtils.jsonToPojo(netData.getData().toString(), BlockDTO.class);

        long instance = blockDTO.getInstance();
        long blockHeight = blockDTO.getBlockHeight();


        BlockResultDTO blockResultDTO = BlockResultDTOService.getBlockResultDTO(instance, blockHeight);


        SenderService.sendJsonNetData(ctx, "/getBlockResult", "Read block information", ServerService.getNodeName(netResources), blockResultDTO);
    }

}
