package org.nachain.core.intermediate;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.das.TxDas;
import org.rocksdb.RocksDBException;

import java.io.IOException;


@Slf4j
public class AccountTxDasHeightService {


    public static AccountTxDasHeight newAccountTxDasHeight(TxDas tx) {
        AccountTxDasHeight accountTxDasHeight = new AccountTxDasHeight();
        accountTxDasHeight.setWallet(tx.getFrom());
        accountTxDasHeight.setInstance(tx.getInstance());
        accountTxDasHeight.setToken(tx.getToken());
        accountTxDasHeight.setHeight(tx.getTxHeight());

        return accountTxDasHeight;
    }


    public static void updateHeight(TxDas tx) throws RocksDBException, IOException {
        AccountTxDasHeightDAO accountTxDasHeightDAO = new AccountTxDasHeightDAO(tx.getInstance());

        String keyName = accountTxDasHeightDAO.toKeyName(tx.getFrom(), tx.getInstance(), tx.getToken());
        AccountTxDasHeight accountTxDasHeight = accountTxDasHeightDAO.find(keyName);

        boolean state = false;

        if (accountTxDasHeight == null) {
            accountTxDasHeight = newAccountTxDasHeight(tx);
            state = accountTxDasHeightDAO.add(accountTxDasHeight);
            log.debug("add KeyName:" + keyName + ", AccountTxDasHeight:" + accountTxDasHeight + ",state=" + state + ", From:" + tx.getFrom());
        } else {
            accountTxDasHeight.setHeight(tx.getTxHeight());
            state = accountTxDasHeightDAO.edit(accountTxDasHeight);
            log.debug("edit KeyName:" + keyName + ", AccountTxDasHeight:" + accountTxDasHeight + ",state=" + state + ", From:" + tx.getFrom());
        }


    }


    public static long getTxHeight(String wallet, long instance, long token) throws RocksDBException, IOException {
        AccountTxDasHeightDAO accountTxDasHeightDAO = new AccountTxDasHeightDAO(instance);
        AccountTxDasHeight accountTxDasHeight = accountTxDasHeightDAO.find(accountTxDasHeightDAO.toKeyName(wallet, instance, token));
        if (accountTxDasHeight != null) {
            return accountTxDasHeight.getHeight();
        } else {
            return 0;
        }
    }


    public static long nextTxHeight(String wallet, long instance, long token) throws RocksDBException, IOException {
        return getTxHeight(wallet, instance, token) + 1;
    }


}
