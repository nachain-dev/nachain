package org.nachain.core.dapp.internal.supernode;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;

public class SuperNodeDAO extends RocksDAO {


    public SuperNodeDAO() {
        super("SuperNode", CoreInstanceEnum.SUPERNODE.id);
    }


    @Deprecated
    public SuperNodeDAO(long instance) {
        super("SuperNode", instance);
    }


    public boolean put(SuperNode superNode) throws RocksDBException {
        String key = toKeyName(superNode);
        put(key, superNode);
        return true;
    }


    public boolean delete(SuperNode superNode) throws RocksDBException {
        String key = toKeyName(superNode);
        return db.delete(key);
    }


    public SuperNode get(long instance, String nominateAddress) throws RocksDBException {
        String key = toKeyName(instance, nominateAddress);
        String result = db.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, SuperNode.class);
    }


    public List<SuperNode> findAll() {
        List<SuperNode> superNodeList = db().findAll(SuperNode.class);
        return superNodeList;
    }


    private String toKeyName(SuperNode superNode) {
        return toKeyName(superNode.getInstance(), superNode.getNominateAddress());
    }


    private String toKeyName(long instance, String nominateAddress) {
        return instance + "_" + nominateAddress;
    }
}
