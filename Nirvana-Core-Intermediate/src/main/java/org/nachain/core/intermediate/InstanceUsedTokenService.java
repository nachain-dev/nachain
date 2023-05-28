package org.nachain.core.intermediate;

import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.HashSet;

public class InstanceUsedTokenService {

    private static InstanceUsedTokenDAO instanceUsedTokenDAO;

    static {
        try {
            instanceUsedTokenDAO = new InstanceUsedTokenDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static InstanceUsedToken newInstanceUsedToken(long instance) {
        InstanceUsedToken instanceUsedToken = new InstanceUsedToken();
        instanceUsedToken.setInstance(instance);
        instanceUsedToken.setTokenIds(new HashSet<>());

        return instanceUsedToken;
    }


    public static void addToken(long instance, long tokenId) throws RocksDBException {
        InstanceUsedToken instanceUsedToken = instanceUsedTokenDAO.get(instance);
        if (instanceUsedToken == null) {
            instanceUsedToken = newInstanceUsedToken(instance);
        }

        boolean hasNew = instanceUsedToken.addTokenId(tokenId);
        if (hasNew) {

            instanceUsedTokenDAO.put(instanceUsedToken);
        }
    }


    public static InstanceUsedToken getInstanceUsedToken(long instance) throws RocksDBException {
        InstanceUsedToken instanceUsedToken = instanceUsedTokenDAO.get(instance);

        return instanceUsedToken;
    }


}
