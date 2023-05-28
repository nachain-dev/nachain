package org.nachain.core.chain.transaction;

import com.google.common.collect.Lists;
import org.nachain.core.chain.sign.IMinedSignObject;

import java.math.BigInteger;
import java.util.List;


public abstract class TxBase implements ITx, IMinedSignObject {


    protected long instance;


    protected int version;


    protected long timestamp;


    protected long token;


    protected String from;


    protected String to;


    protected BigInteger value;


    protected BigInteger gas;


    protected long gasType;


    protected long gasLimit;


    protected TxInputData gasInputData;


    protected long txHeight;


    protected int txType;


    protected String context;


    protected String remarks;


    protected long blockCondition;


    protected String hash;


    protected String senderSign;


    protected int status;


    protected int eventStatus;


    protected String eventInfo;


    protected List<TxEventState> eventStates;


    protected BigInteger bleedValue;


    protected String minedSign;

    public long getInstance() {
        return instance;
    }

    public void setInstance(long instance) {
        this.instance = instance;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public BigInteger getGas() {
        return gas;
    }

    public void setGas(BigInteger gas) {
        this.gas = gas;
    }

    public long getGasType() {
        return gasType;
    }

    public void setGasType(long gasType) {
        this.gasType = gasType;
    }

    public TxInputData getGasInputData() {
        return gasInputData;
    }

    public TxBase setGasInputData(TxInputData gasInputData) {
        this.gasInputData = gasInputData;
        return this;
    }

    public long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(long gasLimit) {
        this.gasLimit = gasLimit;
    }

    public long getTxHeight() {
        return txHeight;
    }

    public void setTxHeight(long txHeight) {
        this.txHeight = txHeight;
    }

    public int getTxType() {
        return txType;
    }

    public void setTxType(int txType) {
        this.txType = txType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getBlockCondition() {
        return blockCondition;
    }

    public void setBlockCondition(long blockCondition) {
        this.blockCondition = blockCondition;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSenderSign() {
        return senderSign;
    }

    public void setSenderSign(String senderSign) {
        this.senderSign = senderSign;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(int eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public TxBase setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
        return this;
    }

    public List<TxEventState> getEventStates() {
        if (eventStates == null) {
            eventStates = Lists.newArrayList();
        }
        return eventStates;
    }

    public TxBase setEventStates(List<TxEventState> eventStates) {
        this.eventStates = eventStates;
        return this;
    }

    public BigInteger getBleedValue() {
        return bleedValue;
    }

    public void setBleedValue(BigInteger bleedValue) {
        this.bleedValue = bleedValue;
    }

    @Override
    public String getMinedSign() {
        return minedSign;
    }

    public void setMinedSign(String minedSign) {
        this.minedSign = minedSign;
    }

}
