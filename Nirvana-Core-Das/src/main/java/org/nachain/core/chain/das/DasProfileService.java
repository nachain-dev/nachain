package org.nachain.core.chain.das;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.config.PricingSystem;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.transaction.TxBase;
import org.nachain.core.chain.transaction.TxGasType;
import org.nachain.core.intermediate.feedback.Feedback;
import org.nachain.core.intermediate.feedback.FeedbackService;
import org.nachain.core.intermediate.feedback.StateFeedback;
import org.nachain.core.token.CoreTokenEnum;
import org.nachain.core.util.CommUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
public class DasProfileService {


    public static DasProfile getDasProfile(String address, long instance, long token) throws RocksDBException, IOException {
        DasProfileDAO dasProfileDAO = new DasProfileDAO(instance);
        DasProfile dasProfile = dasProfileDAO.get(address, token);
        return dasProfile;
    }


    public static DasProfile newDasProfile(String address, long instance, long token) {
        DasProfile dasProfile = new DasProfile();
        dasProfile.setInstance(instance);
        dasProfile.setToken(token);
        dasProfile.setAddress(address);
        dasProfile.setBalance(BigInteger.ZERO);
        dasProfile.setHistoryIn(BigInteger.ZERO);
        dasProfile.setHistoryOut(BigInteger.ZERO);
        dasProfile.setHistoryGas(BigInteger.ZERO);
        dasProfile.setTxHeight(0);
        dasProfile.setTimestamp(CommUtils.currentTimeMillis());

        return dasProfile;
    }


    private static void addBalance(long instance, long token, String address, BigInteger addValue, long txHeight, long txTimestamp) throws RocksDBException, IOException {
        DasProfileDAO dasProfileDAO = new DasProfileDAO(instance);
        DasProfile dasProfile = dasProfileDAO.get(address, token);

        if (dasProfile == null) {
            dasProfile = newDasProfile(address, instance, token);
        }

        dasProfile.setBalance(dasProfile.getBalance().add(addValue));
        dasProfile.setTxHeight(txHeight);
        dasProfile.setTimestamp(txTimestamp);

        dasProfileDAO.put(dasProfile);

        log.debug("address:" + address + ", dasProfile:" + dasProfile);
    }


    public static StateFeedback saveDasProfile(long instance, TxDas txDas, long txHeight, long txTimestamp) throws RocksDBException, IOException {
        String fromAddress = txDas.getFrom();
        String toAddress = txDas.getTo();
        BigInteger value = txDas.getValue();
        long token = txDas.getToken();


        DasProfileDAO dasProfileDAO = new DasProfileDAO(instance);

        BigInteger beforeFrom;
        BigInteger afterFrom;


        {
            DasProfile fromProfile = dasProfileDAO.get(fromAddress, token);

            if (fromProfile == null) {
                fromProfile = newDasProfile(fromAddress, instance, token);
            }


            beforeFrom = fromProfile.getBalance();

            afterFrom = fromProfile.getBalance().subtract(txDas.getValue());

            fromProfile.setBalance(afterFrom);
            fromProfile.setHistoryOut(fromProfile.getHistoryOut().add(value));
            fromProfile.setHistoryGas(fromProfile.getHistoryGas().add(txDas.getGas()));
            fromProfile.setTxHeight(txHeight);
            fromProfile.setTimestamp(txTimestamp);

            dasProfileDAO.put(fromProfile);

            log.debug("fromAddress:" + fromAddress + ", fromBalance:" + fromProfile);
        }


        if (txDas.getGasType() == TxGasType.USDN.value) {

            DasProfile fromBalance = dasProfileDAO.get(fromAddress, CoreTokenEnum.USDN.id);

            if (fromBalance == null) {
                fromBalance = DasProfileService.newDasProfile(fromAddress, instance, CoreTokenEnum.USDN.id);
            }

            fromBalance.setBalance(fromBalance.getBalance().subtract(txDas.getGas()));
            fromBalance.setHistoryOut(fromBalance.getHistoryOut().add(txDas.getGas()));
            fromBalance.setTimestamp(txTimestamp);

            dasProfileDAO.put(fromBalance);
        }


        BigInteger beforeTo;
        BigInteger afterTo;
        {
            DasProfile toProfile = dasProfileDAO.get(toAddress, token);
            if (toProfile == null) {
                toProfile = newDasProfile(toAddress, instance, token);
            }

            BigInteger toBI = toProfile.getBalance().add(value);


            beforeTo = toProfile.getBalance();
            afterTo = toBI;

            toProfile.setBalance(toBI);
            toProfile.setHistoryIn(toProfile.getHistoryIn().add(value));
            toProfile.setTxHeight(txHeight);
            toProfile.setTimestamp(txTimestamp);

            dasProfileDAO.put(toProfile);

            log.debug("toAddress:" + toAddress + ", toBalance:" + toProfile);
        }


        BigInteger gasTotal = txDas.getGas();

        if (gasTotal.compareTo(BigInteger.ZERO) == 1) {

            BigInteger destroy = new BigDecimal(gasTotal).multiply(PricingSystem.Destroy.ALL_RATIO).toBigInteger();

            gasTotal = gasTotal.subtract(destroy);

            gasTotal = gasTotal.subtract(txDas.getBleedValue());

            addBalance(instance, token, txDas.getMinedAddress(), gasTotal, txHeight, txTimestamp);
        }

        return new StateFeedback(beforeFrom, afterFrom, beforeTo, afterTo);
    }


    public static Feedback checkBalanceEnough(TxBase txBase) throws RocksDBException, IOException {


        DasProfile dasProfile = DasProfileService.getDasProfile(txBase.getFrom(), txBase.getInstance(), txBase.getToken());
        BigInteger balance = dasProfile.getBalance();


        if (txBase.getToken() == CoreInstanceEnum.NAC.id) {

            if (balance.compareTo(txBase.getValue().add(txBase.getGas())) == -1) {

                return FeedbackService.newFeedback().asFail().setMessage("Token and gas not enough.");
            }
        } else {

            if (balance.compareTo(txBase.getValue()) == -1) {

                return FeedbackService.newFeedback().asFail().setMessage("Token not enough.");
            }

            if (checkGasBalanceEnough(txBase).isFailed()) {
                return FeedbackService.newFeedback().asFail().setMessage("Gas not enough.");
            }
        }

        return FeedbackService.newFeedback().asSucceed();
    }


    public static Feedback checkGasBalanceEnough(TxBase txBase) throws RocksDBException, IOException {

        DasProfile dasProfile = DasProfileService.getDasProfile(txBase.getFrom(), txBase.getInstance(), CoreTokenEnum.USDN.id);
        if (dasProfile.getBalance().compareTo(txBase.getGas()) == -1) {

            return FeedbackService.newFeedback().asFail().setMessage("Gas not enough.");
        }

        return FeedbackService.newFeedback().asSucceed();
    }

}
