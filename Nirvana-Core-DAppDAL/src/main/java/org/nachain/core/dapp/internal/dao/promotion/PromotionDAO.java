package org.nachain.core.dapp.internal.dao.promotion;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;

public class PromotionDAO extends RocksDAO {


    public PromotionDAO() throws RocksDBException, IOException {
        super("Promotion", CoreInstanceEnum.APPCHAIN.id);
    }


    public boolean add(Promotion promotion) throws RocksDBException {
        String clientName = promotion.getClientName();

        if (get(clientName) != null) {
            return false;
        }
        put(clientName, promotion);
        return true;
    }


    public Promotion get(String clientName) throws RocksDBException {
        String result = db.get(clientName);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Promotion.class);
    }


    public List findAll() {
        return db.findAll(Promotion.class);
    }


}
