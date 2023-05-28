package org.nachain.core.dapp.internal.supernode.vote;

import com.google.common.cache.CacheLoader;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class SuperNodeVoteDAO extends RocksDAO {


    public SuperNodeVoteDAO() throws RocksDBException, IOException {
        super("SuperNodeVote", CoreInstanceEnum.SUPERNODE.id);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<SuperNodeVote>>() {
            public Optional<SuperNodeVote> load(String key) throws RocksDBException {
                return Optional.ofNullable(get(key));
            }
        };
    }


    public SuperNodeVote find(String txHash) throws ExecutionException {
        return (SuperNodeVote) cache.get(txHash).orElse(null);
    }


    public boolean add(SuperNodeVote voteSuperNode) throws RocksDBException {
        put(voteSuperNode.getHash(), voteSuperNode);
        return true;
    }


    public boolean put(SuperNodeVote voteSuperNode) throws RocksDBException {
        put(voteSuperNode.getHash(), voteSuperNode);
        return true;
    }


    public boolean delete(String hash) throws RocksDBException {
        return super.delete(hash);
    }


    public SuperNodeVote get(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, SuperNodeVote.class);
    }


    public List findAll() {
        return db.findAll(SuperNodeVote.class);
    }


    public SuperNodeVote findByBeneficiaryAddress(String beneficiaryAddress) {
        List<SuperNodeVote> superNodeVoteList = findAll();
        for (SuperNodeVote superNodeVote : superNodeVoteList) {
            if (superNodeVote.getBeneficiaryAddress().equals(beneficiaryAddress)) {
                return superNodeVote;
            }
        }

        return null;
    }

}
