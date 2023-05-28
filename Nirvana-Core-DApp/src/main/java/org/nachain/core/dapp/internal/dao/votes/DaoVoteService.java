package org.nachain.core.dapp.internal.dao.votes;

import com.fasterxml.jackson.core.type.TypeReference;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMarkService;
import org.nachain.core.dapp.internal.dao.votes.events.DaoVoteProposalDTO;
import org.nachain.core.util.JsonUtils;

public class DaoVoteService {


    public static TxContext toDaoVoteProposalDTOContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<DaoVoteProposalDTO>>() {
        });
    }


    public static TxContext newDaoVoteProposalContext(DaoVoteProposalDTO proposalDTO) {

        TxContext txContext = new TxContext<DaoVoteProposalDTO>().setInstanceType(InstanceType.Core);

        txContext.setEventType(TxEventType.DAO_VOTES);

        txContext.setReferrerInstance(CoreInstanceEnum.SUPERNODE.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.SUPERNODE.id);

        txContext.setData(proposalDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }
}
