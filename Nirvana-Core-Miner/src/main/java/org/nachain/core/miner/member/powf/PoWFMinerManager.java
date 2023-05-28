package org.nachain.core.miner.member.powf;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.miner.member.AbstractMinerManager;
import org.nachain.core.miner.member.Maintain;
import org.nachain.core.miner.member.MinerMember;
import org.nachain.core.miner.member.MinerMemberService;
import org.nachain.core.nodes.NodeRole;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class PoWFMinerManager extends AbstractMinerManager {


    private List<MinerMember> powfNodes = new ArrayList<>();


    private List<MinerMember> dnsNodes = new ArrayList<>();


    private List<MinerMember> dataflowNodes = new ArrayList<>();

    public PoWFMinerManager(long instance) {

        super(instance);


        rebuild();
    }

    @Override
    public void clear() {

        powfNodes.clear();
        dnsNodes.clear();
        dataflowNodes.clear();
    }


    @Override
    public void addMiner(MinerMember minerMember) {

        Maintain mi = minerMember.getMaintain(this.instance);

        NodeRole nodeRole = mi.getNodeRole();

        switch (nodeRole) {
            case POWF_DNS:

                if (isOnline(minerMember.getWalletAddress())) {

                    dnsNodes.add(minerMember);

                    powfNodes.add(minerMember);
                }
                break;
            case POWF_DATAFLOW:

                if (isOnline(minerMember.getWalletAddress())) {

                    dataflowNodes.add(minerMember);

                    powfNodes.add(minerMember);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public List<MinerMember> nodes() {
        r.lock();
        try {
            return powfNodes;
        } finally {
            r.unlock();
        }
    }

    @Override
    public List<String> nodesAddress() {
        r.lock();
        try {
            return MinerMemberService.convert(powfNodes);
        } finally {
            r.unlock();
        }
    }


    public List<MinerMember> getPowfNodes() {
        r.lock();
        try {
            return powfNodes;
        } finally {
            r.unlock();
        }
    }


    public List<MinerMember> getDnsNodes() {
        r.lock();
        try {
            return dnsNodes;
        } finally {
            r.unlock();
        }
    }

    public List<String> dnsNodesAddress() {
        r.lock();
        try {
            return MinerMemberService.convert(dnsNodes);
        } finally {
            r.unlock();
        }
    }

    public List<MinerMember> getDataflowNodes() {
        r.lock();
        try {
            return dataflowNodes;
        } finally {
            r.unlock();
        }
    }


    public String getInfo() {
        return new StringBuffer()
                .append("powfNodes:" + this.powfNodes.size() + ",")
                .append("dnsNodes:" + this.dnsNodes.size() + ",")
                .append("dataflowNodes:" + this.dataflowNodes.size())
                .toString();
    }


    public void printMiner(List<MinerMember> minerMembers) {
        for (MinerMember minerMember : minerMembers) {
            System.out.println(minerMember.toString());
        }
    }

}
