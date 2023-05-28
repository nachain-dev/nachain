package org.nachain.core.miner.member;

import org.nachain.core.config.KeyStoreHolder;
import org.nachain.core.crypto.Key;
import org.nachain.core.wallet.walletskill.NirvanaWalletSkill;

import java.util.Iterator;
import java.util.List;


public class MinerMember {


    private String walletAddress;


    private String minerAddress;


    private List<Maintain> maintainList;

    public MinerMember() {
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public MinerMember setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
        return this;
    }

    public String getMinerAddress() {
        return minerAddress;
    }

    public MinerMember setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress;
        return this;
    }

    public List<Maintain> getMaintainList() {
        return maintainList;
    }

    public MinerMember setMaintainList(List<Maintain> maintainList) {
        this.maintainList = maintainList;
        return this;
    }


    @Override
    public String toString() {
        return "MinerMember{" +
                "walletAddress='" + walletAddress + '\'' +
                ", minerAddress='" + minerAddress + '\'' +
                ", maintainList=" + maintainList +
                '}';
    }


    public Key toMinerKey() {
        Key key = KeyStoreHolder.getKey(this.walletAddress);
        if (key != null) {
            key.init(new NirvanaWalletSkill());
        }
        return key;
    }

    public void addMaintain(Maintain mi) {

        maintainList.add(mi);
    }


    public List<Maintain> putMaintain(Maintain maintain) {
        Iterator<Maintain> iter = maintainList.iterator();
        while (iter.hasNext()) {
            Maintain m = iter.next();

            if (m.getInstance() == maintain.getInstance()) {
                iter.remove();
            }
        }
        maintainList.add(maintain);

        return maintainList;
    }


    public List<Maintain> delMaintain(long mainInstance) {
        Iterator<Maintain> iter = maintainList.iterator();
        while (iter.hasNext()) {
            Maintain m = iter.next();

            if (m.getInstance() == mainInstance) {
                iter.remove();
            }
        }
        return maintainList;
    }


    public Maintain getMaintain(long instance) {
        for (Maintain mi : maintainList) {
            if (instance == mi.getInstance()) {
                return mi;
            }
        }

        return null;
    }


}

