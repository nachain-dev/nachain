package org.nachain.core.chain.transaction;

import com.google.common.collect.Lists;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class TxIndexDAO extends RocksDAO {


    public TxIndexDAO(long instance) throws RocksDBException, IOException {
        super("TxIndex", instance);
    }


    public boolean put(TxIndex txIndex) throws RocksDBException {
        put(txIndex.getId().toString(), txIndex);
        return true;
    }


    public TxIndex get(BigInteger id) throws RocksDBException {
        String result = db.get(id.toString());
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, TxIndex.class);
    }


    public List<TxIndex> gets(long startId, long endId) throws RocksDBException {
        List<TxIndex> txIndexList = db.gets(startId, endId, TxIndex.class);

        return txIndexList;
    }


    public List<TxIndex> gets(List<BigInteger> keyList) throws RocksDBException {
        List<TxIndex> txIndexList = Lists.newArrayList();
        for (BigInteger index : keyList) {
            TxIndex txIndex = db.get(index.longValue(), TxIndex.class);
            txIndexList.add(txIndex);
        }

        return txIndexList;
    }


    public long count() {
        return db.count();
    }

}
