package org.nachain.core.dapp.internal.supernode.vote;

import org.nachain.core.base.Amount;


public interface IVoteCenters {


    void voteSuperNode(String voteAddress, String nominateAdress, Amount amount, String signData);


    void checkTicketSuperNode(String minerAdress);

}
