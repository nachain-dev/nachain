package org.nachain.core.subscribe.transaction;

import java.util.List;


public class TxLog {


    String txHash;


    long blockHeight;


    String eventData;


    TxLogState txLogState;


    String message;


    String walletAddress;


    List<String> redoLog;


    List<String> undoLog;

    public String getTxHash() {
        return txHash;
    }

    public TxLog setTxHash(String txHash) {
        this.txHash = txHash;
        return this;
    }

    public String getEventData() {
        return eventData;
    }

    public TxLog setEventData(String eventData) {
        this.eventData = eventData;
        return this;
    }

    public TxLogState getTxLogState() {
        return txLogState;
    }

    public TxLog setTxLogState(TxLogState txLogState) {
        this.txLogState = txLogState;
        return this;
    }

    public List<String> getRedoLog() {
        return redoLog;
    }

    public TxLog setRedoLog(List<String> redoLog) {
        this.redoLog = redoLog;
        return this;
    }

    public List<String> getUndoLog() {
        return undoLog;
    }

    public TxLog setUndoLog(List<String> undoLog) {
        this.undoLog = undoLog;
        return this;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public TxLog setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    @Override
    public String toString() {
        return "TxLog{" +
                "txHash='" + txHash + '\'' +
                ", blockHeight=" + blockHeight +
                ", eventData='" + eventData + '\'' +
                ", txState=" + txLogState +
                ", message='" + message + '\'' +
                ", walletAddress='" + walletAddress + '\'' +
                ", redoLog=" + redoLog +
                ", undoLog=" + undoLog +
                '}';
    }
}
