package org.nachain.core.intermediate;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.transaction.*;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.intermediate.feedback.Feedback;
import org.nachain.core.intermediate.feedback.FeedbackService;
import org.nachain.core.intermediate.feedback.StateFeedback;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;


@Slf4j
public class AccountBalanceService {


    public static AccountBalance newAccountBalance(String address, long instance, long token) {
        AccountBalance account = new AccountBalance();
        account.setInstance(instance);
        account.setToken(token);
        account.setAddress(address);
        account.setBalance(BigInteger.ZERO);
        account.setHistoryIn(BigInteger.ZERO);
        account.setHistoryOut(BigInteger.ZERO);
        account.setHistoryGasOut(BigInteger.ZERO);
        account.setBlockHeight(0);
        account.setTimestamp(0);

        return account;
    }


    public static AccountBalance getAccountBalance(String address, long instance, long token) throws RocksDBException, ExecutionException {
        AccountBalanceDAO accountBalanceDAO = new AccountBalanceDAO(instance);
        AccountBalance accountBalance = accountBalanceDAO.find(address, token);

        return accountBalance;
    }


    public static StateFeedback saveBalance(long instance, Tx tx, TxContext txContext, long blockHeight, long blockTimestamp) throws RocksDBException, IOException, ExecutionException {

        if (tx.getTxType() == TxType.TRANSFER_CHANGE.value) {
            return null;
        }

        if (tx.getValue().compareTo(BigInteger.ZERO) != 1) {
            return null;
        }

        log.debug("[instance={}] saveBalance() Tx={}", instance, tx);

        long fromInstanceId = 0;
        long toInstanceId = 0;
        String fromAddress = tx.getFrom();
        String toAddress = tx.getTo();


        boolean isCross = txContext.getReferrerInstance() != txContext.getCrossToInstance();


        if (!isCross) {
            fromInstanceId = instance;
            toInstanceId = instance;
        } else {

            if (tx.getTo().equals(TxReservedWord.TRANSFER_CROSS_OUT.name)) {
                fromInstanceId = txContext.getReferrerInstance();
                toInstanceId = txContext.getReferrerInstance();
            } else if (tx.getFrom().equals(TxReservedWord.TRANSFER_CROSS_IN.name)) {
                fromInstanceId = txContext.getCrossToInstance();
                toInstanceId = txContext.getCrossToInstance();
            }
        }


        AccountBalanceDAO fromAccountBalanceDAO = new AccountBalanceDAO(fromInstanceId);


        AccountBalance fromBalance = fromAccountBalanceDAO.find(fromAddress, tx.getToken());

        if (fromBalance == null) {
            fromBalance = AccountBalanceService.newAccountBalance(fromAddress, fromInstanceId, tx.getToken());
        }

        BigInteger beforeFrom = fromBalance.getBalance();
        BigInteger afterFrom = BigInteger.ZERO;

        fromBalance.subBalance(tx.getValue());

        fromBalance.addGas(tx.getGas());

        fromBalance.setBlockHeight(blockHeight);
        fromBalance.setTimestamp(blockTimestamp);

        if (tx.getToken() == TxGasType.NAC.value) {
            if (tx.getGasType() == TxGasType.NAC.value) {

                fromBalance.subBalance(tx.getGas());
            } else if (tx.getGasType() == TxGasType.USDN.value) {

            }
        }

        afterFrom = fromBalance.getBalance();

        fromAccountBalanceDAO.put(fromBalance);

        log.debug("fromAddress:" + fromAddress + ", fromBalance:" + fromBalance);


        AccountBalanceDAO toAccountBalanceDAO = new AccountBalanceDAO(toInstanceId);

        AccountBalance toBalance = toAccountBalanceDAO.find(toAddress, tx.getToken());
        if (toBalance == null) {
            toBalance = AccountBalanceService.newAccountBalance(toAddress, toInstanceId, tx.getToken());
        }

        BigInteger beforeTo = toBalance.getBalance();

        BigInteger afterTo = BigInteger.ZERO;
        toBalance.addBalance(tx.getValue());
        toBalance.setBlockHeight(blockHeight);
        toBalance.setTimestamp(blockTimestamp);

        toAccountBalanceDAO.put(toBalance);

        log.debug("toAddress:" + toAddress + ", toBalance:" + toBalance);

        return new StateFeedback(beforeFrom, afterFrom, beforeTo, afterTo);
    }


    public static Feedback checkBalanceEnough(TxBase txBase) throws RocksDBException, IOException, ExecutionException {

        AccountBalance accountBalance = AccountBalanceService.getAccountBalance(txBase.getFrom(), txBase.getInstance(), txBase.getToken());
        BigInteger balance = accountBalance.getBalance();


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


    public static Feedback checkGasBalanceEnough(TxBase txBase) throws RocksDBException, ExecutionException {

        AccountBalance nacAccountBalance = AccountBalanceService.getAccountBalance(txBase.getFrom(), txBase.getInstance(), TxGasType.NAC.value);
        if (nacAccountBalance == null) {
            return FeedbackService.newFeedback().asFail().setMessage(String.format("Gas not enough."));
        } else if (nacAccountBalance.getBalance().compareTo(txBase.getGas()) == -1) {

            return FeedbackService.newFeedback().asFail().setMessage(String.format("Gas not enough. Account Balance=%d, Tx Gas=%d", nacAccountBalance.getBalance(), txBase.getGas()));
        }

        return FeedbackService.newFeedback().asSucceed();
    }


}
