package org.nachain.core.networks.chain.server.service;

import com.google.common.collect.Lists;
import org.nachain.core.chain.block.Block;
import org.nachain.core.chain.block.BlockBody;
import org.nachain.core.chain.block.BlockBodyDAO;
import org.nachain.core.chain.block.BlockDAO;
import org.nachain.core.chain.structure.instance.InstanceDetail;
import org.nachain.core.chain.structure.instance.InstanceDetailDAO;
import org.nachain.core.chain.structure.instance.InstanceDetailService;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.TxDAO;
import org.nachain.core.networks.chain.BlockResultDTO;
import org.nachain.core.networks.chain.BlocksResultDTO;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;


public class BlockResultDTOService {


    public static BlocksResultDTO newBlocksResultDTO() {
        BlocksResultDTO blocksResultDTO = new BlocksResultDTO();
        blocksResultDTO.setBlockResultDTOList(Lists.newArrayList());

        return blocksResultDTO;
    }


    public static BlockResultDTO newBlockResultDTO() {
        BlockResultDTO blockResultDTO = new BlockResultDTO();

        return blockResultDTO;
    }


    public static BlockResultDTO getBlockResultDTO(long instance, long blockHeight) {
        BlockResultDTO blockResultDTO = new BlockResultDTO();
        try {
            BlockDAO blockDAO = new BlockDAO(instance);
            BlockBodyDAO blockBodyDAO = new BlockBodyDAO(instance);
            TxDAO txDAO = new TxDAO(instance);
            InstanceDetailDAO instanceDetailDAO = new InstanceDetailDAO();


            Block block = blockDAO.get(blockHeight);
            if (block != null) {

                BlockBody blockBody = blockBodyDAO.get(blockHeight);

                List<Tx> txList = Lists.newArrayList();
                for (String txHash : blockBody.getTransactions()) {
                    Tx tx = txDAO.get(txHash);
                    txList.add(tx);
                }

                long latestBlockHeight = InstanceDetailService.getBlockHeight(instance);


                InstanceDetail instanceDetail = instanceDetailDAO.get(instance);


                blockResultDTO.setInstance(instance);
                blockResultDTO.setBlockHeight(blockHeight);
                blockResultDTO.setBlock(block);
                blockResultDTO.setBlockBody(blockBody);
                blockResultDTO.setTxList(txList);
                blockResultDTO.setInstanceDetail(instanceDetail);
                blockResultDTO.setLatestBlockHeight(latestBlockHeight);
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return blockResultDTO;
    }
}
