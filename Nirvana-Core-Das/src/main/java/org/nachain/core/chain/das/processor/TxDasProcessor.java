package org.nachain.core.chain.das.processor;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.base.Amount;
import org.nachain.core.base.Unit;
import org.nachain.core.chain.Feedback;
import org.nachain.core.chain.FeedbackService;
import org.nachain.core.chain.config.PricingSystem;
import org.nachain.core.chain.das.DasProfileService;
import org.nachain.core.chain.das.TxDas;
import org.nachain.core.chain.das.TxDasDAO;
import org.nachain.core.chain.das.TxDasService;
import org.nachain.core.chain.transaction.TxEventStateService;
import org.nachain.core.chain.transaction.TxEventStatus;
import org.nachain.core.crypto.Key;
import org.nachain.core.intermediate.feedback.StateFeedback;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;


@Slf4j
public class TxDasProcessor implements ITxDasProcessor {

    private TxDasDAO txDasDAO;


    private BigInteger gasMinAmount;


    private boolean isPlayback;


    public TxDasProcessor(long instance, boolean isPlayback) {
        try {
            txDasDAO = new TxDasDAO(instance);

            gasMinAmount = Amount.of(BigDecimal.valueOf(PricingSystem.Gas.GAS_DAS_USDN), Unit.NAC).toBigInteger();

            this.isPlayback = isPlayback;
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean isMining() {
        return !isPlayback;
    }


    @Override
    public Feedback doBefore(TxDas txDas) {


        if (isMining()) {
            BigInteger gasTotal = txDas.getGas();

            BigInteger bleedValue = gasTotal.subtract(gasMinAmount);
            if (bleedValue.compareTo(BigInteger.ZERO) == 1) {
                try {
                    txDas.setBleedValue(bleedValue);
                    txDasDAO.edit(txDas);
                } catch (RocksDBException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return FeedbackService.newFeedback();
    }


    @Override
    public Feedback doFinally(Feedback feedback, TxDas txDas, Key senderKey) {

        if (isMining()) {

            if (feedback.isFailed()) {
                TxDasDAO txDasDAO = null;
                try {
                    txDasDAO = new TxDasDAO(txDas.getInstance());
                    txDas.setEventStatus(TxEventStatus.FAILED.value);

                    txDasDAO.edit(txDas);
                } catch (Exception e) {
                    log.error("Tx:" + txDas, e);
                }


                try {

                    if (senderKey != null) {

                        TxDas eventRefundTx = TxDasService.newEventRefundTxDas(txDas, senderKey);
                        feedback.setData(eventRefundTx);
                    }
                } catch (Exception e) {
                    log.error("TRANSFER_EVENT_REFUND txHash:" + txDas.getHash(), e);
                }
            }
        }


        try {
            StateFeedback stateFeedback = DasProfileService.saveDasProfile(txDas.getInstance(), txDas, txDas.getTxHeight(), txDas.getTimestamp());
            if (stateFeedback != null) {

                TxEventStateService.addTxDasEventState(txDas, txDas.getFrom(), stateFeedback.getBeforeFrom(), stateFeedback.getAfterFrom());
                TxEventStateService.addTxDasEventState(txDas, txDas.getTo(), stateFeedback.getBeforeTo(), stateFeedback.getAfterTo());
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return feedback;
    }


    @Override
    public Feedback DAS_ACTION(TxDas txDas) {
        log.debug("TxDasProcessor Event:DAS_ACTION");
        return FeedbackService.newFeedback();
    }


    @Override
    public Feedback DAS_PREPAID(TxDas txDas) {
        log.debug("TxDasProcessor Event:DAS_PREPAID");
        return FeedbackService.newFeedback();
    }
}
