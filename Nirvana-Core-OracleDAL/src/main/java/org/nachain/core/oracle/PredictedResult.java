package org.nachain.core.oracle;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.chain.sign.IMinedSignObject;
import org.nachain.core.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;


public class PredictedResult implements IPredictedResult, IMinedSignObject {


    private long instance;


    private long token;


    private int eventType;


    private String resultData;


    private List<String> predictedIDs = new ArrayList<>();


    private long blockHeight;


    private String minedSign;

    public PredictedResult() {

        predictedIDs.addAll(initPredictions());
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public PredictedResult setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public long getInstance() {
        return instance;
    }

    public PredictedResult setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getToken() {
        return token;
    }

    public PredictedResult setToken(long token) {
        this.token = token;
        return this;
    }

    public int getEventType() {
        return eventType;
    }

    public PredictedResult setEventType(int eventType) {
        this.eventType = eventType;
        return this;
    }

    public String getResultData() {
        return resultData;
    }

    public PredictedResult setResultData(String resultData) {
        this.resultData = resultData;
        return this;
    }

    public List<String> getPredictedIDs() {
        return predictedIDs;
    }

    public PredictedResult setPredictedIDs(List<String> predictedIDs) {
        this.predictedIDs = predictedIDs;
        return this;
    }

    public String getMinedSign() {
        return minedSign;
    }

    public PredictedResult setMinedSign(String minedSign) {
        this.minedSign = minedSign;
        return this;
    }


    @Override
    public boolean addPrediction(String pHash) {
        return predictedIDs.add(pHash);
    }

    @Override
    public String toMinedSignString() throws Exception {
        return "PredictedResult{" +
                "instance=" + instance +
                ", token=" + token +
                ", eventType=" + eventType +
                ", resultData='" + resultData + '\'' +
                ", predictedIDs=" + predictedIDs +
                ", blockHeight=" + blockHeight +
                '}';
    }

    @Override
    public String toString() {
        return "PredictedResult{" +
                "instance=" + instance +
                ", token=" + token +
                ", eventType=" + eventType +
                ", resultData='" + resultData + '\'' +
                ", predictedIDs=" + predictedIDs +
                ", blockHeight=" + blockHeight +
                ", minedSign='" + minedSign + '\'' +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }


    @Override
    public List<String> initPredictions() {
        return new ArrayList<>();
    }


}
