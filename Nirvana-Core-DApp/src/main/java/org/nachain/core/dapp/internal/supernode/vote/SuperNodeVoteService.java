package org.nachain.core.dapp.internal.supernode.vote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import org.nachain.core.base.Amount;
import org.nachain.core.base.Unit;
import org.nachain.core.chain.block.BlockService;
import org.nachain.core.chain.config.PricingSystem;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.TxReservedWord;
import org.nachain.core.chain.transaction.TxSendService;
import org.nachain.core.chain.transaction.TxService;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxContextService;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMarkService;
import org.nachain.core.config.miner.Mining;
import org.nachain.core.crypto.Key;
import org.nachain.core.dapp.internal.fullnode.FullNode;
import org.nachain.core.dapp.internal.fullnode.FullNodeService;
import org.nachain.core.dapp.internal.supernode.SuperNodeService;
import org.nachain.core.dapp.internal.supernode.vote.events.CancelVoteDTO;
import org.nachain.core.dapp.internal.supernode.vote.events.VoteDTO;
import org.nachain.core.intermediate.AccountTxHeightService;
import org.nachain.core.miner.member.MaintainInstanceService;
import org.nachain.core.miner.member.MinerMember;
import org.nachain.core.miner.member.MinerMemberService;
import org.nachain.core.nodes.NodeRole;
import org.nachain.core.token.CoreTokenEnum;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SuperNodeVoteService {

    private static SuperNodeVoteDAO superNodeVoteDAO;

    static {
        try {
            superNodeVoteDAO = new SuperNodeVoteDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static TxContext newVoteContext(VoteDTO voteDTO) {

        TxContext txContext = new TxContext<VoteDTO>().setInstanceType(InstanceType.DApp);

        txContext.setEventType(TxEventType.SUPERNODE_VOTE);

        txContext.setReferrerInstance(CoreInstanceEnum.SUPERNODE.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.SUPERNODE.id);

        txContext.setData(voteDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext toVoteContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<VoteDTO>>() {
        });
    }


    public static VoteDTO newVoteDTO(long voteInstance, String voteAddress, String beneficiaryAddress, String nominateAdress, BigInteger amount) {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setVoteInstance(voteInstance);
        voteDTO.setVoteAddress(voteAddress);
        voteDTO.setBeneficiaryAddress(beneficiaryAddress);
        voteDTO.setNominateAddress(nominateAdress);
        voteDTO.setAmount(amount);

        return voteDTO;
    }


    public static SuperNodeVote newSuperNodeVote(long voteInstance, long voteTxBlockHeight, String voteTxHash, String voteAddress, String nominateAddress, String beneficiaryAddress, BigInteger amount) {

        long nacBlockHeight = BlockService.getNacBlockHeight(CoreInstanceEnum.SUPERNODE.id, voteTxBlockHeight);

        SuperNodeVote superNodeVote = new SuperNodeVote();
        superNodeVote.setVoteInstance(voteInstance);
        superNodeVote.setVoteTx(voteTxHash);
        superNodeVote.setBlockHeight(nacBlockHeight);
        superNodeVote.setUnlockHeight(nacBlockHeight + PricingSystem.SuperNode.VOTE_CANCEL_INTERVAL);
        superNodeVote.setVoteAddress(voteAddress);
        superNodeVote.setBeneficiaryAddress(beneficiaryAddress);
        superNodeVote.setNominateAddress(nominateAddress);
        superNodeVote.setAmount(amount);
        superNodeVote.setHash(superNodeVote.encodeHashString());
        superNodeVote.setCancelTx("");
        superNodeVote.setWithdrawTx("");

        return superNodeVote;
    }


    public static SuperNodeVote newSuperNodeVote(long voteInstance, Tx voteTx, String voteAddress, String nominateAddress, String beneficiaryAddress, BigInteger amount) {
        return newSuperNodeVote(voteInstance, voteTx.getBlockHeight(), voteTx.getHash(), voteAddress, nominateAddress, beneficiaryAddress, amount);
    }


    public static SuperNodeVote newSuperNodeVote(long voteInstance, Tx voteTx, String voteAddress, String nominateAdress, String beneficiaryAddress, long tokenAmount) {
        return newSuperNodeVote(voteInstance, voteTx, voteAddress, nominateAdress, beneficiaryAddress, Amount.of(BigInteger.valueOf(tokenAmount), Unit.NAC).toBigInteger());
    }


    public static SuperNodeVote newSuperNodeVote(Tx voteTx, VoteDTO voteDTO) {
        return newSuperNodeVote(voteDTO.getVoteInstance(), voteTx, voteDTO.getVoteAddress(), voteDTO.getNominateAddress(), voteDTO.getBeneficiaryAddress(), voteDTO.getAmount());
    }


    public static CancelVoteDTO newCancelDTO(String voteHash) {
        CancelVoteDTO cancelVote = new CancelVoteDTO();
        cancelVote.setVoteHash(voteHash);

        return cancelVote;
    }


    public static CancelVoteDTO toCancelVote(String json) {
        return JsonUtils.jsonToPojo(json, CancelVoteDTO.class);
    }


    public static TxContext toCancelVoteContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<CancelVoteDTO>>() {
        });
    }


    public static TxContext newCancelContext(CancelVoteDTO cancelVoteDTO) {

        TxContext txContext = new TxContext<CancelVoteDTO>().setInstanceType(InstanceType.DApp);

        txContext.setEventType(TxEventType.SUPERNODE_CANCEL);

        txContext.setReferrerInstance(CoreInstanceEnum.SUPERNODE.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.SUPERNODE.id);

        txContext.setData(cancelVoteDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static void addVote(SuperNodeVote vote) throws RocksDBException, IOException, ExecutionException {

        superNodeVoteDAO.add(vote);

        SuperNodeVoteSingleton.get().addToMap(vote);


        SuperNodeService.addVote(vote);


        FullNode fullNode = FullNodeService.get(vote.getBeneficiaryAddress());

        fullNode.addVoteAmount(vote.getAmount());

        FullNodeService.edit(fullNode);


        NodeRole nodeRole = fullNode.getApplyNodeRole();
        long mainInstance;
        if (nodeRole == NodeRole.POWF_DATAFLOW || nodeRole == NodeRole.POWF_DNS) {
            mainInstance = CoreInstanceEnum.APPCHAIN.id;
        } else {
            mainInstance = vote.getVoteInstance();
        }

        MinerMember minerMember = MinerMemberService.getMinerMember(vote.getBeneficiaryAddress());

        minerMember.addMaintain(MaintainInstanceService.newMaintainInstance(mainInstance, nodeRole, vote.getAmount()));

        MinerMemberService.saveMinerMember(minerMember);
    }


    public static void delVote(SuperNodeVote vote) throws Exception {

        long instanceId = CoreInstanceEnum.SUPERNODE.id;


        superNodeVoteDAO.delete(vote.getHash());

        SuperNodeVoteSingleton.get().remove(vote.getBeneficiaryAddress());


        SuperNodeService.subtractVote(vote);


        Key minerKey = Mining.getKey();

        Tx eventWithdraw = TxService.newEventWithdrawTx(instanceId, CoreTokenEnum.NAC.id, TxReservedWord.INSTANCE.name, vote.getBeneficiaryAddress(), vote.getAmount(), AccountTxHeightService.nextTxHeight(TxReservedWord.INSTANCE.name, instanceId, CoreTokenEnum.NAC.id), TxContextService.newTransferContext(instanceId), minerKey);
        try {

            TxSendService.sendTx(eventWithdraw);
        } catch (RuntimeException e) {
            throw new ChainException("eventWithdraw", e);
        }


        FullNode fullNode = FullNodeService.get(vote.getBeneficiaryAddress());

        fullNode.subtractVoteAmount(vote.getAmount());

        FullNodeService.edit(fullNode);


        NodeRole nodeRole = fullNode.getApplyNodeRole();
        long mainInstance = SuperNodeVoteService.getMainInstance(nodeRole, vote);

        MinerMember minerMember = MinerMemberService.getMinerMember(vote.getBeneficiaryAddress());

        minerMember.delMaintain(mainInstance);

        MinerMemberService.saveMinerMember(minerMember);
    }


    public static boolean isCancel(SuperNodeVote superNodeVote) {
        return !Strings.isNullOrEmpty(superNodeVote.getCancelTx());
    }


    public static BigInteger sumVote() {
        BigInteger amount = BigInteger.ZERO;
        List<SuperNodeVote> superNodeVoteList = superNodeVoteDAO.findAll();
        for (SuperNodeVote superNodeVote : superNodeVoteList) {

            if (isCancel(superNodeVote)) {
                continue;
            }
            amount = amount.add(superNodeVote.getAmount());
        }
        return amount;
    }


    public static BigInteger sumUnVote() {
        BigInteger amount = BigInteger.ZERO;
        List<SuperNodeVote> superNodeVoteList = superNodeVoteDAO.findAll();
        for (SuperNodeVote superNodeVote : superNodeVoteList) {

            if (!isCancel(superNodeVote)) {
                continue;
            }
            amount = amount.add(superNodeVote.getAmount());
        }
        return amount;
    }


    public static long getMainInstance(NodeRole nodeRole, SuperNodeVote vote) {
        long mainInstance;
        if (nodeRole == NodeRole.POWF_DATAFLOW || nodeRole == NodeRole.POWF_DNS) {
            mainInstance = CoreInstanceEnum.APPCHAIN.id;
        } else {
            mainInstance = vote.getVoteInstance();
        }
        return mainInstance;
    }

}
