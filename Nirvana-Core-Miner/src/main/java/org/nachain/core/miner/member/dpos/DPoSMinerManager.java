package org.nachain.core.miner.member.dpos;

import org.nachain.core.miner.member.AbstractMinerManager;
import org.nachain.core.miner.member.Maintain;
import org.nachain.core.miner.member.MinerMember;
import org.nachain.core.miner.member.MinerMemberService;
import org.nachain.core.nodes.NodeRole;

import java.util.ArrayList;
import java.util.List;

public class DPoSMinerManager extends AbstractMinerManager {


    private List<MinerMember> dposNodes = new ArrayList<>();


    private List<MinerMember> superNodes = new ArrayList<>();


    private List<MinerMember> fullNodes = new ArrayList<>();


    private List<MinerMember> temporaryNodes = new ArrayList<>();


    private List<MinerMember> winVoteNodes = new ArrayList<>();


    private List<MinerMember> loseVoteNodes = new ArrayList<>();


    private List<MinerMember> activityNodes = new ArrayList<>();

    public DPoSMinerManager(long instance) {

        super(instance);

        rebuild();
    }

    @Override
    public void clear() {
        w.lock();
        try {

            dposNodes.clear();
            superNodes.clear();
            fullNodes.clear();
            temporaryNodes.clear();
            winVoteNodes.clear();
            loseVoteNodes.clear();
            activityNodes.clear();
        } finally {
            w.unlock();
        }
    }

    @Override
    public void addMiner(MinerMember minerMember) {
        w.lock();
        try {

            Maintain mi = minerMember.getMaintain(this.instance);

            NodeRole nodeRole = mi.getNodeRole();

            switch (nodeRole) {
                case DPOS_SUPER:

                    if (isOnline(minerMember.getWalletAddress())) {

                        superNodes.add(minerMember);

                        fullNodes.add(minerMember);

                        dposNodes.add(minerMember);
                    }
                    break;
                case DPOS_FULLNODE:

                    if (isOnline(minerMember.getWalletAddress())) {

                        fullNodes.add(minerMember);

                        dposNodes.add(minerMember);
                    }
                    break;
                case DPOS_TEMPORARY:

                    temporaryNodes.add(minerMember);

                    dposNodes.add(minerMember);
                    break;
                default:
                    break;
            }


            if (isOnline(minerMember.getWalletAddress())) {

                winVoteNodes.add(minerMember);

                activityNodes.add(minerMember);
            }
        } finally {
            w.unlock();
        }
    }


    @Override
    public List<MinerMember> nodes() {
        r.lock();
        try {
            return dposNodes;
        } finally {
            r.unlock();
        }
    }

    @Override
    public List<String> nodesAddress() {
        r.lock();
        try {
            return MinerMemberService.convert(dposNodes);
        } finally {
            r.unlock();
        }
    }

    public List<String> superNodesAddress() {
        r.lock();
        try {
            return MinerMemberService.convert(superNodes);
        } finally {
            r.unlock();
        }
    }


    public List<MinerMember> getSuperNodes() {
        r.lock();
        try {
            return superNodes;
        } finally {
            r.unlock();
        }
    }


    public List<MinerMember> getFullNodes() {
        r.lock();
        try {

            return fullNodes;
        } finally {
            r.unlock();
        }
    }

    public List<MinerMember> getTemporaryNodes() {
        r.lock();
        try {
            return temporaryNodes;
        } finally {
            r.unlock();
        }
    }

    public List<MinerMember> getWinVoteNodes() {
        r.lock();
        try {
            return winVoteNodes;
        } finally {
            r.unlock();
        }
    }

    public List<MinerMember> getLoseVoteNodes() {
        r.lock();
        try {
            return loseVoteNodes;
        } finally {
            r.unlock();
        }
    }

    public List<MinerMember> getActivityNodes() {
        r.lock();
        try {
            return activityNodes;
        } finally {
            r.unlock();
        }
    }

    public String getInfo() {
        return new StringBuffer()
                .append("dposNodes:" + this.dposNodes.size() + ",")
                .append("superNodes:" + this.superNodes.size() + ",")
                .append("fullNodes:" + this.fullNodes.size() + ",")
                .append("temporaryNodes:" + this.temporaryNodes.size() + ",")
                .append("winVoteNodes:" + this.winVoteNodes.size() + ",")
                .append("loseVoteNodes:" + this.loseVoteNodes.size() + ",")
                .append("activityNodes:" + this.activityNodes.size())
                .toString();
    }

}
