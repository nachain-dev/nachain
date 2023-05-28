package org.nachain.core.token.nft.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.nachain.core.token.nft.collection.NftCollection;
import org.nachain.core.token.nft.collection.NftCollectionDetail;

@Data
@NoArgsConstructor
public class NftCollWrap {

    private long nftCollInstanceId;

    private long nftCollTokenId;

    private NftCollection nftCollection;

    private NftCollectionDetail nftCollectionDetail;
}
