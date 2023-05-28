package org.nachain.core.dapp.internal.dao.votes;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;

public class DaoVoteDAO extends RocksDAO {


    public DaoVoteDAO() {
        super("DaoVote", CoreInstanceEnum.SUPERNODE.id);
    }


    public boolean add(DaoVote daoVote) throws RocksDBException {
        String hash = daoVote.getHash();

        if (get(hash) != null) {
            return false;
        }
        put(hash, daoVote);
        return true;
    }


    public boolean edit(DaoVote daoVote) throws RocksDBException {
        put(daoVote.getHash(), daoVote);
        return true;
    }


    public DaoVote get(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, DaoVote.class);
    }


    public List findAll() {
        return db.findAll(DaoVote.class);
    }


}
