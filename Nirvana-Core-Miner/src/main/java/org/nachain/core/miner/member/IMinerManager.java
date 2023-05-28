package org.nachain.core.miner.member;

import java.util.List;

public interface IMinerManager {


    void clear();


    void rebuild();


    void addMiner(MinerMember minerMember);


    List<MinerMember> nodes();

    List<String> nodesAddress();

}
