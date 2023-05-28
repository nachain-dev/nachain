package org.nachain.core.dapp.internal.supernode.vote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.nachain.core.chain.block.BlockService;
import org.nachain.core.chain.config.PricingSystem;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMarkService;
import org.nachain.core.config.KeyStoreHolder;
import org.nachain.core.crypto.Key;
import org.nachain.core.dapp.internal.supernode.SuperNode;
import org.nachain.core.dapp.internal.supernode.SuperNodeCandidate;
import org.nachain.core.dapp.internal.supernode.SuperNodeCandidateDAO;
import org.nachain.core.dapp.internal.supernode.SuperNodeDAO;
import org.nachain.core.dapp.internal.supernode.vote.events.CancelCandidateDTO;
import org.nachain.core.dapp.internal.supernode.vote.events.CandidateDTO;
import org.nachain.core.dapp.internal.supernode.vote.vo.CandidateVO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SuperNodeCandidateService {

    private static SuperNodeCandidateDAO superNodeCandidateDAO;

    static {
        superNodeCandidateDAO = new SuperNodeCandidateDAO();
    }


    public static SuperNodeCandidate newSuperNodeCandidate(String candidateAddress, BigInteger pledgeAmount, long blockHeight, String fromTx) {
        SuperNodeCandidate snc = new SuperNodeCandidate();
        snc.setCandidateAddress(candidateAddress);
        snc.setPledgeAmount(pledgeAmount);
        snc.setBlockHeight(blockHeight);
        snc.setLockedBlockHeight(calcLockedBlockHeight(blockHeight));
        snc.setFromTx(fromTx);

        return snc;
    }


    public static SuperNodeCandidate newSuperNodeCandidate(CandidateDTO candidate, Tx tx) {

        long nacBlockHeight = BlockService.getNacBlockHeight(tx);


        SuperNodeCandidate snc = new SuperNodeCandidate();
        snc.setCandidateAddress(candidate.getCandidateAddress());
        snc.setPledgeAmount(tx.getValue());
        snc.setBlockHeight(nacBlockHeight);
        snc.setLockedBlockHeight(calcLockedBlockHeight(snc.getBlockHeight()));
        snc.setFromTx(tx.getHash());

        return snc;
    }


    public static TxContext toCandidateContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<CandidateDTO>>() {
        });
    }


    public static TxContext newCandidateContext(CandidateDTO candidateDTO) {

        TxContext txContext = new TxContext<CandidateDTO>().setInstanceType(InstanceType.DApp);

        txContext.setEventType(TxEventType.SUPERNODE_CANDIDATE);

        txContext.setReferrerInstance(CoreInstanceEnum.SUPERNODE.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.SUPERNODE.id);

        txContext.setData(candidateDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext newCancelCandidateDTOContext(CancelCandidateDTO cancelCandidateDTO) {

        TxContext txContext = new TxContext<CandidateDTO>().setInstanceType(InstanceType.DApp);

        txContext.setEventType(TxEventType.SUPERNODE_CANCEL_CANDIDATE);

        txContext.setReferrerInstance(CoreInstanceEnum.SUPERNODE.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.SUPERNODE.id);

        txContext.setData(cancelCandidateDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static long calcLockedBlockHeight(long blockHeight) {
        return PricingSystem.SuperNode.CANDIDATE_LOCKED_BLOCK_HEIGHT + blockHeight;
    }


    public static SuperNodeCandidate getSuperNodeCandidate(String candidateAddress) throws RocksDBException {
        SuperNodeCandidate superNodeCandidate = superNodeCandidateDAO.get(candidateAddress);
        return superNodeCandidate;
    }


    public static boolean isSuperNodeCandidate(String candidateAddress) {

        SuperNodeCandidate superNodeCandidate;
        try {
            superNodeCandidate = getSuperNodeCandidate(candidateAddress);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }

        if (superNodeCandidate == null) {
            return false;
        }


        if (!existKey(candidateAddress)) {
            return false;
        }

        return true;
    }


    private static boolean existKey(String candidateAddress) {

        Key key = KeyStoreHolder.getKey(candidateAddress);
        if (key == null) {
            return false;
        }

        return true;
    }


    public static List<CandidateVO> findCandidate(long instance) throws RocksDBException {

        List<CandidateVO> voList = Lists.newArrayList();

        SuperNodeDAO superNodeDAO = new SuperNodeDAO();


        List<SuperNodeCandidate> candidateList = superNodeCandidateDAO.findAll();
        for (SuperNodeCandidate superNodeCandidate : candidateList) {

            if (existKey(superNodeCandidate.getCandidateAddress())) {

                BigInteger voteAmount = BigInteger.ZERO;

                SuperNode superNode = superNodeDAO.get(instance, superNodeCandidate.getCandidateAddress());
                if (superNode != null) {
                    voteAmount = superNode.getAmountTotal();
                }


                CandidateVO candidateVO = new CandidateVO();
                candidateVO.setCandidateAddress(superNodeCandidate.getCandidateAddress());
                candidateVO.setPledgeAmount(superNodeCandidate.getPledgeAmount());
                candidateVO.setVoteAmount(voteAmount);

                voList.add(candidateVO);
            }
        }


        sortCandidate(voList);

        return voList;
    }


    public static void sortCandidate(List<CandidateVO> voList) {

        Collections.sort(voList, new Comparator<CandidateVO>() {
            @Override
            public int compare(CandidateVO o1, CandidateVO o2) {


                return o2.getVoteAmount().compareTo(o1.getVoteAmount());
            }
        });
    }


}
