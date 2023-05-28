package org.nachain.core.chain.das;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

@Slf4j
public class DasProfileDAO extends RocksDAO {


    public DasProfileDAO(long instance) {
        super("DasProfile", instance);
    }

    private String key(DasProfile dasProfile) {
        return key(dasProfile.getAddress(), dasProfile.getToken());
    }

    private String key(String wallet, long token) {
        return wallet + "_" + token;
    }


    public boolean put(DasProfile account) throws RocksDBException {
        put(key(account), account);
        return true;
    }


    public DasProfile get(String address, long token) throws RocksDBException {
        String result = db.get(key(address, token));
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, DasProfile.class);
    }

}
