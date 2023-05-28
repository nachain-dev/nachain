package org.nachain.core.token.protocol;

import org.nachain.core.token.nft.NftContentTypeEnum;

import java.math.BigInteger;
import java.util.List;
import java.util.Properties;


public class NFTProtocolService {


    public final static String MAX_MINT_PER_ADDRESS = "MAX_MINT_PER_ADDRESS";


    public static NFTProtocol newNFTProtocol(String baseURI, String baseExt, NftContentTypeEnum nftContentTypeEnum, Properties properties, long mintTokenId, List<BigInteger> mintPrices, List<Long> mintPricesBatch, double royaltyPayment, String author) {

        NFTProtocol nftProtocol = new NFTProtocol();
        nftProtocol.setBaseURI(baseURI);
        nftProtocol.setBaseExt(baseExt);
        nftProtocol.setContentType(nftContentTypeEnum);
        nftProtocol.setProperties(properties);
        nftProtocol.setMintTokenId(mintTokenId);
        nftProtocol.setMintPrices(mintPrices);
        nftProtocol.setMintPricesBatch(mintPricesBatch);
        nftProtocol.setRoyaltyPayment(royaltyPayment);
        nftProtocol.setAuthor(author);

        return nftProtocol;
    }


    public static NFTProtocol newNFTProtocol(String baseURI, String baseExt, NftContentTypeEnum nftContentTypeEnum, long mintTokenId, List<BigInteger> mintPrices, List<Long> mintPricesBatch, double royaltyPayment, String author) {
        return newNFTProtocol(baseURI, baseExt, nftContentTypeEnum, new Properties(), mintTokenId, mintPrices, mintPricesBatch, royaltyPayment, author);
    }


    public static long mint(String to, String tokenURI) {
        long lastTokenId = 0;


        return lastTokenId;
    }

}
