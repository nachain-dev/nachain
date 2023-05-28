package org.nachain.core.chain.transaction.context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.nachain.core.chain.instruction.InstructionSet;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.Instance;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.structure.instance.dto.InstanceDTO;
import org.nachain.core.token.Token;
import org.nachain.core.token.TokenIconDTO;
import org.nachain.core.token.TokenIconDTOService;
import org.nachain.core.token.nft.NftMintDTOService;
import org.nachain.core.token.nft.NftTransferDTOService;
import org.nachain.core.token.nft.dto.NftMaxMintDTO;
import org.nachain.core.token.nft.dto.NftMintDTO;
import org.nachain.core.token.nft.dto.NftMintedDTO;
import org.nachain.core.token.nft.dto.NftTransferDTO;
import org.nachain.core.util.JsonUtils;

import java.util.List;

public class TxContextService {


    public static TxContext newEmptyContext(long instance) {
        return new TxContext();
    }


    public static TxContext toTxContext(String json) {
        return JsonUtils.jsonToPojo(json, TxContext.class);
    }


    public static TxContext newMinedTokenContext(long instance) {

        TxContext txContext = new TxContext().setInstanceType(InstanceType.Token);

        txContext.setEventType(TxEventType.TOKEN_MINED);

        txContext.setReferrerInstance(instance).setReferrerTx("");

        txContext.setCrossToInstance(instance);

        txContext.setData("");

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext newTransferContext(long instance) {

        TxContext txContext = new TxContext().setInstanceType(InstanceType.Token);

        txContext.setEventType(TxEventType.TOKEN_TRANSFER);

        txContext.setReferrerInstance(instance).setReferrerTx("");

        txContext.setCrossToInstance(instance);

        txContext.setData("");

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext newTokenIconContext(long instance, long tokenId, String logoUrl, String logoBase64) {
        return newTokenIconContext(instance, TokenIconDTOService.newTokenIconDTO(tokenId, logoUrl, logoBase64));
    }


    public static TxContext newTokenIconContext(long instance, TokenIconDTO tokenIconDTO) {

        TxContext txContext = new TxContext().setInstanceType(InstanceType.Token);

        txContext.setEventType(TxEventType.TOKEN_ICON);

        txContext.setReferrerInstance(instance).setReferrerTx("");

        txContext.setCrossToInstance(instance);

        txContext.setData(tokenIconDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext newNftTransferContext(long instance, long token, List<Long> tokenIds) {

        TxContext txContext = new TxContext().setInstanceType(InstanceType.Token);

        txContext.setEventType(TxEventType.TOKEN_NFT_TRANSFER);

        txContext.setReferrerInstance(instance).setReferrerTx("");

        txContext.setCrossToInstance(instance);

        txContext.setData(NftTransferDTOService.newNftTransferDTO(instance, token, tokenIds));

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext newNftTransferContext(long instance, long token, long tokenId) {
        return newNftTransferContext(instance, token, Lists.newArrayList(tokenId));
    }


    public static TxContext toNftTransferContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<NftTransferDTO>>() {
        });
    }


    public static TxContext toTokenIconContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<TokenIconDTO>>() {
        });
    }


    public static TxContext newNftMintContext(long instance, long token, long mintAmount) {

        TxContext txContext = new TxContext().setInstanceType(InstanceType.Token);

        txContext.setEventType(TxEventType.TOKEN_NFT_MINT);

        txContext.setReferrerInstance(instance).setReferrerTx("");

        txContext.setCrossToInstance(instance);

        txContext.setData(NftMintDTOService.newNftMintDTO(instance, token, mintAmount));

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext toNftMintContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<NftMintDTO>>() {
        });
    }


    public static TxContext newNftMintedContext(NftMintedDTO nftMintedDTO) {

        TxContext txContext = new TxContext().setInstanceType(InstanceType.Token);

        txContext.setEventType(TxEventType.TOKEN_NFT_MINTED);

        txContext.setReferrerInstance(nftMintedDTO.getInstance()).setReferrerTx("");

        txContext.setCrossToInstance(nftMintedDTO.getInstance());

        txContext.setData(nftMintedDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext toNftMintedContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<NftMintedDTO>>() {
        });
    }


    public static TxContext newNftMaxMintContext(NftMaxMintDTO nftMaxMintDTO) {

        TxContext txContext = new TxContext().setInstanceType(InstanceType.Token);

        txContext.setEventType(TxEventType.TOKEN_NFT_MAXMINT);

        txContext.setReferrerInstance(nftMaxMintDTO.getInstance()).setReferrerTx("");

        txContext.setCrossToInstance(nftMaxMintDTO.getInstance());

        txContext.setData(nftMaxMintDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext toNftMaxMintContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<NftMaxMintDTO>>() {
        });
    }


    public static TxContext newCrossOutContext(TxEventType eventType, Object contextData) {

        TxContext txContext = new TxContext().setInstanceType(InstanceType.Token);

        txContext.setEventType(eventType);

        txContext.setReferrerInstance(CoreInstanceEnum.NULL.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.NULL.id);

        txContext.setData(contextData);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext newCrossInContext(long referrerInstance, String referrerTx, long toInstance, TxEventType eventType, Object data) {

        TxContext txContext = new TxContext().setInstanceType(InstanceType.Token);

        txContext.setEventType(eventType);

        txContext.setReferrerInstance(referrerInstance).setReferrerTx(referrerTx);

        txContext.setCrossToInstance(toInstance);

        txContext.setData(data);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext newInstallContext(InstanceDTO instanceDTO) {

        TxEventType txEventType = null;
        switch (instanceDTO.getInstanceType()) {
            case Core:
                txEventType = TxEventType.CORE_INSTALL;
                break;
            case Token:
                txEventType = TxEventType.TOKEN_INSTALL;
                break;
            case DApp:
                txEventType = TxEventType.DAPP_INSTALL;
                break;
            case DWeb:
                txEventType = TxEventType.DWEB_INSTALL;
                break;
            case DContract:
                txEventType = TxEventType.DCONTRACT_INSTALL;
                break;
        }


        TxContext txContext = new TxContext<Instance>().setInstanceType(instanceDTO.getInstanceType());

        txContext.setEventType(txEventType);

        txContext.setReferrerInstance(CoreInstanceEnum.NULL.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.NULL.id);

        txContext.setData(instanceDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext<Token> toTokenTxContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<Token>>() {
        });
    }


    public static TxContext<Instance> toInstanceTxContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<Instance>>() {
        });
    }


    public static TxContext<InstanceDTO> toInstanceDTOTxContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<InstanceDTO>>() {
        });
    }


    public static TxContext newInstructionSetContext(InstructionSet instructionSet) {

        TxContext txContext = new TxContext().setInstanceType(InstanceType.Core);

        txContext.setEventType(TxEventType.INSTRUCTION_SET);

        txContext.setReferrerInstance(CoreInstanceEnum.NULL.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.NULL.id);

        txContext.setData(instructionSet);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static TxContext<InstructionSet> toInstructionSetTxContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<InstructionSet>>() {
        });
    }


}
