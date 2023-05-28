package org.nachain.core.intermediate;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;

public class AccountTxDasHeightDAO extends RocksDAO {


    public AccountTxDasHeightDAO(long instance) throws RocksDBException, IOException {
        super("AccountTxDasHeight", instance);
    }


    public boolean add(AccountTxDasHeight accountTxHeight) throws RocksDBException {
        String keyName = toKeyName(accountTxHeight);
        String result = db.get(keyName);

        if (result != null) {
            return false;
        }
        put(keyName, accountTxHeight);
        return true;
    }


    public boolean edit(AccountTxDasHeight accountTxDasHeight) throws RocksDBException {
        String keyName = toKeyName(accountTxDasHeight);
        String result = db.get(keyName);

        if (result == null) {
            return false;
        }
        put(keyName, accountTxDasHeight);
        return true;
    }


    public AccountTxDasHeight find(String keyName) throws RocksDBException {
        String result = db.get(keyName);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, AccountTxDasHeight.class);
    }


    public long count() {
        return db.count();
    }


    public String toKeyName(String wallet, long instance, long token) {
        return wallet + "_" + instance + "_" + token;
    }


    public String toKeyName(AccountTxDasHeight accountTxDasHeight) {
        return toKeyName(accountTxDasHeight.getWallet(), accountTxDasHeight.getInstance(), accountTxDasHeight.getToken());
    }


}
