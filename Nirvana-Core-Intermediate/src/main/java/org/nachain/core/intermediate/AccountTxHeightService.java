package org.nachain.core.intermediate;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.transaction.Tx;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@Slf4j
public class AccountTxHeightService {


    public static AccountTxHeight newAccountTxHeight(Tx tx) {
        AccountTxHeight accountTxHeight = new AccountTxHeight();
        accountTxHeight.setWallet(tx.getFrom());
        accountTxHeight.setInstance(tx.getInstance());
        accountTxHeight.setToken(tx.getToken());
        accountTxHeight.setHeight(tx.getTxHeight());

        return accountTxHeight;
    }


    public static void updateHeight(Tx tx) {
        try {
            AccountTxHeightDAO accountTxHeightDAO = new AccountTxHeightDAO(tx.getInstance());

            String keyName = accountTxHeightDAO.toKey(tx.getFrom(), tx.getInstance(), tx.getToken());
            AccountTxHeight accountTxHeight = accountTxHeightDAO.find(keyName);

            boolean state;

            if (accountTxHeight == null) {
                accountTxHeight = newAccountTxHeight(tx);
                state = accountTxHeightDAO.add(accountTxHeight);
                log.debug("add KeyName:" + keyName + ", AccountTxHeight:" + accountTxHeight + ",state=" + state + ", From:" + tx.getFrom());
            } else {
                accountTxHeight.setHeight(tx.getTxHeight());
                state = accountTxHeightDAO.put(accountTxHeight);
                log.debug("edit KeyName:" + keyName + ", AccountTxHeight:" + accountTxHeight + ",state=" + state + ", From:" + tx.getFrom());
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public static long getTxHeight(String wallet, long instance, long token) throws RocksDBException, IOException, ExecutionException {
        AccountTxHeightDAO accountTxHeightDAO = new AccountTxHeightDAO(instance);
        AccountTxHeight accountTxHeight = accountTxHeightDAO.find(accountTxHeightDAO.toKey(wallet, instance, token));
        if (accountTxHeight != null) {
            return accountTxHeight.getHeight();
        } else {
            return 0;
        }
    }


    public static long nextTxHeight(String wallet, long instance, long token) throws RocksDBException, IOException, ExecutionException {
        return getTxHeight(wallet, instance, token) + 1;
    }


}
