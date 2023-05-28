package org.nachain.core.token.nft;

import com.google.common.collect.Lists;
import org.nachain.core.token.nft.dto.NftTransferDTO;

import java.util.List;

public class NftTransferDTOService {


    public static NftTransferDTO newNftTransferDTO(long instance, long token, long tokenId) {
        return newNftTransferDTO(instance, token, Lists.newArrayList(tokenId));
    }


    public static NftTransferDTO newNftTransferDTO(long instance, long token, List<Long> tokenIds) {
        NftTransferDTO nftTransferDTO = new NftTransferDTO();
        nftTransferDTO.setInstance(instance);
        nftTransferDTO.setToken(token);
        nftTransferDTO.setTokenIds(tokenIds);

        return nftTransferDTO;
    }

}
