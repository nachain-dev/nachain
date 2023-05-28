package org.nachain.core.intermediate;

import com.google.common.collect.Lists;
import org.nachain.core.chain.transaction.Tx;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class AccountDetailsService {

    private static AccountDetailsDAO accountDetailsDAO;

    static {
        try {
            accountDetailsDAO = new AccountDetailsDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static AccountDetails newAccountDetails(String address) {
        AccountDetails account = new AccountDetails();
        account.setAddress(address);
        account.setUsedInstances(new ConcurrentHashMap<>());

        return account;
    }


    public static AccountDetails getAccountDetails(String address) throws ExecutionException {
        AccountDetails accountDetails = accountDetailsDAO.find(address);

        return accountDetails;
    }


    public static List<InstanceUsedToken> getInstanceUsedTokenList(String address) throws ExecutionException {
        List<InstanceUsedToken> instanceUsedTokens = Lists.newArrayList();


        AccountDetails accountDetails = AccountDetailsService.getAccountDetails(address);
        Map<Long, InstanceUsedToken> usedInstances = accountDetails.getUsedInstances();


        for (Map.Entry<Long, InstanceUsedToken> entry : usedInstances.entrySet()) {
            InstanceUsedToken usedToken = entry.getValue();
            instanceUsedTokens.add(usedToken);
        }

        return instanceUsedTokens;
    }


    public static InstanceUsedToken getUsedDetail(String address, long instanceId) throws ExecutionException {
        InstanceUsedToken usedTokenDetail = null;


        AccountDetails accountDetails = getAccountDetails(address);
        if (accountDetails != null) {
            usedTokenDetail = accountDetails.getInstanceDetail(instanceId);
        }

        return usedTokenDetail;
    }


    public static void saveAccountDetails(long instance, Tx tx) throws RocksDBException, IOException, ExecutionException {
        String fromAddress = tx.getFrom();
        String toAddress = tx.getTo();
        long tokenId = tx.getToken();


        for (String address : Arrays.asList(fromAddress, toAddress)) {
            AccountDetails accountDetails = accountDetailsDAO.find(address);
            if (accountDetails == null) {

                accountDetails = AccountDetailsService.newAccountDetails(address);
            }


            addToken(instance, accountDetails, tokenId);


            InstanceUsedTokenService.addToken(instance, tokenId);
        }
    }


    public static void addToken(long instance, AccountDetails accountDetails, long tokenId) throws RocksDBException, IOException {
        InstanceUsedToken usedTokenDetail;


        Map<Long, InstanceUsedToken> usedInstances = accountDetails.getUsedInstances();


        if (!usedInstances.containsKey(instance)) {
            usedTokenDetail = InstanceUsedTokenService.newInstanceUsedToken(instance);

            usedInstances.put(instance, usedTokenDetail);

            usedTokenDetail.addTokenId(tokenId);

            accountDetailsDAO.put(accountDetails);
        } else {

            usedTokenDetail = usedInstances.get(instance);

            boolean hasNew = usedTokenDetail.addTokenId(tokenId);
            if (hasNew) {
                accountDetailsDAO.put(accountDetails);
            }
        }
    }


}
