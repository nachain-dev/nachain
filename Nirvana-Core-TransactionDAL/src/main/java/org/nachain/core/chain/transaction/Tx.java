package org.nachain.core.chain.transaction;

import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;


public class Tx extends TxBase {


    private TxInputData inputData;


    private TxOutput output;


    private String changeTx;


    private long blockHeight;

    public TxInputData getInputData() {
        return inputData;
    }

    public Tx setInputData(TxInputData inputData) {
        this.inputData = inputData;
        return this;
    }

    public TxOutput getOutput() {
        return output;
    }

    public Tx setOutput(TxOutput output) {
        this.output = output;
        return this;
    }

    public String getChangeTx() {
        return changeTx;
    }

    public Tx setChangeTx(String changeTx) {
        this.changeTx = changeTx;
        return this;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public Tx setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }


    @Override
    public String toHashString() {
        return "Tx{" +
                "output=" + output +
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
        return "Tx{" +
                "output=" + output +
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
        return "Tx{" +
                "inputData=" + inputData +
                ", output=" + output +
                ", changeTx='" + changeTx + '\'' +
                ", blockHeight=" + blockHeight +
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
                ", eventInfo='" + eventInfo + '\'' +
                ", eventStates=" + eventStates +
                ", bleedValue=" + bleedValue +
                '}';
    }

    @Override
    public String toString() {
        return "Tx{" +
                "inputData=" + inputData +
                ", output=" + output +
                ", changeTx='" + changeTx + '\'' +
                ", blockHeight=" + blockHeight +
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
                ", eventInfo='" + eventInfo + '\'' +
                ", eventStates=" + eventStates +
                ", bleedValue=" + bleedValue +
                ", minedSign='" + minedSign + '\'' +
                '}';
    }


    public static Tx toTx(String json) {
        return JsonUtils.jsonToPojo(json, Tx.class);
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
