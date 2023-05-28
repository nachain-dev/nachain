package org.nachain.core.dapp.internal.dao.promotion;

import com.fasterxml.jackson.core.type.TypeReference;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMarkService;
import org.nachain.core.dapp.internal.dao.promotion.events.PromotionDTO;
import org.nachain.core.util.JsonUtils;

public class PromotionService {


    public static TxContext toPromotionContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<PromotionDTO>>() {
        });
    }


    public static TxContext newPromotionContext(PromotionDTO promotionDTO) {

        TxContext txContext = new TxContext<PromotionDTO>().setInstanceType(InstanceType.Core);

        txContext.setEventType(TxEventType.DAO_PROMOTION);

        txContext.setReferrerInstance(CoreInstanceEnum.NAC.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.NAC.id);

        txContext.setData(promotionDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }

}
