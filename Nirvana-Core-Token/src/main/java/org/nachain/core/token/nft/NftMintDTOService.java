package org.nachain.core.token.nft;

import org.nachain.core.token.nft.dto.NftMintDTO;

public class NftMintDTOService {


    public static NftMintDTO newNftMintDTO(long instance, long token, long mintAmount) {
        NftMintDTO nftMintDTO = new NftMintDTO();
        nftMintDTO.setInstance(instance);
        nftMintDTO.setToken(token);
        nftMintDTO.setMintAmount(mintAmount);

        return nftMintDTO;
    }

}
