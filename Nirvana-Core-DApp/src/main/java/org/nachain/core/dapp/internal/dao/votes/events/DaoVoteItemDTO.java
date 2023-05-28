package org.nachain.core.dapp.internal.dao.votes.events;

import lombok.Data;


@Data
public class DaoVoteItemDTO {


    String proposalHash;


    boolean voteValue;
}
