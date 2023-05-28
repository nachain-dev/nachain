package org.nachain.core.intermediate;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

public class AccountDeployDAO extends RocksDAO {

    public AccountDeployDAO() {
        super("AccountDeploy", CoreInstanceEnum.APPCHAIN.id);
    }


    public boolean put(AccountDeploy accountDeploy) throws RocksDBException {
        put(accountDeploy.getAddress(), accountDeploy);
        return true;
    }


    public AccountDeploy get(String address) throws RocksDBException {
        String result = db.get(address);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, AccountDeploy.class);
    }


    public long count() {
        return db.count();
    }


}
