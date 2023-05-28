package org.nachain.core.chain.block;


import org.nachain.core.chain.sign.IMinedSignObject;

import java.util.ArrayList;
import java.util.List;


public class BlockBody implements IBlockBody, IMinedSignObject {


    private long height;


    private List<String> transactions = new ArrayList<>();


    private String minedSign;

    public BlockBody(long height) {

        transactions.addAll(initTxs());

        this.height = height;
    }

    public BlockBody() {
    }


    @Override
    public boolean addTransaction(String txHash) {
        return transactions.add(txHash);
    }

    public long getHeight() {
        return height;
    }

    public BlockBody setHeight(long height) {
        this.height = height;
        return this;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public BlockBody setTransactions(List<String> transactions) {
        this.transactions = transactions;
        return this;
    }


    public String getMinedSign() {
        return minedSign;
    }

    public BlockBody setMinedSign(String minedSign) {
        this.minedSign = minedSign;
        return this;
    }

    @Override
    public String toString() {
        return "BlockBody{" +
                "height=" + height +
                ", transactions=" + transactions +
                ", minedSign='" + minedSign + '\'' +
                '}';
    }


    @Override
    public String toMinedSignString() throws Exception {
        return "BlockBody{" +
                "height=" + height +
                ", transactions=" + transactions +
                '}';
    }


    public List<String> initTxs() {
        return new ArrayList<>();
    }

}
