package org.nachain.core.chain.transaction.context;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.util.JsonUtils;

import java.util.List;


public class TxContext<T> {


    private InstanceType instanceType;


    private TxEventType eventType;


    private long referrerInstance;


    private String referrerTx;


    private long crossToInstance;


    private T data;


    private TxMark txMark;


    private List<String> attachments;

    public InstanceType getInstanceType() {
        return instanceType;
    }

    public TxContext<T> setInstanceType(InstanceType instanceType) {
        this.instanceType = instanceType;
        return this;
    }

    public TxEventType getEventType() {
        return eventType;
    }

    public TxContext<T> setEventType(TxEventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public long getReferrerInstance() {
        return referrerInstance;
    }

    public TxContext<T> setReferrerInstance(long referrerInstance) {
        this.referrerInstance = referrerInstance;
        return this;
    }

    public String getReferrerTx() {
        return referrerTx;
    }

    public TxContext<T> setReferrerTx(String referrerTx) {
        this.referrerTx = referrerTx;
        return this;
    }

    public long getCrossToInstance() {
        return crossToInstance;
    }

    public TxContext<T> setCrossToInstance(long crossToInstance) {
        this.crossToInstance = crossToInstance;
        return this;
    }

    public T getData() {
        return data;
    }

    public TxContext<T> setData(T data) {
        this.data = data;
        return this;
    }

    public TxMark getTxMark() {
        return txMark;
    }

    public TxContext<T> setTxMark(TxMark txMark) {
        this.txMark = txMark;
        return this;
    }

    @Override
    public String toString() {
        return "TxContext{" +
                "instanceType=" + instanceType +
                ", eventType=" + eventType +
                ", referrerInstance=" + referrerInstance +
                ", referrerTx='" + referrerTx + '\'' +
                ", crossToInstance=" + crossToInstance +
                ", data=" + data +
                ", txMark=" + txMark +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }
}
