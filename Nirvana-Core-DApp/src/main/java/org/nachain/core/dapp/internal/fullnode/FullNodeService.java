package org.nachain.core.dapp.internal.fullnode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.nachain.core.base.Amount;
import org.nachain.core.base.Unit;
import org.nachain.core.chain.config.IndexConfigDAO;
import org.nachain.core.chain.config.IndexConfigService;
import org.nachain.core.chain.config.IndexEnum;
import org.nachain.core.chain.index.IndexId;
import org.nachain.core.chain.index.IndexIdDAO;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMarkService;
import org.nachain.core.config.ChainConfig;
import org.nachain.core.crypto.Hash;
import org.nachain.core.dapp.internal.fullnode.events.*;
import org.nachain.core.nodes.NodeRole;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FullNodeService {

    private static FullNodeDAO fullNodeDAO;

    static {
        try {
            fullNodeDAO = new FullNodeDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static FullNode toFullNode(String json) {
        return JsonUtils.jsonToPojo(json, FullNode.class);
    }


    public static FullNode newFullNode(long index, long blockHeight, NodeRole nodeRole, String ownerAddress, String beneficiaryAddress, BigInteger paidNOMC, String paidNomcTx, BigInteger paidNAC, String paidNacTx) {
        FullNode fullNode = new FullNode();
        fullNode.setId(index);
        fullNode.setBlockHeight(blockHeight);
        fullNode.setApplyNodeRole(nodeRole);
        fullNode.setApplyType(FNApplyType.NOMC_NAC);
        fullNode.setOwnerAddress(ownerAddress);
        fullNode.setBeneficiaryAddress(beneficiaryAddress);
        fullNode.setBeneficiaryChangeTx("");
        fullNode.setRequiredNomc(FullNodeService.calcNomc(index));
        fullNode.setRequiredNac(FullNodeService.calcNac(index));
        fullNode.setPaidNomc(paidNOMC);
        if (Strings.isNullOrEmpty(paidNomcTx)) {
            fullNode.setPaidNomcTx(Lists.newArrayList());
        } else {
            fullNode.setPaidNomcTx(Lists.newArrayList(paidNomcTx));
        }
        fullNode.setPaidNac(paidNAC);
        if (Strings.isNullOrEmpty(paidNacTx)) {
            fullNode.setPaidNacTx(Lists.newArrayList());
        } else {
            fullNode.setPaidNacTx(Lists.newArrayList(paidNacTx));
        }
        fullNode.setEnabled(false);
        fullNode.setVoteAmount(BigInteger.ZERO);
        fullNode.setEnabledForwarding(false);
        fullNode.setForwardingProtocol("");
        fullNode.setAdminAddress("");
        fullNode.setPassExam(false);
        fullNode.setOnline(false);

        return fullNode;
    }


    public static FullNode addFullNode(FullNode fullNode) throws RocksDBException, IOException {

        fullNodeDAO.add(fullNode);

        IndexIdDAO indexIdDAO = new IndexIdDAO(CoreInstanceEnum.FNC.id);
        indexIdDAO.add(fullNodeDAO.getDbName(), fullNode.getId(), fullNode.getBeneficiaryAddress());

        IndexConfigService.getDAO().saveIndexId(IndexEnum.FULL_NODE, fullNode.getId());
        return fullNode;
    }


    public static long getLastId() throws RocksDBException, IOException, ExecutionException {
        IndexConfigDAO configDAO = new IndexConfigDAO();
        return configDAO.getIndexId(IndexEnum.FULL_NODE);
    }


    public static boolean verifyPaidEnabled(FullNode fullNode) {
        boolean enabled = false;


        if (fullNode.getPaidNomc().compareTo(fullNode.getRequiredNomc()) != -1) {

            if (fullNode.getPaidNac().compareTo(fullNode.getRequiredNac()) != -1) {
                enabled = true;
            }
        }

        return enabled;
    }


    public static boolean verifyPower(Tx tx, FullNode fullNode) {
        boolean enabled = false;

        if (tx.getFrom().equals(fullNode.getOwnerAddress()) || tx.getFrom().equals(fullNode.getBeneficiaryAddress())) {
            enabled = true;
        }

        return enabled;
    }


    public static BigInteger sumCalcNac(long total) {
        BigInteger nacAmount = BigInteger.ZERO;
        for (int i = 1; i <= total; i++) {
            nacAmount = nacAmount.add(FullNodeService.calcNac(i));
        }
        return nacAmount;
    }


    public static BigInteger sumCalcNomc(long total) {
        BigInteger nomcAmount = BigInteger.ZERO;
        for (int i = 1; i <= total; i++) {
            nomcAmount = nomcAmount.add(FullNodeService.calcNomc(i));
        }
        return nomcAmount;
    }


    public static BigInteger calcNac(long index) {
        if (index >= 1 && index <= 100) {
            return Amount.of(BigDecimal.valueOf(2851.2), Unit.NAC).toBigInteger();
        } else if (index >= 101 && index <= 1000) {
            return Amount.of(BigDecimal.valueOf(570.24), Unit.NAC).toBigInteger();
        } else if (index >= 1001 && index <= 2000) {
            return Amount.of(BigDecimal.valueOf(285.12), Unit.NAC).toBigInteger();
        } else if (index >= 2001 && index <= 3000) {
            return Amount.of(BigDecimal.valueOf(142.56), Unit.NAC).toBigInteger();
        } else if (index >= 3001 && index <= 5000) {
            return Amount.of(BigDecimal.valueOf(142.56), Unit.NAC).toBigInteger();
        } else if (index >= 5001 && index <= 10000) {
            return Amount.of(BigDecimal.valueOf(85.536), Unit.NAC).toBigInteger();
        } else if (index >= 10001 && index <= 20000) {
            return Amount.of(BigDecimal.valueOf(42.768), Unit.NAC).toBigInteger();
        } else if (index >= 20001 && index <= 30000) {
            return Amount.of(BigDecimal.valueOf(21.384), Unit.NAC).toBigInteger();
        } else if (index >= 30001 && index <= 50000) {
            return Amount.of(BigDecimal.valueOf(19.008), Unit.NAC).toBigInteger();
        } else if (index >= 50001 && index <= 100000) {
            return Amount.of(BigDecimal.valueOf(11.4048), Unit.NAC).toBigInteger();
        } else {
            return Amount.of(BigDecimal.valueOf(10), Unit.NAC).toBigInteger();
        }
    }


    public static BigInteger calcNomc(long index) {
        if (index >= 1 && index <= 100) {
            return Amount.of(BigDecimal.valueOf(3), Unit.NAC).toBigInteger();
        } else if (index >= 101 && index <= 1000) {
            return Amount.of(BigDecimal.valueOf(3), Unit.NAC).toBigInteger();
        } else if (index >= 1001 && index <= 2000) {
            return Amount.of(BigDecimal.valueOf(2), Unit.NAC).toBigInteger();
        } else if (index >= 2001 && index <= 3000) {
            return Amount.of(BigDecimal.valueOf(2), Unit.NAC).toBigInteger();
        } else if (index >= 3001 && index <= 5000) {
            return Amount.of(BigDecimal.valueOf(1), Unit.NAC).toBigInteger();
        } else if (index >= 5001 && index <= 10000) {
            return Amount.of(BigDecimal.valueOf(1), Unit.NAC).toBigInteger();
        } else if (index >= 10001 && index <= 20000) {
            return Amount.of(BigDecimal.valueOf(0.5), Unit.NAC).toBigInteger();
        } else if (index >= 20001 && index <= 30000) {
            return Amount.of(BigDecimal.valueOf(0.5), Unit.NAC).toBigInteger();
        } else if (index >= 30001 && index <= 50000) {
            return Amount.of(BigDecimal.valueOf(0.1), Unit.NAC).toBigInteger();
        } else if (index >= 50001 && index <= 100000) {
            return Amount.of(BigDecimal.valueOf(0.1), Unit.NAC).toBigInteger();
        } else {
            return Amount.of(BigDecimal.valueOf(0.05), Unit.NAC).toBigInteger();
        }
    }


    public static TxContext<PayNacDTO> toPayNacContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<PayNacDTO>>() {
        });
    }


    public static TxContext<PayNacDTO> newPayNacContext(PayNacDTO payNacDTO) {

        TxContext txContext = new TxContext<PayNacDTO>().setInstanceType(InstanceType.DApp);

        txContext.setEventType(TxEventType.FULLNODE_PAYNAC);

        txContext.setReferrerInstance(CoreInstanceEnum.FNC.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.FNC.id);

        txContext.setData(payNacDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext<PayNomcDTO> toPayNomcContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<PayNomcDTO>>() {
        });
    }


    public static TxContext<PayNomcDTO> newPayNomcContext(PayNomcDTO payNomcDTO) {

        TxContext txContext = new TxContext<PayNomcDTO>().setInstanceType(InstanceType.DApp);

        txContext.setEventType(TxEventType.FULLNODE_PAYNOMC);

        txContext.setReferrerInstance(CoreInstanceEnum.FNC.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.FNC.id);

        txContext.setData(payNomcDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static List<NodeRole> applyNodeRoles() {
        return Lists.newArrayList(NodeRole.DPOS_FULLNODE, NodeRole.POWF_DATAFLOW, NodeRole.POWF_DNS);
    }


    public static TxContext toChangleContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<ChangeDTO>>() {
        });
    }


    public static TxContext newChangleContext(ChangeDTO changleDTO) {

        TxContext txContext = new TxContext<ChangeDTO>().setInstanceType(InstanceType.DApp);

        txContext.setEventType(TxEventType.FULLNODE_CHANGE);

        txContext.setReferrerInstance(CoreInstanceEnum.FNC.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.FNC.id);

        txContext.setData(changleDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext toAdminContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<AdminDTO>>() {
        });
    }


    public static TxContext newAdminContext(AdminDTO adminDTO) {

        TxContext txContext = new TxContext<AdminDTO>().setInstanceType(InstanceType.DApp);

        txContext.setEventType(TxEventType.FULLNODE_ADMIN);

        txContext.setReferrerInstance(CoreInstanceEnum.FNC.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.FNC.id);

        txContext.setData(adminDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext toEnabledContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<EnabledDTO>>() {
        });
    }


    public static TxContext newEnabledContext(EnabledDTO enabledDTO) {

        TxContext txContext = new TxContext<EnabledDTO>().setInstanceType(InstanceType.DApp);

        txContext.setEventType(TxEventType.FULLNODE_ENABLED);

        txContext.setReferrerInstance(CoreInstanceEnum.FNC.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.FNC.id);

        txContext.setData(enabledDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static String hashFullNodes(List<FullNode> fullNodeList) {

        String json = JsonUtils.objectToJson(fullNodeList);


        byte[] encodeHash = Hash.h256(json.getBytes());


        return Hex.encode0x(encodeHash);
    }


    public static FullNode get(String beneficiaryAddress) throws ExecutionException {
        FullNode fullNode = fullNodeDAO.find(beneficiaryAddress);
        return fullNode;
    }


    public static FullNode get(long fullNodeId) throws ExecutionException {
        IndexIdDAO indexIdDAO = new IndexIdDAO(CoreInstanceEnum.FNC.id);
        IndexId indexId = indexIdDAO.find(fullNodeDAO.getDbName(), fullNodeId);
        if (indexId != null) {
            String beneficiaryAddress = indexId.getIndexValue();
            return get(beneficiaryAddress);
        }

        return null;
    }


    public static boolean edit(FullNode fullNode) throws RocksDBException {
        return fullNodeDAO.edit(fullNode);
    }


    public static void offlineFullNode(String walletAddress) {

        try {
            FullNode fullnode = get(walletAddress);
            if (fullnode != null) {
                fullnode.setOnline(false);

                fullNodeDAO.edit(fullnode);
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public static void onlineFullNode(String walletAddress) {

        try {
            FullNode fullnode = get(walletAddress);
            if (fullnode != null) {
                fullnode.setOnline(true);
                fullnode.setPassExam(true);

                fullNodeDAO.edit(fullnode);
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean isOnline(String beneficiaryAddress) {
        try {
            FullNode fullNode = fullNodeDAO.find(beneficiaryAddress);
            if (fullNode != null) {
                if (fullNode.isEnabled() && fullNode.isPassExam() && fullNode.isOnline()) {

                    if (beneficiaryAddress.equals(ChainConfig.GENESIS_WALLET_ADDRESS)) {
                        return true;
                    }

                    if (fullNode.getVoteAmount().compareTo(BigInteger.ZERO) == 1) {
                        return true;
                    }
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        return false;
    }


    public static BigInteger sumPaidNac() {
        BigInteger sum = BigInteger.ZERO;
        List<FullNode> fullNodeList = fullNodeDAO.findAll();
        for (FullNode fullNode : fullNodeList) {
            sum = sum.add(fullNode.getPaidNac());
        }
        return sum;
    }


    public static BigInteger sumPaidNomc() {
        BigInteger sum = BigInteger.ZERO;
        List<FullNode> fullNodeList = fullNodeDAO.findAll();
        for (FullNode fullNode : fullNodeList) {
            sum = sum.add(fullNode.getPaidNomc());
        }
        return sum;
    }


}
