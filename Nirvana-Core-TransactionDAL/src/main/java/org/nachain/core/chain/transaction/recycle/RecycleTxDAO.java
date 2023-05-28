package org.nachain.core.chain.transaction.recycle;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;


public class RecycleTxDAO extends RocksDAO {


    public RecycleTxDAO() throws RocksDBException, IOException {
        super("RecycleTx", CoreInstanceEnum.APPCHAIN.id);
    }


    public boolean add(RecycleTx recycleTx) throws RocksDBException {
        put(recycleTx.getHash(), recycleTx);
        return true;
    }


    public boolean delete(String rtHash) throws RocksDBException {
        return super.delete(rtHash);
    }


    public RecycleTx find(String rtHash) throws RocksDBException {
        String result = db.get(rtHash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, RecycleTx.class);
    }


    public List<RecycleTx> findAll() {
        return db.findAll(RecycleTx.class);
    }

}
