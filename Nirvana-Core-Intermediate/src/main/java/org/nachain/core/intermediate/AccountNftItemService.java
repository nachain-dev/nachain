package org.nachain.core.intermediate;

import org.rocksdb.RocksDBException;

import java.util.HashSet;

public class AccountNftItemService {


    public static AccountNftItem newAccountNftItem(String address, long instance, long token) {
        AccountNftItem accountNft = new AccountNftItem();
        accountNft.setAddress(address);
        accountNft.setAddress(address);
        accountNft.setInstance(instance);
        accountNft.setCollTokenId(token);
        accountNft.setNftItemIds(new HashSet<>());

        return accountNft;
    }


    public static void addNftItemId(String address, long instance, long token, long nftItemId) throws RocksDBException {
        AccountNftItemDAO accountNftItemDAO = new AccountNftItemDAO(instance);
        AccountNftItem accountNftItem = accountNftItemDAO.get(address, token);
        if (accountNftItem == null) {
            accountNftItem = newAccountNftItem(address, instance, token);
        }

        boolean hasNew = accountNftItem.addNftItemId(nftItemId);
        if (hasNew) {

            accountNftItemDAO.put(accountNftItem);
        }
    }


    public static void delNftItemId(String address, long instance, long token, long nftItemId) throws RocksDBException {
        AccountNftItemDAO accountNftItemDAO = new AccountNftItemDAO(instance);
        AccountNftItem accountNftItem = accountNftItemDAO.get(address, token);
        if (accountNftItem != null) {

            boolean hasDel = accountNftItem.delNftItemId(nftItemId);
            if (hasDel) {

                accountNftItemDAO.put(accountNftItem);
            }
        }


    }

}
