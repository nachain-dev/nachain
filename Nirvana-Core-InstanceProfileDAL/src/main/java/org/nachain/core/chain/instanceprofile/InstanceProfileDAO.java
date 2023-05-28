package org.nachain.core.chain.instanceprofile;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;


@Slf4j
public class InstanceProfileDAO extends RocksDAO {


    public InstanceProfileDAO(long instance) {
        super("InstanceProfile", instance);
    }

    private String key(InstanceProfile dasProfile) {
        return key(dasProfile.getAddress(), dasProfile.getToken());
    }

    private String key(String wallet, long token) {
        return wallet + "_" + token;
    }


    public boolean put(InstanceProfile account) throws RocksDBException {
        put(key(account), account);
        return true;
    }


    public InstanceProfile get(String address, long token) throws RocksDBException {
        String result = db.get(key(address, token));
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, InstanceProfile.class);
    }

}
