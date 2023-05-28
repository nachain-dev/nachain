package org.nachain.core.chain.mined;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceDetailService;
import org.nachain.core.config.Constants;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;


public class CollectMinedService {

    public static CollectMined newCollectMined(long blockHeight, BigInteger value, BigInteger nodeValue) {
        CollectMined collectMined = new CollectMined();
        collectMined.setBlockHeight(blockHeight);
        collectMined.setValue(value);
        collectMined.setNodeValue(nodeValue);
        return collectMined;
    }


    public static CollectMined getNacCollectMined() throws RocksDBException {
        long instanceId = CoreInstanceEnum.NAC.id;
        return getCollectMined(instanceId);
    }


    public static CollectMined getAppCollectMined() throws RocksDBException {
        long instanceId = CoreInstanceEnum.APPCHAIN.id;
        return getCollectMined(instanceId);
    }


    public static CollectMined getCollectMined(long instanceId) throws RocksDBException {

        long blockHeight = InstanceDetailService.getBlockHeight(instanceId);


        if (instanceId == CoreInstanceEnum.APPCHAIN.id) {
            blockHeight = blockHeight / Constants.PoWF_BLOCKS_PER_DAY * Constants.PoWF_BLOCKS_PER_DAY;
        } else {
            blockHeight = blockHeight / Constants.DPoS_BLOCKS_PER_DAY * Constants.DPoS_BLOCKS_PER_DAY;
        }

        CollectMinedDAO collectMinedDAO = new CollectMinedDAO(instanceId);
        CollectMined collectMined = collectMinedDAO.get(blockHeight);
        return collectMined;
    }

}
