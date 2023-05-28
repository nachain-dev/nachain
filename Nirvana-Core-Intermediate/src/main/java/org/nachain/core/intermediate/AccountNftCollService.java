package org.nachain.core.intermediate;

import com.google.common.collect.Lists;
import org.nachain.core.token.TokenSingleton;
import org.nachain.core.token.nft.NftItem;
import org.nachain.core.token.nft.NftItemDAO;
import org.rocksdb.RocksDBException;

import java.util.HashSet;
import java.util.List;

public class AccountNftCollService {


    public static AccountNftColl newAccountNftColl(String address) {
        AccountNftColl accountNftColl = new AccountNftColl();
        accountNftColl.setAddress(address);
        accountNftColl.setCollTokenIds(new HashSet<>());

        return accountNftColl;
    }


    public static void addToken(String address, long tokenId) throws RocksDBException {
        AccountNftCollDAO accountNftCollDAO = new AccountNftCollDAO();
        AccountNftColl accountNftColl = accountNftCollDAO.get(address);
        if (accountNftColl == null) {
            accountNftColl = newAccountNftColl(address);
        }

        boolean hasNew = accountNftColl.addCollTokenId(tokenId);
        if (hasNew) {

            accountNftCollDAO.put(accountNftColl);
        }
    }


    public static HashSet<Long> getCollTokenIds(String address) throws RocksDBException {
        AccountNftCollDAO accountNftCollDAO = new AccountNftCollDAO();
        AccountNftColl accountNftColl = accountNftCollDAO.get(address);

        return accountNftColl.getCollTokenIds();
    }


    public static List<NftItem> getNftItems(String address, long collTokenId) throws RocksDBException {
        List<NftItem> nftItems = Lists.newArrayList();


        long instanceId = TokenSingleton.get().get(collTokenId).getInstanceId();
        AccountNftItemDAO accountNftItemDAO = new AccountNftItemDAO(instanceId);
        AccountNftItem accountNftItem = accountNftItemDAO.get(address, collTokenId);
        NftItemDAO nftItemDAO = new NftItemDAO(instanceId);

        for (long nftItemId : accountNftItem.getNftItemIds()) {
            NftItem nftItem = nftItemDAO.get(nftItemId);
            nftItems.add(nftItem);
        }

        return nftItems;
    }

}
