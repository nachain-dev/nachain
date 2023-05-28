package org.nachain.core.intermediate.feedback;

public class FeedbackService {


    public static Feedback newFeedback() {
        return new Feedback().asSucceed().setMessage("").setData("");
    }


    public static Feedback newFeedback(boolean succeed) {
        return new Feedback(succeed).setMessage("").setData("");
    }


    public static Feedback newFailFeedback(String message) {

        return FeedbackService.newFeedback().asFail().setMessage(message);
    }


}
