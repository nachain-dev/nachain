package org.nachain.core.intermediate;

import com.google.common.collect.Lists;
import org.rocksdb.RocksDBException;

public class AccountDeployService {

    private static AccountDeployDAO accountDeployDAO;

    static {
        accountDeployDAO = new AccountDeployDAO();
    }


    public static AccountDeploy newAccountDeploy(String address) {
        AccountDeploy accountDeploy = new AccountDeploy();
        accountDeploy.setAddress(address);
        accountDeploy.setDeployInstances(Lists.newArrayList());
        return accountDeploy;
    }


    public static AccountDeploy newAccountDeploy(String address, long instance) {
        AccountDeploy accountDeploy = newAccountDeploy(address);
        accountDeploy.addDeploy(instance);
        return accountDeploy;
    }


    public static AccountDeploy getAccountDeploy(String address) throws RocksDBException {
        return accountDeployDAO.get(address);
    }


    public static AccountDeploy addDeployInstance(String address, long instance) throws RocksDBException {
        AccountDeploy accountDeploy = getAccountDeploy(address);
        if (accountDeploy == null) {
            accountDeploy = AccountDeployService.newAccountDeploy(address, instance);
        } else {
            accountDeploy.addDeploy(instance);
        }

        accountDeployDAO.put(accountDeploy);
        return accountDeploy;
    }


}
