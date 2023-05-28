package org.nachain.core.dapp.internal.fullnode;

import com.google.common.cache.CacheLoader;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


public class FullNodeDAO extends RocksDAO {


    public FullNodeDAO() throws RocksDBException, IOException {
        super("FullNode", CoreInstanceEnum.FNC.id);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<FullNode>>() {
            public Optional<FullNode> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public FullNode find(String beneficiaryAddress) throws ExecutionException {
        return (FullNode) cache.get(beneficiaryAddress).orElse(null);
    }


    public boolean add(FullNode fullNode) throws RocksDBException {
        String result = db.get(toKeyName(fullNode));

        if (result != null) {
            return false;
        }
        put(toKeyName(fullNode), fullNode);
        return true;
    }


    public boolean edit(FullNode fullNode) throws RocksDBException {
        put(toKeyName(fullNode), fullNode);
        return true;
    }


    public boolean delete(String beneficiaryAddress) throws RocksDBException {
        return super.delete(beneficiaryAddress);
    }


    public FullNode get(String beneficiaryAddress) throws RocksDBException {
        String result = db.get(beneficiaryAddress);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, FullNode.class);
    }


    public List findAll() {
        return db.findAll(FullNode.class);
    }


    private String toKeyName(FullNode fullNode) {
        return fullNode.getBeneficiaryAddress();
    }


}
