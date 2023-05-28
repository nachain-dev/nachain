package org.nachain.core.chain.structure.instance.datachain;

import org.nachain.core.base.Amount;
import org.nachain.core.chain.structure.IChain;
import org.nachain.core.consensus.Consensus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DataChain implements IChain {


    private static final Map<String, Amount> accountBalances = new ConcurrentHashMap<>();


    private Amount usdnAmount;


    private void addUSDN(String fromAddress, Amount usdnAmount) {
        synchronized (accountBalances) {

            Amount balance = accountBalances.getOrDefault(fromAddress, Amount.ZERO);
            balance.add(usdnAmount);


            accountBalances.put(fromAddress, balance);
        }
    }


    private boolean removeUSDN(String fromAddress, Amount usdnAmount) {
        boolean rtv = false;
        synchronized (accountBalances) {

            Amount balance = accountBalances.getOrDefault(fromAddress, Amount.ZERO);

            if (balance.greaterThanOrEqual(usdnAmount)) {
                balance = balance.subtract(usdnAmount);

                accountBalances.put(fromAddress, balance);
                rtv = true;
            }
        }

        return rtv;
    }


    @Override
    public void setConsensus(Consensus consensus) {
    }

}
