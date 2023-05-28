package org.nachain.core.dapp.internal.supernode;

import org.nachain.core.dapp.internal.supernode.vote.SuperNodeVote;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SuperNodeService {


    public static SuperNode newSuperNode(long instance, String nominateAddress, BigInteger amountTotal) {
        SuperNode superNode = new SuperNode();
        superNode.setInstance(instance);
        superNode.setNominateAddress(nominateAddress);
        superNode.setAmountTotal(amountTotal);

        return superNode;
    }


    public static SuperNode get(long voteInstance, String walletAddress) throws RocksDBException {
        SuperNodeDAO superNodeDAO = new SuperNodeDAO();

        SuperNode superNode = superNodeDAO.get(voteInstance, walletAddress);
        return superNode;
    }


    public static SuperNode addVote(SuperNodeVote vote) throws RocksDBException {
        SuperNodeDAO superNodeDAO = new SuperNodeDAO();


        SuperNode superNode = superNodeDAO.get(vote.getVoteInstance(), vote.getNominateAddress());

        if (superNode == null) {
            superNode = SuperNodeService.newSuperNode(vote.getVoteInstance(), vote.getNominateAddress(), vote.getAmount());
        } else {
            superNode.addAmount(vote.getAmount());
        }


        superNodeDAO.put(superNode);

        return superNode;
    }


    public static SuperNode subtractVote(SuperNodeVote vote) throws RocksDBException {
        SuperNodeDAO superNodeDAO = new SuperNodeDAO();


        SuperNode superNode = superNodeDAO.get(vote.getVoteInstance(), vote.getNominateAddress());

        if (superNode != null) {
            superNode.subtractAmount(vote.getAmount());

            superNodeDAO.put(superNode);
        }

        return superNode;
    }


    public static void sortVote(List<SuperNode> superNodeList) {

        Collections.sort(superNodeList, new Comparator<SuperNode>() {
            @Override
            public int compare(SuperNode o1, SuperNode o2) {

                return o2.getAmountTotal().compareTo(o1.getAmountTotal());
            }
        });
    }


}
