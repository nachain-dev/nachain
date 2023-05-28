package org.nachain.core.miner.member;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.config.ChainConfig;
import org.nachain.core.dapp.internal.fullnode.FullNode;
import org.nachain.core.dapp.internal.fullnode.FullNodeDAO;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Slf4j
public abstract class AbstractMinerManager implements IMinerManager {

    protected MinerMemberDAO minerMemberDAO;


    protected List<MinerMember> minerMembers = new ArrayList<>();


    protected List<MinerMember> maintainMiner = new ArrayList<>();


    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    protected Lock r = rwl.readLock();
    protected Lock w = rwl.writeLock();


    protected long instance;


    protected boolean disabledRebuild;


    public boolean isDisabledRebuild() {
        return disabledRebuild;
    }


    public AbstractMinerManager disabledRebuild() {
        this.disabledRebuild = true;
        return this;
    }


    public AbstractMinerManager enabledRebuild() {
        this.disabledRebuild = false;
        return this;
    }

    protected AbstractMinerManager(long instance) {
        try {
            this.instance = instance;

            minerMemberDAO = new MinerMemberDAO();
        } catch (Exception e) {
            log.error("new MinerMemberDAO() error", e);
        }
    }


    @Override
    public void rebuild() {
        if (isDisabledRebuild()) {
            return;
        }

        w.lock();
        try {

            minerMembers.clear();
            maintainMiner.clear();


            clear();


            minerMembers = minerMemberDAO.findAll();


            maintainMiner = MaintainInstanceService.getMaintainInstance(instance, minerMembers);
            for (MinerMember minerMember : maintainMiner) {

                addMiner(minerMember);
            }

            log.debug("[instance={}] rebuild() -> Miner total={}, maintainMiner={}", instance, minerMembers.size(), maintainMiner.size());
        } finally {
            w.unlock();
        }
    }


    public MinerMember getMinerByBlockHeight(long blockHeight) {
        r.lock();
        try {

            int nodesTotal = nodes().size();
            log.debug("getMinerByBlockHeight(blockHeight={}) nodesTotal:{}", blockHeight, nodesTotal);
            if (nodesTotal > 0) {
                int index = (int) (blockHeight % nodesTotal);

                index = index - 1;

                if (index == -1) {
                    index = nodesTotal - 1;
                }

                return nodes().get(index);
            } else {
                return null;
            }
        } finally {
            r.unlock();
        }
    }


    public int count() {
        r.lock();
        try {
            return nodes().size();
        } finally {
            r.unlock();
        }
    }


    public boolean isOnline(String walletAddress) {
        try {
            FullNodeDAO fullNodeDAO = new FullNodeDAO();
            FullNode fullNode = fullNodeDAO.find(walletAddress);
            if (fullNode != null) {
                if (fullNode.isEnabled() && fullNode.isPassExam() && fullNode.isOnline()) {

                    if (walletAddress.equals(ChainConfig.GENESIS_WALLET_ADDRESS)) {
                        return true;
                    }

                    if (fullNode.getVoteAmount().compareTo(BigInteger.ZERO) == 1) {
                        return true;
                    }
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

}
