package org.nachain.core.chain.structure.instance;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.util.CommUtils;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Slf4j
public class InstanceDetailService {

    private static InstanceDetailDAO instanceDetailDAO;

    static {
        instanceDetailDAO = new InstanceDetailDAO();
    }


    public static InstanceDetail newInstanceDetail(long instanceId, long markBlockHeight, long markTxDasHeight) {
        InstanceDetail detail = new InstanceDetail();

        detail.setId(instanceId);

        detail.setBlockHeight(0);

        detail.setTxHeight(BigInteger.ZERO);


        detail.setCoinbase(BigInteger.ZERO);
        detail.setGasUsed(BigInteger.ZERO);
        detail.setGas(BigInteger.ZERO);
        detail.setGasDestroy(BigInteger.ZERO);
        detail.setUninstallAward(BigInteger.ZERO);
        detail.setGasAward(BigInteger.ZERO);
        detail.setDestroyNac(BigInteger.ZERO);
        detail.setBleedValue(BigInteger.ZERO);


        detail.setMarkBlockHeight(markBlockHeight);
        detail.setMarkTxDasHeight(markTxDasHeight);
        detail.setTimestamp(CommUtils.currentTimeMillis());

        return detail;
    }


    public static InstanceDetail addInstanceDetail(long instanceId) throws RocksDBException {
        InstanceDetail instanceDetail = InstanceDetailService.newInstanceDetail(instanceId, 0, 0);
        instanceDetailDAO.put(instanceDetail);
        return instanceDetail;
    }


    public static InstanceDetail getInstanceDetail(long instance) {
        try {
            InstanceDetail instanceDetail = instanceDetailDAO.find(instance);
            return instanceDetail;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public static long getBlockHeight(long instance) throws RocksDBException {
        InstanceDetail instanceDetail = getInstanceDetail(instance);

        if (instanceDetail != null) {
            return instanceDetail.getBlockHeight();
        } else {
            return 0;
        }
    }


    public static long getNacBlockHeight() throws RocksDBException {
        return getBlockHeight(CoreInstanceEnum.NAC.id);
    }


    public static long getAcBlockHeight() throws RocksDBException {
        return getBlockHeight(CoreInstanceEnum.APPCHAIN.id);
    }


    public static long nextBlockHeight(long instance) throws RocksDBException {
        return getBlockHeight(instance) + 1;
    }


    public static BigInteger getTxHeight(long instance) throws RocksDBException {
        InstanceDetail instanceDetail = getInstanceDetail(instance);

        if (instanceDetail != null) {
            return instanceDetail.getTxHeight();
        } else {
            return BigInteger.ZERO;
        }

    }


    public static BigInteger nextTxHeight(long instanceId) throws RocksDBException {
        return getTxHeight(instanceId).add(BigInteger.ONE);
    }


    public static void saveTxHeight(long instanceId, BigInteger txIndexId) {
        try {

            InstanceDetail instanceDetail = getInstanceDetail(instanceId);
            if (instanceDetail == null) {
                instanceDetail = InstanceDetailService.newInstanceDetail(instanceId, 0, 0);
            }

            instanceDetail.setTxHeight(txIndexId);
            instanceDetailDAO.put(instanceDetail);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static void saveBlockHeight(long instanceId, long blockHeight) {
        try {

            InstanceDetail instanceDetail = getInstanceDetail(instanceId);
            if (instanceDetail == null) {
                instanceDetail = InstanceDetailService.newInstanceDetail(instanceId, 0, 0);
            }

            instanceDetail.setBlockHeight(blockHeight);
            instanceDetailDAO.put(instanceDetail);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

}
