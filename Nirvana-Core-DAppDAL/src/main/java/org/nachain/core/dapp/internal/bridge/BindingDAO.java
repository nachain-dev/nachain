package org.nachain.core.dapp.internal.bridge;

import com.google.common.cache.CacheLoader;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;
import java.util.Optional;


public class BindingDAO extends RocksDAO {


    public BindingDAO() {
        super("Binding", CoreInstanceEnum.Bridge.id);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<Binding>>() {
            public Optional<Binding> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public boolean add(Binding binding) throws RocksDBException {
        String result = db.get(toKeyName(binding));

        if (result != null) {
            return false;
        }
        put(toKeyName(binding), binding);
        return true;
    }


    public boolean edit(Binding binding) throws RocksDBException {
        put(toKeyName(binding), binding);
        return true;
    }


    public boolean delete(String key) throws RocksDBException {
        return super.delete(key);
    }


    public Binding get(String key) throws RocksDBException {
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Binding.class);
    }


    public Binding get(String address, ChainName chainName) throws RocksDBException {
        return get(toKeyName(address, chainName));
    }


    public List findAll() {
        return db.findAll(Binding.class);
    }


    public String toKeyName(Binding binding) {
        return toKeyName(binding.getAddress(), binding.getChainName());
    }


    public String toKeyName(String address, ChainName chainName) {
        return address + "_" + chainName.toString();
    }


}
