package org.nachain.core.subscribe.client.action;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.Feedback;
import org.nachain.core.chain.block.Block;
import org.nachain.core.chain.block.BlockBody;
import org.nachain.core.chain.block.BlockBodyDAO;
import org.nachain.core.chain.block.BlockDAO;
import org.nachain.core.chain.structure.instance.InstanceDetail;
import org.nachain.core.chain.structure.instance.InstanceDetailDAO;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.TxDAO;
import org.nachain.core.chain.transaction.TxType;
import org.nachain.core.networks.chain.BlockResultDTO;
import org.nachain.core.networks.p2p.isc.NetData;
import org.nachain.core.networks.p2p.isc.NetResources;
import org.nachain.core.networks.p2p.isc.factory.IAction;
import org.nachain.core.subscribe.client.process.ITxProcess;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Slf4j
public abstract class AbstractGetBlockResult implements IAction, ITxProcess {

    @Override
    public void execute(ChannelHandlerContext ctx, NetData netData, NetResources netResources) {

        BlockResultDTO result = JsonUtils.jsonToPojo(netData.getData().toString(), BlockResultDTO.class);


        saveResult(result);
    }


    public void saveResult(BlockResultDTO result) {

        long instance = result.getInstance();
        long blockHeight = result.getBlockHeight();
        long latestBlockHeight = result.getLatestBlockHeight();
        try {
            BlockDAO blockDAO = new BlockDAO(instance);
            BlockBodyDAO blockBodyDAO = new BlockBodyDAO(instance);
            TxDAO txDAO = new TxDAO(instance);
            InstanceDetailDAO instanceDetailDAO = new InstanceDetailDAO();


            Block block = result.getBlock();
            BlockBody blockBody = result.getBlockBody();
            List<Tx> txList = result.getTxList();
            InstanceDetail instanceDetail = result.getInstanceDetail();


            if (block == null) {
                log.debug("Not new block, blockHeight=" + blockHeight + ", latestBlockHeight=" + latestBlockHeight + ", instance=" + instance);
                return;
            }


            Block existBlock = blockDAO.get(blockHeight);
            if (existBlock != null) {
                log.warn("Block already existed, blockHeight=" + blockHeight + ", latestBlockHeight=" + latestBlockHeight);
                return;
            }


            for (Tx tx : txList) {
                txDAO.add(tx);


                if (tx.getTxType() != TxType.TRANSFER.value) {
                    continue;
                }


                try {
                    Feedback feedback = process(tx);
                    if (feedback.isFailed()) {
                        log.error("{}. Form={}, To={}, Value={}", feedback.getMessage(), tx.getFrom(), tx.getTo(), tx.getValue());
                    }
                } catch (Exception e) {
                    log.error("TxProcess.process({}) -> {}", tx.getHash(), e);
                }
            }


            blockBodyDAO.add(blockBody);
            blockDAO.add(block);


            instanceDetail.setBlockHeight(blockHeight);
            instanceDetailDAO.put(instanceDetail);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            log.info("GetBlockResult -> blockHeight={}, latestBlockHeight={}", blockHeight, latestBlockHeight);
        }
    }

}
