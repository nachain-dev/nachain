package org.nachain.core.intermediate;

import lombok.Data;

import java.math.BigInteger;


@Data
public class AccountBalance {


    private long instance;


    private long token;


    private String address;


    private BigInteger balance;


    private BigInteger historyIn;


    private BigInteger historyOut;


    private BigInteger historyGasOut;


    private long blockHeight;


    private long timestamp;


    public void addBalance(BigInteger amount) {

        if (balance == null) {
            balance = BigInteger.ZERO;
        }
        balance = balance.add(amount);


        if (historyIn == null) {
            historyIn = BigInteger.ZERO;
        }
        historyIn = historyIn.add(amount);
    }


    public void subBalance(BigInteger amount) {

        if (balance == null) {
            balance = BigInteger.ZERO;
        }
        balance = balance.subtract(amount);


        if (historyOut == null) {
            historyOut = BigInteger.ZERO;
        }
        historyOut = historyOut.add(amount);
    }


    public void addGas(BigInteger amount) {

        if (historyGasOut == null) {
            historyGasOut = BigInteger.ZERO;
        }
        historyGasOut = historyGasOut.add(amount);
    }
}