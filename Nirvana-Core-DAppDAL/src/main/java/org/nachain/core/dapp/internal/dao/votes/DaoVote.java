package org.nachain.core.dapp.internal.dao.votes;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class DaoVote {


    VoteTypeEnum voteTypeEnum;


    long proposalBlockHeight;


    String proposalTx;


    String proposalAddress;


    BigDecimal proposalValue;


    String hash;


    List<VoteItem> voteItems;


    long generateResultBlockHeight;


    String generateResultTx;


    boolean result;

}

