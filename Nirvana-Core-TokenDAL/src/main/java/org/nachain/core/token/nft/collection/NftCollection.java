package org.nachain.core.token.nft.collection;

import org.nachain.core.token.protocol.NFTProtocol;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;
import java.util.List;


public class NftCollection {


    private long instance;


    private long token;


    private NFTProtocol nftProtocol;


    private boolean enabledMint;


    private boolean enabledWhiteListMint;


    private boolean enabledSingleMint;


    private List<String> whiteList;


    private BigInteger floorPrice = BigInteger.ZERO;


    private long mintAmount = 0;


    private long maxMintAmount = 0;

    public NftCollection() {
    }


    public NftCollection(long instance, long token) {
        this.instance = instance;
        this.token = token;
    }

    public long getInstance() {
        return instance;
    }

    public NftCollection setInstance(long instance) {
        this.instance = instance;
        return this;
    }

    public long getToken() {
        return token;
    }

    public NftCollection setToken(long token) {
        this.token = token;
        return this;
    }

    public boolean isEnabledMint() {
        return enabledMint;
    }

    public NftCollection setEnabledMint(boolean enabledMint) {
        this.enabledMint = enabledMint;
        return this;
    }

    public boolean isEnabledWhiteListMint() {
        return enabledWhiteListMint;
    }

    public NftCollection setEnabledWhiteListMint(boolean enabledWhiteListMint) {
        this.enabledWhiteListMint = enabledWhiteListMint;
        return this;
    }

    public boolean isEnabledSingleMint() {
        return enabledSingleMint;
    }

    public NftCollection setEnabledSingleMint(boolean enabledSingleMint) {
        this.enabledSingleMint = enabledSingleMint;
        return this;
    }

    public List<String> getWhiteList() {
        return whiteList;
    }

    public NftCollection setWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
        return this;
    }

    public BigInteger getFloorPrice() {
        return floorPrice;
    }

    public NftCollection setFloorPrice(BigInteger floorPrice) {
        this.floorPrice = floorPrice;
        return this;
    }

    public long getMintAmount() {
        return mintAmount;
    }

    public NftCollection setMintAmount(long mintAmount) {
        this.mintAmount = mintAmount;
        return this;
    }

    public long getMaxMintAmount() {
        return maxMintAmount;
    }

    public NftCollection setMaxMintAmount(long maxMintAmount) {
        this.maxMintAmount = maxMintAmount;
        return this;
    }

    public NFTProtocol getNftProtocol() {
        return nftProtocol;
    }

    public NftCollection setNftProtocol(NFTProtocol nftProtocol) {
        this.nftProtocol = nftProtocol;
        return this;
    }

    @Override
    public String toString() {
        return "NftCollection{" +
                "instance=" + instance +
                ", token=" + token +
                ", nftProtocol=" + nftProtocol +
                ", enabledMint=" + enabledMint +
                ", enabledWhiteListMint=" + enabledWhiteListMint +
                ", enabledSingleMint=" + enabledSingleMint +
                ", whiteList=" + whiteList +
                ", floorPrice=" + floorPrice +
                ", mintAmount=" + mintAmount +
                ", maxMintAmount=" + maxMintAmount +
                '}';
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }


    public String toKey(NftSell nftSell) {
        return String.format("%d_%d_%d", nftSell.getInstance(), nftSell.getToken(), nftSell.getTokenId());
    }

    public String toKey(NftBuy nftBuy) {
        return String.format("%d_%d_%d", nftBuy.getInstance(), nftBuy.getToken(), nftBuy.getTokenId());
    }


    public long addMintAmount(long amount) {
        mintAmount = mintAmount + amount;

        return mintAmount;
    }


    public static String getActivityName(long instance, long token) {
        return String.format("%s?instance=%d&token=%d", "NftActivity", instance, token);
    }

}
