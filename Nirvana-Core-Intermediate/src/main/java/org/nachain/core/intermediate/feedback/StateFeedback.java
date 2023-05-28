package org.nachain.core.intermediate.feedback;

import java.math.BigInteger;


public class StateFeedback {

    private BigInteger beforeFrom = BigInteger.ZERO;
    private BigInteger afterFrom = BigInteger.ZERO;


    private BigInteger beforeTo = BigInteger.ZERO;
    private BigInteger afterTo = BigInteger.ZERO;

    public StateFeedback() {
    }

    public StateFeedback(BigInteger beforeFrom, BigInteger afterFrom, BigInteger beforeTo, BigInteger afterTo) {
        this.beforeFrom = beforeFrom;
        this.afterFrom = afterFrom;
        this.beforeTo = beforeTo;
        this.afterTo = afterTo;
    }

    public BigInteger getBeforeFrom() {
        return beforeFrom;
    }

    public StateFeedback setBeforeFrom(BigInteger beforeFrom) {
        this.beforeFrom = beforeFrom;
        return this;
    }

    public BigInteger getAfterFrom() {
        return afterFrom;
    }

    public StateFeedback setAfterFrom(BigInteger afterFrom) {
        this.afterFrom = afterFrom;
        return this;
    }

    public BigInteger getBeforeTo() {
        return beforeTo;
    }

    public StateFeedback setBeforeTo(BigInteger beforeTo) {
        this.beforeTo = beforeTo;
        return this;
    }

    public BigInteger getAfterTo() {
        return afterTo;
    }

    public StateFeedback setAfterTo(BigInteger afterTo) {
        this.afterTo = afterTo;
        return this;
    }

    @Override
    public String toString() {
        return "StateFeedback{" +
                "beforeFrom=" + beforeFrom +
                ", afterFrom=" + afterFrom +
                ", beforeTo=" + beforeTo +
                ", afterTo=" + afterTo +
                '}';
    }
}