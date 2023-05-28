package org.nachain.core.networks.chain.server.action;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.networks.chain.BlockResultDTO;
import org.nachain.core.networks.chain.BlocksDTO;
import org.nachain.core.networks.chain.BlocksResultDTO;
import org.nachain.core.networks.chain.server.service.BlockResultDTOService;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.SenderService;
import org.nachain.core.networks.p2p.isc.factory.IAction;
import org.nachain.core.networks.p2p.iserver.ServerService;
import org.nachain.core.util.JsonUtils;


public class GetBlocks implements IAction {

    @Override
    public void execute(ChannelHandlerContext ctx, NetData netData, NetResources netResources) {

        BlocksDTO blocksDTO = JsonUtils.jsonToPojo(netData.getData().toString(), BlocksDTO.class);

        long instance = blocksDTO.getInstance();
        long blockHeight = blocksDTO.getBlockHeight();
        long endHeight = blocksDTO.getEndHeight();


        BlocksResultDTO blocksResultDTO = BlockResultDTOService.newBlocksResultDTO();
        blocksResultDTO.setInstance(instance);
        blocksResultDTO.setBlockHeight(blockHeight);
        blocksResultDTO.setEndHeight(endHeight);

        while (true) {
            BlockResultDTO blockResultDTO = BlockResultDTOService.getBlockResultDTO(instance, blockHeight);

            blocksResultDTO.getBlockResultDTOList().add(blockResultDTO);

            if (blockHeight < endHeight) {
                blockHeight++;
            } else {
                break;
            }
        }


        SenderService.sendJsonNetData(ctx, "/getBlocksResult", "Read block information in batches", ServerService.getNodeName(netResources), blocksResultDTO);
    }

}
