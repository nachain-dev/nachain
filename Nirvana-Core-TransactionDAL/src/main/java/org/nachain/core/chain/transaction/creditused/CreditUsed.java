package org.nachain.core.chain.transaction.creditused;

import java.math.BigInteger;


public class CreditUsed {


    private String creditSourceTx;

    private long fromInstance;

    private long toInstance;

    private long token;

    private String to;


    private BigInteger tokenValue;

    public String getCreditSourceTx() {
        return creditSourceTx;
    }

    public CreditUsed setCreditSourceTx(String creditSourceTx) {
        this.creditSourceTx = creditSourceTx;
        return this;
    }

    public long getFromInstance() {
        return fromInstance;
    }

    public CreditUsed setFromInstance(long fromInstance) {
        this.fromInstance = fromInstance;
        return this;
    }

    public long getToInstance() {
        return toInstance;
    }

    public CreditUsed setToInstance(long toInstance) {
        this.toInstance = toInstance;
        return this;
    }

    public long getToken() {
        return token;
    }

    public CreditUsed setToken(long token) {
        this.token = token;
        return this;
    }

    public String getTo() {
        return to;
    }

    public CreditUsed setTo(String to) {
        this.to = to;
        return this;
    }

    public BigInteger getTokenValue() {
        return tokenValue;
    }

    public CreditUsed setTokenValue(BigInteger tokenValue) {
        this.tokenValue = tokenValue;
        return this;
    }

    @Override
    public String toString() {
        return "CreditUsed{" +
                "creditSourceTx='" + creditSourceTx + '\'' +
                ", fromInstance=" + fromInstance +
                ", toInstance=" + toInstance +
                ", token=" + token +
                ", to='" + to + '\'' +
                ", tokenValue=" + tokenValue +
                '}';
    }


}
