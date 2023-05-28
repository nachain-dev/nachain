package org.nachain.core.chain.structure.instance.logicchain;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.config.Constants;
import org.nachain.core.util.CommUtils;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;


public class DPoSMinedService {


    public static long calcSchedule(long instance, long blockHeight) {

        long BLOCKS_PER_DAY = (instance == CoreInstanceEnum.APPCHAIN.id) ? Constants.PoWF_BLOCKS_PER_DAY : Constants.DPoS_BLOCKS_PER_DAY;


        long blockCycle = 0;

        long mod = blockHeight % BLOCKS_PER_DAY;
        if (mod > 0) {
            blockCycle = 1;
        }
        return blockCycle + blockHeight / BLOCKS_PER_DAY;
    }


    public static void minedCycle(long instance, String minerAddress, long blockHeight, BigInteger newReward) throws RocksDBException, ExecutionException {
        DPoSMinedCycleDAO dPoSMinedDAO = new DPoSMinedCycleDAO(instance);


        long blockCycle = DPoSMinedService.calcSchedule(instance, blockHeight);


        DPoSMinedCycle dPoSMined = dPoSMinedDAO.find(minerAddress, blockCycle);

        if (dPoSMined == null) {
            dPoSMined = new DPoSMinedCycle();

            dPoSMined.setMinerAddress(minerAddress);

            dPoSMined.setTokenValue(newReward);

            dPoSMined.setSchedule(blockCycle);

            dPoSMined.setTimestamp(CommUtils.currentTimeMillis());

            dPoSMinedDAO.add(dPoSMined);
        } else {
            dPoSMined.addTokenValue(newReward);
            dPoSMinedDAO.put(dPoSMined);
        }
    }


}
