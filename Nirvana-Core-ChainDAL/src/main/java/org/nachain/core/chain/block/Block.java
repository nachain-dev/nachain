package org.nachain.core.chain.block;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.chain.sign.IMinedSignObject;
import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


public class Block implements IBlock, IMinedSignObject {


    private long instance;


    private long height;


    private long timestamp;


    private String miner;


    private BigInteger blockReward;


    private BigInteger collectMined;


    private long size;


    private String parentHash;


    private int version;


    private String transactionsRoot;


    private long txVolume;


    private BigInteger gasUsed;


    private BigInteger gasLimit;


    private BigInteger gasMinimum;


    private BigInteger gasMaximum;


    private BlockExtraData extraData;


    private String hash;


    private String minedSign;

    public long getInstance() {
        return instance;
    }

    public Block setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getHeight() {
        return height;
    }

    public Block setHeight(long height) {
        this.height = height;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Block setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getMiner() {
        return miner;
    }

    public Block setMiner(String miner) {
        this.miner = miner;
        return this;
    }

    public BigInteger getBlockReward() {
        return blockReward;
    }

    public Block setBlockReward(BigInteger blockReward) {
        this.blockReward = blockReward;
        return this;
    }

    public BigInteger getCollectMined() {
        return collectMined;
    }

    public Block setCollectMined(BigInteger collectMined) {
        this.collectMined = collectMined;
        return this;
    }

    public long getSize() {
        return size;
    }

    public Block setSize(long size) {
        this.size = size;
        return this;
    }

    public String getParentHash() {
        return parentHash;
    }

    public Block setParentHash(String parentHash) {
        this.parentHash = parentHash;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public Block setVersion(int version) {
        this.version = version;
        return this;
    }

    public String getTransactionsRoot() {
        return transactionsRoot;
    }

    public Block setTransactionsRoot(String transactionsRoot) {
        this.transactionsRoot = transactionsRoot;
        return this;
    }

    public long getTxVolume() {
        return txVolume;
    }

    public Block setTxVolume(long txVolume) {
        this.txVolume = txVolume;
        return this;
    }

    public BigInteger getGasUsed() {
        return gasUsed;
    }

    public Block setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
        return this;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public Block setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
        return this;
    }

    public BigInteger getGasMinimum() {
        return gasMinimum;
    }

    public Block setGasMinimum(BigInteger gasMinimum) {
        this.gasMinimum = gasMinimum;
        return this;
    }

    public BigInteger getGasMaximum() {
        return gasMaximum;
    }

    public Block setGasMaximum(BigInteger gasMaximum) {
        this.gasMaximum = gasMaximum;
        return this;
    }

    public BlockExtraData getExtraData() {
        return extraData;
    }

    public Block setExtraData(BlockExtraData extraData) {
        this.extraData = extraData;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public Block setHash(String hash) {
        this.hash = hash;
        return this;
    }

    @Override
    public String getMinedSign() {
        return minedSign;
    }

    public Block setMinedSign(String minedSign) {
        this.minedSign = minedSign;
        return this;
    }

    @Override
    public String toHashString() {
        return "Block{" +
                "instance=" + instance +
                "height=" + height +
                ", timestamp=" + timestamp +
                ", miner='" + miner + '\'' +
                ", blockReward=" + blockReward +
                ", collectMined=" + collectMined +
                ", size=" + size +
                ", parentHash='" + parentHash + '\'' +
                ", version=" + version +
                ", transactionsRoot='" + transactionsRoot + '\'' +
                ", txVolume=" + txVolume +
                ", gasUsed=" + gasUsed +
                ", gasLimit=" + gasLimit +
                ", gasMinimum=" + gasMinimum +
                ", gasMaximum=" + gasMaximum +
                ", extraData='" + extraData + '\'' +
                '}';
    }

    @Override
    public String toMinedSignString() {
        return "Block{" +
                "instance=" + instance +
                "height=" + height +
                ", timestamp=" + timestamp +
                ", miner='" + miner + '\'' +
                ", blockReward=" + blockReward +
                ", collectMined=" + collectMined +
                ", size=" + size +
                ", parentHash='" + parentHash + '\'' +
                ", version=" + version +
                ", transactionsRoot='" + transactionsRoot + '\'' +
                ", txVolume=" + txVolume +
                ", gasUsed=" + gasUsed +
                ", gasLimit=" + gasLimit +
                ", gasMinimum=" + gasMinimum +
                ", gasMaximum=" + gasMaximum +
                ", extraData='" + extraData + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "Block{" +
                "instance=" + instance +
                ", height=" + height +
                ", timestamp=" + timestamp +
                ", miner='" + miner + '\'' +
                ", blockReward=" + blockReward +
                ", collectMined=" + collectMined +
                ", size=" + size +
                ", parentHash='" + parentHash + '\'' +
                ", version=" + version +
                ", transactionsRoot='" + transactionsRoot + '\'' +
                ", txVolume=" + txVolume +
                ", gasUsed=" + gasUsed +
                ", gasLimit=" + gasLimit +
                ", gasMinimum=" + gasMinimum +
                ", gasMaximum=" + gasMaximum +
                ", extraData=" + extraData +
                ", hash='" + hash + '\'' +
                ", minedSign='" + minedSign + '\'' +
                '}';
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }


    @Override
    public byte[] encodeHash() throws Exception {

        return Hash.h256(this.toHashString().getBytes());
    }


    @Override
    public String encodeHashString() throws Exception {
        return Hex.encode0x(encodeHash());
    }
}
