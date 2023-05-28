package org.nachain.core.chain.instanceprofile;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.TxBase;
import org.nachain.core.chain.transaction.TxReservedWord;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxContextService;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMarkService;
import org.nachain.core.intermediate.dto.WithdrawalDTO;
import org.nachain.core.util.CommUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;


@Slf4j
public class InstanceProfileService {


    public static InstanceProfile getInstanceProfile(String address, long instance, long token) throws RocksDBException, IOException {
        InstanceProfileDAO instanceProfileDAO = new InstanceProfileDAO(instance);
        InstanceProfile instanceProfile = instanceProfileDAO.get(address, token);
        return instanceProfile;
    }


    public static InstanceProfile newInstanceProfile(String address, long instance, long token, long blockHeight) {
        InstanceProfile instanceProfile = new InstanceProfile();
        instanceProfile.setInstance(instance);
        instanceProfile.setToken(token);
        instanceProfile.setAddress(address);
        instanceProfile.setBalance(BigInteger.ZERO);
        instanceProfile.setHistoryIn(BigInteger.ZERO);
        instanceProfile.setHistoryOut(BigInteger.ZERO);
        instanceProfile.setBlockHeight(blockHeight);
        instanceProfile.setTimestamp(CommUtils.currentTimeMillis());

        return instanceProfile;
    }


    public static void depositProfile(Tx tx) throws RocksDBException, IOException {
        long instanceId = tx.getInstance();
        long token = tx.getToken();
        String fromAddress = tx.getFrom();
        String toAddress = tx.getTo();
        BigInteger addValue = tx.getValue();
        long blockHeight = tx.getBlockHeight();
        long txTimestamp = tx.getTimestamp();


        depositProfile(instanceId, token, fromAddress, toAddress, addValue, blockHeight, txTimestamp);
    }


    public static void depositProfile(long instanceId, long token, String fromAddress, String toAddress, BigInteger addValue, long blockHeight, long txTimestamp) throws RocksDBException, IOException {
        if (!toAddress.equals(TxReservedWord.INSTANCE.name)) {
            return;
        }

        InstanceProfileDAO instanceProfileDAO = new InstanceProfileDAO(instanceId);
        InstanceProfile instanceProfile = instanceProfileDAO.get(fromAddress, token);

        if (instanceProfile == null) {
            instanceProfile = newInstanceProfile(fromAddress, instanceId, token, blockHeight);
        }


        instanceProfile.setBalance(instanceProfile.getBalance().add(addValue));
        instanceProfile.setBlockHeight(blockHeight);
        instanceProfile.setTimestamp(txTimestamp);

        instanceProfileDAO.put(instanceProfile);

        log.debug("address:" + fromAddress + ", instanceProfile:" + instanceProfile);
    }


    public static void withdrawalProfile(Tx wdTx) throws RocksDBException, IOException {
        long instance = wdTx.getInstance();
        long token = wdTx.getToken();
        String fromAddress = wdTx.getFrom();
        String toAddress = wdTx.getTo();
        long blockHeight = wdTx.getBlockHeight();
        long txTimestamp = wdTx.getTimestamp();


        TxContext<WithdrawalDTO> withdrawalTC = TxContextService.toTxContext(wdTx.getContext());

        WithdrawalDTO withdrawalDTO = withdrawalTC.getData();


        withdrawalProfile(instance, token, fromAddress, toAddress, blockHeight, txTimestamp, withdrawalDTO.getAmount());
    }


    public static void withdrawalProfile(long instance, long token, String fromAddress, String toAddress, long blockHeight, long txTimestamp, BigInteger withdrawalAmount) throws RocksDBException, IOException {


        if (!fromAddress.equals(toAddress)) {
            return;
        }


        if (checkBalanceEnough(fromAddress, instance, token, withdrawalAmount)) {
            throw new ChainException("Withdrawal not enough");
        }

        InstanceProfileDAO instanceProfileDAO = new InstanceProfileDAO(instance);
        InstanceProfile instanceProfile = instanceProfileDAO.get(fromAddress, token);

        if (instanceProfile != null) {

            instanceProfile.setBalance(instanceProfile.getBalance().subtract(withdrawalAmount));
            instanceProfile.setBlockHeight(blockHeight);
            instanceProfile.setTimestamp(txTimestamp);

            instanceProfileDAO.put(instanceProfile);
        }

        log.debug("address:" + fromAddress + ", instanceProfile:" + instanceProfile);
    }


    public static void transferInstanceProfile(long instance, long token, BigInteger amount, String fromAddress, String toAddress, long blockHeight, long txTimestamp) throws RocksDBException, IOException {
        InstanceProfileDAO instanceProfileDAO = new InstanceProfileDAO(instance);

        InstanceProfile fromProfile = instanceProfileDAO.get(fromAddress, token);
        {

            if (fromProfile == null) {
                fromProfile = newInstanceProfile(fromAddress, instance, token, blockHeight);
            }

            BigInteger fromBI = fromProfile.getBalance().subtract(amount);
            fromProfile.setBalance(fromBI);
            fromProfile.setHistoryOut(fromProfile.getHistoryOut().add(amount));
            fromProfile.setBlockHeight(blockHeight);
            fromProfile.setTimestamp(txTimestamp);

            instanceProfileDAO.put(fromProfile);

            log.debug("fromAddress:" + fromAddress + ", fromBalance:" + fromProfile);
        }


        InstanceProfile toProfile = instanceProfileDAO.get(toAddress, token);
        {
            if (toProfile == null) {
                toProfile = newInstanceProfile(toAddress, instance, token, blockHeight);
            }

            BigInteger toBI = toProfile.getBalance().add(amount);
            toProfile.setBalance(toBI);
            toProfile.setHistoryIn(toProfile.getHistoryIn().add(amount));
            toProfile.setBlockHeight(blockHeight);
            toProfile.setTimestamp(txTimestamp);

            instanceProfileDAO.put(toProfile);

            log.debug("toAddress:" + toAddress + ", toBalance:" + toProfile);
        }

    }


    public static boolean checkBalanceEnough(TxBase withdrawalTxBase) throws RocksDBException, IOException {
        TxContext<WithdrawalDTO> withdrawalTC = TxContextService.toTxContext(withdrawalTxBase.getContext());

        WithdrawalDTO withdrawalDTO = withdrawalTC.getData();


        return checkBalanceEnough(withdrawalTxBase.getFrom(), withdrawalTxBase.getInstance(), withdrawalTxBase.getToken(), withdrawalDTO.getAmount());
    }


    public static boolean checkBalanceEnough(String fromAddress, long instance, long token, BigInteger checkAmount) throws RocksDBException, IOException {

        InstanceProfile instanceProfile = InstanceProfileService.getInstanceProfile(fromAddress, instance, token);
        if (instanceProfile == null) {
            return false;
        }
        BigInteger balance = instanceProfile.getBalance();

        if (balance.compareTo(checkAmount) == -1) {

            return false;
        }


        return true;
    }


    public static TxContext<WithdrawalDTO> newTxContext(WithdrawalDTO withdrawalDTO) {

        TxContext txContext = new TxContext().setInstanceType(InstanceType.Token);

        txContext.setEventType(TxEventType.TOKEN_INSTANCE_WITHDRAWAL);

        txContext.setReferrerInstance(CoreInstanceEnum.NULL.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.NULL.id);

        txContext.setData(withdrawalDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


}
