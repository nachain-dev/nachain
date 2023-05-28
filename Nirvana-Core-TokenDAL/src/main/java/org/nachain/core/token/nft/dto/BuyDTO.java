package org.nachain.core.token.nft.dto;

import org.nachain.core.token.nft.collection.NftBuy;


public class BuyDTO extends NftBuy {
    @Override
    public String toString() {
        return "BuyDTO{" +
                "instance=" + instance +
                ", token=" + token +
                ", tokenId=" + tokenId +
                '}';
    }
}
