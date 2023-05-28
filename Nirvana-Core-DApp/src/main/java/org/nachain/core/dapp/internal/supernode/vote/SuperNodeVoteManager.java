package org.nachain.core.dapp.internal.supernode.vote;


import lombok.extern.slf4j.Slf4j;
import org.rocksdb.RocksDBException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class SuperNodeVoteManager {


    private final Map<String, SuperNodeVote> superNodeVoteMap = new ConcurrentHashMap<String, SuperNodeVote>();

    private SuperNodeVoteDAO superNodeVoteDAO;

    public SuperNodeVoteManager() {
        try {
            superNodeVoteDAO = new SuperNodeVoteDAO();
        } catch (Exception e) {
            log.error("new SuperNodeVoteManager() error", e);
        }

        loadDB();
    }


    private void loadDB() {

        try {
            List<SuperNodeVote> superNodeVoteList = superNodeVoteDAO.findAll();

            for (SuperNodeVote superNodeVote : superNodeVoteList) {
                addToMap(superNodeVote);
            }
            log.info("Initialize FullNode:" + superNodeVoteList.size());
        } catch (Exception e) {
            log.error("Initialize FullNodeManager error", e);
        }
    }


    public void addToMap(SuperNodeVote superNodeVote) throws RocksDBException {
        String beneficiaryAddress = superNodeVote.getBeneficiaryAddress();

        if (!superNodeVoteMap.containsKey(beneficiaryAddress)) {
            superNodeVoteMap.put(beneficiaryAddress, superNodeVote);
        }
    }


    public void remove(String beneficiaryAddress) {
        if (superNodeVoteMap.containsKey(beneficiaryAddress)) {
            superNodeVoteMap.remove(beneficiaryAddress);
        }
    }


    public SuperNodeVote get(String beneficiaryAddress) throws RocksDBException {
        SuperNodeVote superNodeVote = superNodeVoteMap.get(beneficiaryAddress);
        if (superNodeVote == null) {

            loadDB();

            superNodeVote = superNodeVoteMap.get(beneficiaryAddress);
        }

        return superNodeVote;
    }


    public Map<String, SuperNodeVote> findAll() {
        return superNodeVoteMap;
    }


    public boolean exist(String beneficiaryAddress) {
        return superNodeVoteMap.containsKey(beneficiaryAddress);
    }


    public int count() {
        return superNodeVoteMap.size();
    }

}

