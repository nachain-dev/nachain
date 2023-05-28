package org.nachain.core.intermediate.feedback;


public class Feedback<T> {


    private int succeed;


    private String message;


    private T data;

    public Feedback() {
    }

    public Feedback(boolean succeed) {
        this.succeed = succeed ? FeedbackStatus.SUCCEED.value : FeedbackStatus.FAILED.value;
    }

    public String getMessage() {
        return message;
    }

    public Feedback<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Feedback<T> setData(T data) {
        this.data = data;
        return this;
    }


    public Feedback<T> asSucceed() {
        this.succeed = FeedbackStatus.SUCCEED.value;
        return this;
    }


    public Feedback<T> asFail() {
        this.succeed = FeedbackStatus.FAILED.value;
        return this;
    }


    public boolean isSucceed() {
        return succeed == FeedbackStatus.SUCCEED.value;
    }


    public boolean isFailed() {
        return succeed == FeedbackStatus.FAILED.value;
    }


    @Override
    public String toString() {
        return "Feedback{" +
                "succeed=" + succeed +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
