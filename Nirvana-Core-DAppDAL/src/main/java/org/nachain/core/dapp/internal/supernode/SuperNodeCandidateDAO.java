package org.nachain.core.dapp.internal.supernode;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;

public class SuperNodeCandidateDAO extends RocksDAO {


    public SuperNodeCandidateDAO() {
        super("SuperNodeCandidate", CoreInstanceEnum.SUPERNODE.id);
    }


    public boolean put(SuperNodeCandidate snc) throws RocksDBException {
        put(snc.getCandidateAddress(), snc);
        return true;
    }


    public SuperNodeCandidate get(String candidateAddress) throws RocksDBException {
        String result = db.get(candidateAddress);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, SuperNodeCandidate.class);
    }


    public List<SuperNodeCandidate> findAll() {
        List<SuperNodeCandidate> sncList = db().findAll(SuperNodeCandidate.class);
        return sncList;
    }

}
