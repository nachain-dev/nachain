package org.nachain.core.miner.member;

import org.nachain.core.nodes.NodeRole;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MaintainInstanceService {


    public static Maintain newMaintainInstance(long instance, NodeRole nodeRole) {
        Maintain maintain = new Maintain();
        maintain.setInstance(instance);
        maintain.setNodeRole(nodeRole);
        maintain.setVoteTotal(BigInteger.ZERO);

        return maintain;
    }


    public static Maintain newMaintainInstance(long instance, NodeRole nodeRole, BigInteger voteTotal) {
        Maintain maintain = new Maintain();
        maintain.setInstance(instance);
        maintain.setNodeRole(nodeRole);
        maintain.setVoteTotal(voteTotal);

        return maintain;
    }


    public static List<MinerMember> getMaintainInstance(final long instance, final List<MinerMember> minerMembers) {
        List<MinerMember> maintainMiner = new ArrayList<>();


        for (MinerMember minerMember : minerMembers) {

            Maintain maintain = minerMember.getMaintain(instance);

            if (maintain != null) {
                maintainMiner.add(minerMember);
            }
        }

        return maintainMiner;
    }


    public static List<MinerMember> getMaintainMiner(long maintainInstance, NodeRole nodeRole) throws RocksDBException, IOException {
        List<MinerMember> maintainMiner = new ArrayList<>();

        MinerMemberDAO minerMemberDAO = new MinerMemberDAO();
        List<MinerMember> minerMembers = minerMemberDAO.findAll();


        for (MinerMember minerMember : minerMembers) {

            List<Maintain> maintains = minerMember.getMaintainList();

            for (Maintain mi : maintains) {

                if (mi.getInstance() == maintainInstance) {
                    if (nodeRole != null) {

                        if (mi.getNodeRole() == nodeRole) {
                            maintainMiner.add(minerMember);
                        }
                    } else {
                        maintainMiner.add(minerMember);
                    }
                }
            }
        }

        return maintainMiner;
    }


    public static List<MinerMember> getMaintainMiner(long maintainInstance) throws RocksDBException, IOException {
        return getMaintainMiner(maintainInstance, null);
    }


}
