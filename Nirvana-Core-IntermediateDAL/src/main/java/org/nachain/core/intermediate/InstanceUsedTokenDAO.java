package org.nachain.core.intermediate;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;

public class InstanceUsedTokenDAO extends RocksDAO {


    public InstanceUsedTokenDAO() throws RocksDBException, IOException {
        super("InstanceUsedToken", CoreInstanceEnum.APPCHAIN.id);
    }


    public boolean put(InstanceUsedToken instanceUsedToken) throws RocksDBException {
        put(instanceUsedToken.getInstance(), instanceUsedToken);
        return true;
    }


    public InstanceUsedToken get(long instance) throws RocksDBException {
        String result = db.get(instance);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, InstanceUsedToken.class);
    }


    public long count() {
        return db.count();
    }


}
