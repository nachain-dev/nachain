package org.nachain.core.chain.transaction;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.base.Amount;
import org.nachain.core.base.Unit;
import org.nachain.core.chain.transaction.unused.UnusedPool;
import org.nachain.core.chain.transaction.unused.UnusedPoolDAO;
import org.nachain.core.chain.transaction.unverified.UnverifiedTxDAO;
import org.nachain.core.chain.transaction.unverifiedunused.UnverifiedUnusedPool;
import org.nachain.core.chain.transaction.unverifiedunused.UnverifiedUnusedPoolDAO;
import org.nachain.core.dapp.internal.fullnode.FullNodeDAO;
import org.nachain.core.intermediate.feedback.Feedback;
import org.nachain.core.intermediate.feedback.FeedbackService;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
public class TxBalanceService {


    public static BigDecimal balanceDecimal(long instance, long token, String walletAddress) throws Exception {
        BigInteger balance = TxBalanceService.balance(instance, token, walletAddress);

        Amount amount = Amount.of(balance);

        BigDecimal bigDecimal = amount.toDecimal(Unit.NAC);
        return bigDecimal;
    }


    public static BigDecimal verifiedBalanceDecimal(long instance, long token, String walletAddress) throws Exception {
        BigInteger balance = TxBalanceService.verifiedBalance(instance, token, walletAddress);

        Amount amount = Amount.of(balance);

        BigDecimal bigDecimal = amount.toDecimal(Unit.NAC);
        return bigDecimal;
    }


    public static BigInteger balance(long instance, long token, String walletAddress) throws Exception {
        BigInteger balance = BigInteger.ZERO;


        balance = balance.add(verifiedBalance(instance, token, walletAddress));


        UnverifiedUnusedPoolDAO unverifyUnusedPoolDAO = new UnverifiedUnusedPoolDAO(instance);
        UnverifiedUnusedPool unverifyUnusedPool = unverifyUnusedPoolDAO.find(walletAddress);
        if (unverifyUnusedPool != null) {

            UnverifiedTxDAO unverifiedTxDAO = new UnverifiedTxDAO(instance);
            BigInteger sumBI = unverifiedTxDAO.sumValue(token, unverifyUnusedPool.getUnusedTxs());

            balance = balance.add(sumBI);
            log.debug("unusedPool sum value:" + sumBI + ", balance:" + balance + ", unverifyUnusedPool.getUnusedTxs():" + unverifyUnusedPool.getUnusedTxs() + ", walletAddress:" + walletAddress);
        }

        return balance;
    }


    public static BigInteger verifiedBalance(long instance, long token, String walletAddress) throws Exception {
        BigInteger balance = BigInteger.ZERO;


        UnusedPoolDAO unusedPoolDAO = new UnusedPoolDAO(instance);
        UnusedPool unusedPool = unusedPoolDAO.find(walletAddress);
        if (unusedPool != null) {

            TxDAO txDAO = new TxDAO(instance);
            BigInteger sumBI = txDAO.sumValue(token, unusedPool.getUnusedTxs());

            balance = balance.add(sumBI);
            log.debug("unusedPool sum value:" + sumBI + ", balance:" + balance + ", walletAddress:" + walletAddress);
        }

        return balance;
    }


    public static Feedback checkGasBalanceEnough(TxBase txBase) throws Exception {

        BigInteger balanceAmount = TxBalanceService.balance(txBase.getInstance(), txBase.getGasType(), txBase.getFrom());


        if (balanceAmount.compareTo(txBase.getGas()) == -1) {

            return FeedbackService.newFeedback().asFail().setMessage(String.format("Gas not enough. %s Balance=%d, Tx Gas=%d.", txBase.getFrom(), balanceAmount, txBase.getGas()));
        }

        return FeedbackService.newFeedback().asSucceed();
    }

    public static void main(String[] args) throws Exception {

        BigInteger nacBalAmount = TxBalanceService.balance(4, 1, TxReservedWord.INSTANCE.name);
        System.out.println("nacBalAmount:" + nacBalAmount + ", Token:" + Amount.of(nacBalAmount).toDecimal(Unit.NAC));

        BigInteger nomcBalAmount = TxBalanceService.balance(4, 2, TxReservedWord.INSTANCE.name);
        System.out.println("nomcBalAmount:" + nomcBalAmount + ", Token:" + Amount.of(nomcBalAmount).toDecimal(Unit.NAC));


        FullNodeDAO fullNodeDAO = new FullNodeDAO();
        System.out.println(fullNodeDAO.count());

    }

}
