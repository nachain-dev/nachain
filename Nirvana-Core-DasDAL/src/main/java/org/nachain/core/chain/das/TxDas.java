package org.nachain.core.chain.das;

import org.nachain.core.chain.transaction.TxBase;
import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;


public class TxDas extends TxBase {


    private long schedule;


    private long minedIndex;


    private String minedAddress;

    public long getSchedule() {
        return schedule;
    }

    public TxDas setSchedule(long schedule) {
        this.schedule = schedule;
        return this;
    }

    public long getMinedIndex() {
        return minedIndex;
    }

    public TxDas setMinedIndex(long minedIndex) {
        this.minedIndex = minedIndex;
        return this;
    }

    public String getMinedAddress() {
        return minedAddress;
    }

    public TxDas setMinedAddress(String minedAddress) {
        this.minedAddress = minedAddress;
        return this;
    }


    @Override
    public String toHashString() {
        return "TxDas{" +
                "schedule=" + schedule +
                ", instance=" + instance +
                ", version=" + version +
                ", timestamp=" + timestamp +
                ", token=" + token +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", value=" + value +
                ", gas=" + gas +
                ", gasType=" + gasType +
                ", gasLimit=" + gasLimit +
                ", txHeight=" + txHeight +
                ", txType=" + txType +
                ", context='" + context + '\'' +
                ", remarks='" + remarks + '\'' +
                ", blockCondition=" + blockCondition +
                '}';
    }


    @Override
    public String toSenderSignString() {
        return "TxDas{" +
                "schedule=" + schedule +
                ", instance=" + instance +
                ", version=" + version +
                ", timestamp=" + timestamp +
                ", token=" + token +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", value=" + value +
                ", gas=" + gas +
                ", gasType=" + gasType +
                ", gasLimit=" + gasLimit +
                ", txHeight=" + txHeight +
                ", txType=" + txType +
                ", context='" + context + '\'' +
                ", remarks='" + remarks + '\'' +
                ", blockCondition=" + blockCondition +
                ", hash='" + hash + '\'' +
                '}';
    }

    @Override
    public String toMinedSignString() {
        return "TxDas{" +
                "schedule=" + schedule +
                ", minedIndex=" + minedIndex +
                ", minedAddress='" + minedAddress + '\'' +
                ", instance=" + instance +
                ", version=" + version +
                ", timestamp=" + timestamp +
                ", token=" + token +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", value=" + value +
                ", gas=" + gas +
                ", gasType=" + gasType +
                ", gasLimit=" + gasLimit +
                ", gasInputData=" + gasInputData +
                ", txHeight=" + txHeight +
                ", txType=" + txType +
                ", context='" + context + '\'' +
                ", remarks='" + remarks + '\'' +
                ", blockCondition=" + blockCondition +
                ", hash='" + hash + '\'' +
                ", senderSign='" + senderSign + '\'' +
                ", status=" + status +
                ", eventStatus=" + eventStatus +
                ", bleedValue=" + bleedValue +
                '}';
    }

    @Override
    public String toString() {
        return "TxDas{" +
                "schedule=" + schedule +
                ", minedIndex=" + minedIndex +
                ", minedAddress='" + minedAddress + '\'' +
                ", instance=" + instance +
                ", version=" + version +
                ", timestamp=" + timestamp +
                ", token=" + token +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", value=" + value +
                ", gas=" + gas +
                ", gasType=" + gasType +
                ", gasLimit=" + gasLimit +
                ", gasInputData=" + gasInputData +
                ", txHeight=" + txHeight +
                ", txType=" + txType +
                ", context='" + context + '\'' +
                ", remarks='" + remarks + '\'' +
                ", blockCondition=" + blockCondition +
                ", hash='" + hash + '\'' +
                ", senderSign='" + senderSign + '\'' +
                ", status=" + status +
                ", eventStatus=" + eventStatus +
                ", bleedValue=" + bleedValue +
                ", minedSign='" + minedSign + '\'' +
                '}';
    }


    public static TxDas toTxDas(String json) {
        return JsonUtils.jsonToPojo(json, TxDas.class);
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }


    @Override
    public byte[] encodeHash() {

        return Hash.h256(this.toHashString().getBytes());
    }


    @Override
    public String encodeHashString() {
        return Hex.encode0x(encodeHash());
    }

}
