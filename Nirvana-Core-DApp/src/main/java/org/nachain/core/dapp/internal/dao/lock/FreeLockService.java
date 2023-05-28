package org.nachain.core.dapp.internal.dao.lock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceDetailService;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMarkService;
import org.nachain.core.dapp.internal.dao.lock.events.FreeLockDTO;
import org.nachain.core.dapp.internal.dao.lock.events.FreeUnlockDTO;
import org.nachain.core.oracle.PredictedResultService;
import org.nachain.core.oracle.events.NacPrice;
import org.nachain.core.persistence.page.Page;
import org.nachain.core.persistence.page.PageService;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FreeLockService {

    private static FreeLockDAO freeLockDAO;

    static {
        freeLockDAO = new FreeLockDAO();
    }


    public static FreeLock newFreeLock(String address, double lockNacPrice, long lockDay, long unlockNacBlockHeight, String lockTx, BigInteger amount) {
        FreeLock freeLock = new FreeLock();

        freeLock.setAddress(address);

        freeLock.setLockNacPrice(lockNacPrice);

        freeLock.setLockDay(lockDay);

        freeLock.setUnlockNacBlockHeight(unlockNacBlockHeight);

        freeLock.setLockTx(lockTx);

        freeLock.setAmount(amount);

        freeLock.setHash(freeLock.encodeHashString());

        freeLock.setUnlockTx("");

        freeLock.setWithdrawTx("");

        return freeLock;
    }


    public static FreeLock get(String hash) throws RocksDBException {
        return freeLockDAO.get(hash);
    }


    public static boolean edit(FreeLock freeLock) throws RocksDBException {
        return freeLockDAO.put(freeLock);
    }


    public static List<FreeLock> findAllDesc() throws ExecutionException, RocksDBException {
        List<FreeLock> freeLockList = Lists.newArrayList();

        List<String> descList = PageService.findAllDesc(freeLockDAO.getGroupId(), FreeLock.class);
        for (String itemHash : descList) {
            freeLockList.add(freeLockDAO.get(itemHash));
        }

        return freeLockList;
    }


    public static BigInteger sumLockAmount() {
        BigInteger amount = BigInteger.ZERO;
        List<FreeLock> freeLockList = freeLockDAO.findAll();
        for (FreeLock freeLock : freeLockList) {
            if (freeLock.getUnlockTx().isEmpty())
                amount = amount.add(freeLock.getAmount());
        }
        return amount;
    }


    public static BigInteger sumUnlockAmount() {
        BigInteger amount = BigInteger.ZERO;
        List<FreeLock> freeLockList = freeLockDAO.findAll();
        for (FreeLock freeLock : freeLockList) {
            if (!freeLock.getUnlockTx().isEmpty())
                amount = amount.add(freeLock.getAmount());
        }
        return amount;
    }


    public static long countLockAmount() {
        return freeLockDAO.count();
    }


    public static List<FreeLock> findAddress(String address) throws ExecutionException, RocksDBException {
        List<FreeLock> freeLockList = Lists.newArrayList();

        List<String> descList = PageService.findAllDesc(freeLockDAO.getGroupId(), FreeLock.class, address);
        for (String itemHash : descList) {
            freeLockList.add(freeLockDAO.get(itemHash));
        }

        return freeLockList;
    }


    public static List<FreeLock> findAddress(String address, long pageNum) throws ExecutionException, RocksDBException {
        List<FreeLock> freeLocks = new ArrayList<>();

        Page page = PageService.get(freeLockDAO.getGroupId(), FreeLock.class, address, pageNum);
        if (page == null) {
            return freeLocks;
        }

        List<String> items = page.getItems();
        for (String itemHash : items) {
            freeLocks.add(freeLockDAO.get(itemHash));
        }

        return freeLocks;
    }


    public static boolean isAllowUnlock(String hash) throws Exception {
        boolean rtv = true;

        if (StringUtils.isEmpty(hash)) {
            return false;
        }


        FreeLock freeLock = freeLockDAO.get(hash);
        long unlockNacBlockHeight = freeLock.getUnlockNacBlockHeight();
        double lockNacPrice = freeLock.getLockNacPrice();


        long blockHeight = InstanceDetailService.getBlockHeight(CoreInstanceEnum.NAC.id);


        NacPrice nacPrice = PredictedResultService.getNacPrice(blockHeight);
        BigDecimal bigDecimal = nacPrice.getPrice();


        if (blockHeight < unlockNacBlockHeight) {
            rtv = false;
        }

        if (lockNacPrice < bigDecimal.doubleValue()) {
            rtv = false;
        }

        return rtv;
    }


    public static boolean add(FreeLock freeLock) throws RocksDBException, ExecutionException {
        boolean rtv = freeLockDAO.add(freeLock);


        PageService.addItem(freeLockDAO.getGroupId(), FreeLock.class, freeLock.getAddress(), freeLock.getHash());

        PageService.addItem(freeLockDAO.getGroupId(), FreeLock.class, freeLock.getHash());

        return rtv;
    }


    public static TxContext toFreeLockDTOContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<FreeLockDTO>>() {
        });
    }


    public static TxContext newFreeLockContext(FreeLockDTO freeLockDTO) {

        TxContext txContext = new TxContext<FreeLockDTO>().setInstanceType(InstanceType.Core);

        txContext.setEventType(TxEventType.DAO_FREELOCK);

        txContext.setReferrerInstance(CoreInstanceEnum.FreeLock.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.FreeLock.id);

        txContext.setData(freeLockDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext toFreeUnlockDTOContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<FreeUnlockDTO>>() {
        });
    }


    public static TxContext newFreeUnlockContext(FreeUnlockDTO freeUnlockDTO) {

        TxContext txContext = new TxContext<FreeLockDTO>().setInstanceType(InstanceType.Core);

        txContext.setEventType(TxEventType.DAO_FREEUNLOCK);

        txContext.setReferrerInstance(CoreInstanceEnum.FreeLock.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.FreeLock.id);

        txContext.setData(freeUnlockDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


}
