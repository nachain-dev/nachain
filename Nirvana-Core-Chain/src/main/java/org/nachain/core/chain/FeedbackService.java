package org.nachain.core.chain;

import org.nachain.core.chain.transaction.TxReservedWord;

public class FeedbackService {


    public static Feedback newFeedback() {
        return new Feedback().asSucceed().setMessage("").setData("");
    }


    public static Feedback newFailFeedback(String message) {

        return FeedbackService.newFeedback().asFail().setMessage(message);
    }


    public static Feedback newFeedbackByRequiresInstance(String eventName) {

        return FeedbackService.newFailFeedback(String.format("%s fail,Tx.getTo() requires the \"%s\"", eventName, TxReservedWord.INSTANCE));
    }


    public static Feedback newFeedbackByRequiresToken(String eventName, String tokenName) {

        return FeedbackService.newFailFeedback(String.format("%s fail,Tx.getToken() requires the \"%s\"", eventName, tokenName));
    }


    public static Feedback newFeedbackByPermissionDenied(String eventName) {

        return FeedbackService.newFailFeedback(String.format("%s fail,Permission Denied", eventName));
    }


}
