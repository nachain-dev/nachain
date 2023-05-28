package org.nachain.core.token.protocol;


public abstract class AbstractProtocol implements IProtocol {


    String protocolName;


    String protocolVersion;


    boolean isAllowDecimal;

    public AbstractProtocol() {
        init();
    }

    public String getProtocolName() {
        return protocolName;
    }

    protected AbstractProtocol setProtocolName(String protocolName) {
        this.protocolName = protocolName;
        return this;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    protected AbstractProtocol setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    public boolean isAllowDecimal() {
        return isAllowDecimal;
    }

    protected AbstractProtocol setAllowDecimal(boolean allowDecimal) {
        isAllowDecimal = allowDecimal;
        return this;
    }


    @Override
    public String toString() {
        return "AbstractProtocol{" +
                "protocolName='" + protocolName + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                ", isAllowDecimal=" + isAllowDecimal +
                '}';
    }
}
