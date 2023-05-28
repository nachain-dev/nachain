package org.nachain.core.dapp.internal.swap;

import org.nachain.core.chain.instance.npp.InstanceNppService;
import org.nachain.core.crypto.Hash;
import org.nachain.core.util.Hex;


public class SwapPair {


    private SwapType swapType;


    private String pairName;

    private String baseTokenName;

    private String quoteTokenName;


    private String baseTokenAddress;


    private String quoteTokenAddress;


    private String hash;

    public SwapPair(SwapType swapType, String baseTokenAddress, String quoteTokenAddress) {

        String baseTokenSymbol = InstanceNppService.getTokenSymbol(baseTokenAddress);
        String quoteTokenSymbol = InstanceNppService.getTokenSymbol(quoteTokenAddress);

        this.swapType = swapType;
        this.pairName = baseTokenSymbol + "/" + quoteTokenSymbol;
        this.baseTokenAddress = baseTokenAddress;
        this.quoteTokenAddress = quoteTokenAddress;
    }

    public SwapType getSwapType() {
        return swapType;
    }

    public SwapPair setSwapType(SwapType swapType) {
        this.swapType = swapType;
        return this;
    }

    public String getPairName() {
        return pairName;
    }

    public SwapPair setPairName(String pairName) {
        this.pairName = pairName;
        return this;
    }

    public String getBaseTokenName() {
        return baseTokenName;
    }

    public SwapPair setBaseTokenName(String baseTokenName) {
        this.baseTokenName = baseTokenName;
        return this;
    }

    public String getQuoteTokenName() {
        return quoteTokenName;
    }

    public SwapPair setQuoteTokenName(String quoteTokenName) {
        this.quoteTokenName = quoteTokenName;
        return this;
    }

    public String getBaseTokenAddress() {
        return baseTokenAddress;
    }

    public SwapPair setBaseTokenAddress(String baseTokenAddress) {
        this.baseTokenAddress = baseTokenAddress;
        return this;
    }

    public String getQuoteTokenAddress() {
        return quoteTokenAddress;
    }

    public SwapPair setQuoteTokenAddress(String quoteTokenAddress) {
        this.quoteTokenAddress = quoteTokenAddress;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public SwapPair setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String toHashString() {
        return "SwapQuote{" +
                "swapType=" + swapType +
                ", swapName='" + pairName + '\'' +
                ", baseTokenName='" + baseTokenName + '\'' +
                ", quoteTokenName='" + quoteTokenName + '\'' +
                ", baseTokenAddress='" + baseTokenAddress + '\'' +
                ", quoteTokenAddress='" + quoteTokenAddress + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "SwapQuote{" +
                "swapType=" + swapType +
                ", swapName='" + pairName + '\'' +
                ", baseTokenName='" + baseTokenName + '\'' +
                ", quoteTokenName='" + quoteTokenName + '\'' +
                ", baseTokenAddress='" + baseTokenAddress + '\'' +
                ", quoteTokenAddress='" + quoteTokenAddress + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }


    public byte[] encodeHash() {

        return Hash.h256(this.toHashString().getBytes());
    }


    public String encodeHashString() {
        return Hex.encode0x(encodeHash());
    }

}
