package org.nachain.core.token.nft.dto;

import org.nachain.core.token.nft.collection.NftSell;


public class SaleDTO extends NftSell {

    @Override
    public String toString() {
        return "SaleDTO{" +
                "instance=" + instance +
                ", token=" + token +
                ", tokenId=" + tokenId +
                '}';
    }
}
