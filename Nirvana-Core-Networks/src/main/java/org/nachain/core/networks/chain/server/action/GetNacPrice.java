package org.nachain.core.networks.chain.server.action;

import io.netty.channel.ChannelHandlerContext;
import org.nachain.core.chain.mined.CollectMined;
import org.nachain.core.chain.mined.CollectMinedDAO;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceDetailService;
import org.nachain.core.config.Constants;
import org.nachain.core.networks.chain.NacPriceDTO;
import org.nachain.core.networks.chain.NacPriceResultDTO;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.SenderService;
import org.nachain.core.networks.p2p.isc.factory.IAction;
import org.nachain.core.networks.p2p.iserver.ServerService;
import org.nachain.core.oracle.PredictedResultService;
import org.nachain.core.util.JsonUtils;

import java.math.BigDecimal;
import java.math.BigInteger;


public class GetNacPrice implements IAction {

    @Override
    public void execute(ChannelHandlerContext ctx, NetData netData, NetResources netResources) {

        NacPriceResultDTO resultDTO = new NacPriceResultDTO();

        NacPriceDTO nacPriceDTO = JsonUtils.jsonToPojo(netData.getData().toString(), NacPriceDTO.class);

        long usdPrice = nacPriceDTO.getUsdPrice();


        CollectMinedDAO collectMinedDAO = new CollectMinedDAO(CoreInstanceEnum.NAC.id);

        try {

            BigInteger nacAmount = PredictedResultService.calcNacAmount(usdPrice);

            BigDecimal nacPrice = PredictedResultService.getNacPrice(InstanceDetailService.getBlockHeight(CoreInstanceEnum.APPCHAIN.id)).getPrice();

            BigInteger fullNodeHashRate = BigInteger.ZERO;

            long blockHeight = InstanceDetailService.getBlockHeight(CoreInstanceEnum.NAC.id);

            CollectMined collectMined = collectMinedDAO.get(blockHeight / Constants.DPoS_BLOCKS_PER_DAY * Constants.DPoS_BLOCKS_PER_DAY);
            if (collectMined != null) {
                fullNodeHashRate = collectMined.getNodeValue();
            }


            resultDTO.setNacAmount(nacAmount);
            resultDTO.setNacPrice(nacPrice);
            resultDTO.setFullNodeHashRate(fullNodeHashRate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        SenderService.sendJsonNetData(ctx, "/getNacPriceResult", "Calculate the number of nac required for usd", ServerService.getNodeName(netResources), resultDTO);
    }

}
