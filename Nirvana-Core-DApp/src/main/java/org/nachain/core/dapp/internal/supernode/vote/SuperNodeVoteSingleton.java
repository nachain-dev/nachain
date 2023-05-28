package org.nachain.core.dapp.internal.supernode.vote;


public class SuperNodeVoteSingleton {

    private static SuperNodeVoteManager superNodeVoteManager;

    static {
        superNodeVoteManager = new SuperNodeVoteManager();
    }

    public static SuperNodeVoteManager get() {
        return superNodeVoteManager;
    }

}
