package org.nachain.core.token.protocol;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.nachain.core.token.TokenProtocolEnum;
import org.nachain.core.token.nft.NftContentTypeEnum;

import java.math.BigInteger;
import java.util.List;
import java.util.Properties;


@JsonTypeName("NFTProtocol")
public class NFTProtocol extends AbstractProtocol {


    NftContentTypeEnum ContentType;


    String baseURI;


    String baseExt;


    Properties properties;


    long mintTokenId;


    List<BigInteger> mintPrices;


    List<Long> mintPricesBatch;


    double royaltyPayment;


    String author;

    @Override
    public void init() {
        this.setProtocolName(TokenProtocolEnum.NFT.symbol);
        this.setProtocolVersion("1.0");
        this.setAllowDecimal(false);
        if (properties == null) {
            properties = new Properties();
        }

    }

    public NftContentTypeEnum getContentType() {
        return ContentType;
    }

    public NFTProtocol setContentType(NftContentTypeEnum contentType) {
        ContentType = contentType;
        return this;
    }

    public String getBaseURI() {
        return baseURI;
    }

    public NFTProtocol setBaseURI(String baseURI) {
        this.baseURI = baseURI;
        return this;
    }

    public String getBaseExt() {
        return baseExt;
    }

    public NFTProtocol setBaseExt(String baseExt) {
        this.baseExt = baseExt;
        return this;
    }

    public Properties getProperties() {
        return properties;
    }

    public NFTProtocol setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }

    public List<BigInteger> getMintPrices() {
        return mintPrices;
    }

    public NFTProtocol setMintPrices(List<BigInteger> mintPrices) {
        this.mintPrices = mintPrices;
        return this;
    }

    public List<Long> getMintPricesBatch() {
        return mintPricesBatch;
    }

    public NFTProtocol setMintPricesBatch(List<Long> mintPricesBatch) {
        this.mintPricesBatch = mintPricesBatch;
        return this;
    }

    public double getRoyaltyPayment() {
        return royaltyPayment;
    }

    public NFTProtocol setRoyaltyPayment(double royaltyPayment) {
        this.royaltyPayment = royaltyPayment;
        return this;
    }

    public long getMintTokenId() {
        return mintTokenId;
    }

    public NFTProtocol setMintTokenId(long mintTokenId) {
        this.mintTokenId = mintTokenId;
        return this;
    }


    public String getAuthor() {
        return author;
    }

    public NFTProtocol setAuthor(String author) {
        this.author = author;
        return this;
    }


    public String getTokenURI(long tokenId) {
        return getBaseURI() + tokenId + getBaseExt();
    }


    public Properties putProperties(Object key, Object value) {
        if (properties == null) {
            properties = new Properties();
        }
        properties.put(key, value);

        return properties;
    }

    @Override
    public String toString() {
        return "NFTProtocol{" +
                "ContentType=" + ContentType +
                ", baseURI='" + baseURI + '\'' +
                ", baseExt='" + baseExt + '\'' +
                ", properties=" + properties +
                ", mintTokenId=" + mintTokenId +
                ", mintPrices=" + mintPrices +
                ", mintPricesBatch=" + mintPricesBatch +
                ", royaltyPayment=" + royaltyPayment +
                ", author='" + author + '\'' +
                ", protocolName='" + protocolName + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                ", isAllowDecimal=" + isAllowDecimal +
                '}';
    }


    public long countAmount() {
        long total = 0;
        for (long amount : mintPricesBatch) {
            total = total + amount;
        }
        return total;
    }


    public long sumBatchMintTotal() {
        long sumBatchMintTotal = getMintPricesBatch().stream().mapToLong(Long::longValue).sum();
        return sumBatchMintTotal;
    }

}
