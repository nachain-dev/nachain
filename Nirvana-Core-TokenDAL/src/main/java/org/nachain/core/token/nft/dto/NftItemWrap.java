package org.nachain.core.token.nft.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.nachain.core.token.nft.NftItem;
import org.nachain.core.token.nft.NftItemDetail;

@Data
@NoArgsConstructor
public class NftItemWrap {

    private long nftItemId;

    private NftItem nftItem;

    private NftItemDetail nftItemDetail;
}
