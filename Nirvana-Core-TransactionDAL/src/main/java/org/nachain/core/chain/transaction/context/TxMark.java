package org.nachain.core.chain.transaction.context;


public class TxMark {


    String clientName;


    String osName;

    public String getClientName() {
        return clientName;
    }

    public TxMark setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public String getOsName() {
        return osName;
    }

    public TxMark setOsName(String osName) {
        this.osName = osName;
        return this;
    }

    @Override
    public String toString() {
        return "TxMark{" +
                "clientName='" + clientName + '\'' +
                ", osName='" + osName + '\'' +
                '}';
    }
}
