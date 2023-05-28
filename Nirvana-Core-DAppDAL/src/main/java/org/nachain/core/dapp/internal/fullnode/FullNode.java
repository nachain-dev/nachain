package org.nachain.core.dapp.internal.fullnode;

import com.google.common.collect.Lists;
import org.nachain.core.nodes.NodeRole;

import java.math.BigInteger;
import java.util.List;


public class FullNode {


    private long id;


    private long blockHeight;


    private NodeRole applyNodeRole;


    private FNApplyType applyType;


    private String ownerAddress;


    private String beneficiaryAddress;


    private String beneficiaryChangeTx;


    private BigInteger requiredNomc;


    private BigInteger requiredNac;


    private BigInteger paidNomc;


    private List<String> paidNomcTx;


    private BigInteger paidNac;


    private List<String> paidNacTx;


    private boolean enabled;


    private long enabledBlockHeight;


    private BigInteger voteAmount;


    private boolean enabledForwarding;


    private String forwardingProtocol;


    private String adminAddress;


    private boolean passExam;


    private boolean online;

    public long getId() {
        return id;
    }

    public FullNode setId(long id) {
        this.id = id;
        return this;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public FullNode setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public NodeRole getApplyNodeRole() {
        return applyNodeRole;
    }

    public FullNode setApplyNodeRole(NodeRole applyNodeRole) {
        this.applyNodeRole = applyNodeRole;
        return this;
    }

    public FNApplyType getApplyType() {
        return applyType;
    }

    public FullNode setApplyType(FNApplyType applyType) {
        this.applyType = applyType;
        return this;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public FullNode setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
        return this;
    }

    public String getBeneficiaryAddress() {
        return beneficiaryAddress;
    }

    public FullNode setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
        return this;
    }

    public String getBeneficiaryChangeTx() {
        return beneficiaryChangeTx;
    }

    public FullNode setBeneficiaryChangeTx(String beneficiaryChangeTx) {
        this.beneficiaryChangeTx = beneficiaryChangeTx;
        return this;
    }

    public BigInteger getRequiredNomc() {
        return requiredNomc;
    }

    public FullNode setRequiredNomc(BigInteger requiredNomc) {
        this.requiredNomc = requiredNomc;
        return this;
    }

    public BigInteger getRequiredNac() {
        return requiredNac;
    }

    public FullNode setRequiredNac(BigInteger requiredNac) {
        this.requiredNac = requiredNac;
        return this;
    }

    public BigInteger getPaidNomc() {
        return paidNomc;
    }

    public FullNode setPaidNomc(BigInteger paidNomc) {
        this.paidNomc = paidNomc;
        return this;
    }

    public List<String> getPaidNomcTx() {
        return paidNomcTx;
    }

    public FullNode setPaidNomcTx(List<String> paidNomcTx) {
        this.paidNomcTx = paidNomcTx;
        return this;
    }

    public BigInteger getPaidNac() {
        return paidNac;
    }

    public FullNode setPaidNac(BigInteger paidNac) {
        this.paidNac = paidNac;
        return this;
    }

    public List<String> getPaidNacTx() {
        return paidNacTx;
    }

    public FullNode setPaidNacTx(List<String> paidNacTx) {
        this.paidNacTx = paidNacTx;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public FullNode setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public long getEnabledBlockHeight() {
        return enabledBlockHeight;
    }

    public FullNode setEnabledBlockHeight(long enabledBlockHeight) {
        this.enabledBlockHeight = enabledBlockHeight;
        return this;
    }

    public BigInteger getVoteAmount() {
        return voteAmount;
    }

    public FullNode setVoteAmount(BigInteger voteAmount) {
        this.voteAmount = voteAmount;
        return this;
    }

    public boolean isEnabledForwarding() {
        return enabledForwarding;
    }

    public FullNode setEnabledForwarding(boolean enabledForwarding) {
        this.enabledForwarding = enabledForwarding;
        return this;
    }

    public String getForwardingProtocol() {
        return forwardingProtocol;
    }

    public FullNode setForwardingProtocol(String forwardingProtocol) {
        this.forwardingProtocol = forwardingProtocol;
        return this;
    }

    public String getAdminAddress() {
        return adminAddress;
    }

    public FullNode setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
        return this;
    }

    public boolean isPassExam() {
        return passExam;
    }

    public FullNode setPassExam(boolean passExam) {
        this.passExam = passExam;
        return this;
    }

    public boolean isOnline() {
        return online;
    }

    public FullNode setOnline(boolean online) {
        this.online = online;
        return this;
    }

    @Override
    public String toString() {
        return "FullNode{" +
                "id=" + id +
                ", blockHeight=" + blockHeight +
                ", applyNodeRole=" + applyNodeRole +
                ", applyType=" + applyType +
                ", ownerAddress='" + ownerAddress + '\'' +
                ", beneficiaryAddress='" + beneficiaryAddress + '\'' +
                ", beneficiaryChangeTx='" + beneficiaryChangeTx + '\'' +
                ", requiredNomc=" + requiredNomc +
                ", requiredNac=" + requiredNac +
                ", paidNomc=" + paidNomc +
                ", paidNomcTx=" + paidNomcTx +
                ", paidNac=" + paidNac +
                ", paidNacTx=" + paidNacTx +
                ", enabled=" + enabled +
                ", enabledBlockHeight=" + enabledBlockHeight +
                ", voteAmount=" + voteAmount +
                ", enabledForwarding=" + enabledForwarding +
                ", forwardingProtocol='" + forwardingProtocol + '\'' +
                ", adminAddress='" + adminAddress + '\'' +
                ", passExam=" + passExam +
                ", online=" + online +
                '}';
    }


    public void addPaidNomcTx(String txHash) {
        List<String> txList = getPaidNomcTx();
        if (txList == null) {
            txList = Lists.newArrayList();
        }
        txList.add(txHash);
    }


    public void addPaidNacTx(String txHash) {
        List<String> txList = getPaidNacTx();
        if (txList == null) {
            txList = Lists.newArrayList();
        }
        txList.add(txHash);
    }


    public void addVoteAmount(BigInteger amount) {

        voteAmount = voteAmount.add(amount);
    }


    public void subtractVoteAmount(BigInteger amount) {

        voteAmount = voteAmount.subtract(amount);
    }

}
