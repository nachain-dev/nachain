package org.nachain.libs.result;

import org.nachain.libs.validate.result.VldResult;

public class ResultDTO<T> {

    private VldResult vldResult;

    private int State;

    private T data;

    public ResultDTO() {
    }

    public ResultDTO(boolean buildVldResult) {
        if (buildVldResult) {
            this.vldResult = new VldResult();
        }
    }

    public ResultDTO(VldResult vldResult) {
        this.vldResult = vldResult;
    }

    public VldResult getVldResult() {
        return vldResult;
    }

    public void setVldResult(VldResult vldResult) {
        this.vldResult = vldResult;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}