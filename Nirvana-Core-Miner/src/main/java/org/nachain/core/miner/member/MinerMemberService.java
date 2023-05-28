package org.nachain.core.miner.member;

import com.google.common.collect.Lists;
import org.nachain.core.chain.structure.instance.Instance;
import org.nachain.core.chain.structure.instance.InstanceSingleton;
import org.nachain.core.config.miner.MinerBasic;
import org.nachain.core.config.miner.Mining;
import org.nachain.core.miner.member.dpos.DPoSMinerHolder;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MinerMemberService {

    private static MinerMemberDAO minerMemberDAO;

    static {
        try {
            minerMemberDAO = new MinerMemberDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static MinerMember newMinerMember(String walletAddress) {
        MinerMember minerMember = new MinerMember();
        minerMember.setWalletAddress(walletAddress);
        minerMember.setMinerAddress("");

        minerMember.setMaintainList(Lists.newArrayList());
        return minerMember;
    }


    public static MinerMember newMinerMember(String walletAddress, String minerAddress, List<Maintain> maintains) {
        MinerMember minerMember = new MinerMember();
        minerMember.setWalletAddress(walletAddress);
        minerMember.setMinerAddress(minerAddress);

        minerMember.setMaintainList(maintains);
        return minerMember;
    }


    public static void add(MinerMember minerMember) {
        try {
            minerMemberDAO.add(minerMember);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static MinerMember getMinerMember(String walletAddress) {
        try {
            return minerMemberDAO.get(walletAddress);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean saveMinerMember(MinerMember minerMember) {
        try {
            return minerMemberDAO.put(minerMember);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static MinerMember getMining() {
        MinerBasic minerBasic = Mining.getMiner();
        return MinerMemberService.getMinerMember(minerBasic.getWalletAddress());
    }


    public static MinerBasic getMiningBasic() {
        MinerBasic minerBasic = Mining.getMiner();
        return minerBasic;
    }


    public static List<MinerMember> remove(String walletAddress, final List<MinerMember> minerList) {
        CopyOnWriteArrayList<MinerMember> cowList = new CopyOnWriteArrayList<>(minerList);
        for (MinerMember minerMember : cowList) {

            if (minerMember.getWalletAddress().equals(walletAddress)) {
                cowList.remove(minerMember);
            }
        }
        return cowList;
    }


    public static List<String> convert(List<MinerMember> minerMembers) {
        List<String> wallets = new ArrayList<>();
        for (MinerMember minerMember : minerMembers) {
            wallets.add(minerMember.getWalletAddress());
        }
        return wallets;
    }


    public static void updateInstanceMiner() {
        List<Instance> instances = InstanceSingleton.get().getInstanceList();
        for (Instance instance : instances) {

            DPoSMinerHolder.get(instance.getId()).rebuild();
        }
    }
}
