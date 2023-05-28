package org.nachain.core.chain;

import org.nachain.core.chain.transaction.TxEventStatus;


public class Feedback<T> {


    private int succeed;


    private String message;


    private T data;

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
        this.succeed = TxEventStatus.SUCCEED.value;
        return this;
    }


    public Feedback<T> asFail() {
        this.succeed = TxEventStatus.FAILED.value;
        return this;
    }


    public boolean isSucceed() {
        return succeed == TxEventStatus.SUCCEED.value;
    }


    public boolean isFailed() {
        return succeed == TxEventStatus.FAILED.value;
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
