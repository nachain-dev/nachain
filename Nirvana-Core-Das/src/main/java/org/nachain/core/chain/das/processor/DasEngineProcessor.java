package org.nachain.core.chain.das.processor;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.Feedback;
import org.nachain.core.chain.FeedbackService;
import org.nachain.core.chain.das.TxDas;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxContextService;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.crypto.Key;

import java.lang.reflect.Constructor;


@Slf4j
public class DasEngineProcessor {


    long instance;


    public DasEngineProcessor(long instance) {
        this.instance = instance;
    }


    public Feedback processTxEvent(TxDas txDas, Key minerKey, boolean isPlayback) {
        Feedback feedback = null;


        TxContext txContext = TxContextService.toTxContext(txDas.getContext());
        TxEventType eventType = txContext.getEventType();


        Class clazz = TxDasProcessor.class;
        String methodName = eventType.name;
        Object processor = null;
        try {

            Constructor constructor = clazz.getDeclaredConstructor(int.class, boolean.class);
            processor = constructor.newInstance(txDas.getInstance(), isPlayback);

            feedback = (Feedback) clazz.getDeclaredMethod("doBefore", TxDas.class).invoke(processor, txDas);
            log.debug("processTxEvent(TxDas txDas, TxEventType eventType).doBefore->{}", feedback);
            if (feedback.isSucceed()) {

                feedback = (Feedback) clazz.getDeclaredMethod(methodName, TxDas.class).invoke(processor, txDas);
                log.debug("processTxEvent(TxDas txDas, TxEventType eventType).{}->{}", methodName, feedback);
            }
        } catch (Exception e) {
            log.error("TxDas:" + txDas.toJson(), "EventType:" + eventType, e);
            feedback = FeedbackService.newFeedback().asFail().setMessage("TxDas " + eventType + " fail. Runtime exception.");
        } finally {

            try {

                feedback = (Feedback) clazz.getDeclaredMethod("doFinally", Feedback.class, TxDas.class, Key.class).invoke(processor, feedback, txDas, minerKey);
                log.debug("processTxEvent(TxDas txDas, TxEventType eventType).doFinally->{}", feedback);
            } catch (Exception e) {
                log.error("TxDas:" + txDas.toJson(), "doFinally()", e);
            }
        }

        return feedback;
    }

}
