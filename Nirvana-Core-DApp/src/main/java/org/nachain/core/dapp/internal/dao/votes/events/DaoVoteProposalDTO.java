package org.nachain.core.dapp.internal.dao.votes.events;

import lombok.Data;
import org.nachain.core.dapp.internal.dao.votes.VoteTypeEnum;

import java.math.BigDecimal;


@Data
public class DaoVoteProposalDTO {


    VoteTypeEnum voteTypeEnum;


    long proposalBlockHeight;


    String proposalTx;


    String proposalAddress;


    BigDecimal proposalValue;
}
