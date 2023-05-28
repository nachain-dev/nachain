package org.nachain.core.dapp.internal.swap;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceService;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMarkService;
import org.nachain.core.dapp.internal.swap.amm.dto.SwapPairDeployDTO;
import org.nachain.core.token.Token;
import org.nachain.core.util.JsonUtils;


@Slf4j
public class SwapPairService {


    public static SwapPair newSwapPair(SwapType swapType, String baseTokenAddress, String quoteTokenAddress) {
        SwapPair swapQuote = new SwapPair(swapType, baseTokenAddress, quoteTokenAddress);
        swapQuote.setHash(swapQuote.encodeHashString());

        return swapQuote;
    }


    public static SwapPair newSwapPair(SwapType swapType, Token baseToken, Token quoteToken) {
        String baseAddress = InstanceService.toAppAddress(baseToken.getHash());
        String quoteAddress = InstanceService.toAppAddress(quoteToken.getHash());
        return newSwapPair(swapType, baseAddress, quoteAddress);
    }


    public static TxContext newSwapPairContext(SwapPairDeployDTO swapQuoteDTO) {

        TxContext txContext = new TxContext<SwapPairDeployDTO>().setInstanceType(InstanceType.DApp);

        txContext.setEventType(TxEventType.SWAP_ACTION);

        txContext.setReferrerInstance(CoreInstanceEnum.SWAP.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.SWAP.id);

        txContext.setData(swapQuoteDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext toCreateSwapPairContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<SwapPairDeployDTO>>() {
        });
    }


}
