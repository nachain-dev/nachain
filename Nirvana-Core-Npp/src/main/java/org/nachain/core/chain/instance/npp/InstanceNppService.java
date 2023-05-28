package org.nachain.core.chain.instance.npp;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.base.Amount;
import org.nachain.core.base.Unit;
import org.nachain.core.chain.config.IndexConfigDAO;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.structure.instance.*;
import org.nachain.core.chain.structure.instance.dto.InstanceDTO;
import org.nachain.core.chain.structure.instance.dto.InstanceDTOService;
import org.nachain.core.config.ChainConfig;
import org.nachain.core.intermediate.AccountDeployService;
import org.nachain.core.token.*;
import org.nachain.core.token.nft.NftCollectionService;
import org.nachain.core.token.nft.collection.NftCollection;
import org.nachain.core.token.nft.collection.NftCollectionDAO;
import org.nachain.core.token.protocol.NFTProtocol;
import org.nachain.core.token.protocol.NormalProtocol;
import org.nachain.core.token.protocol.NormalProtocolService;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class InstanceNppService {


    public static List<InstanceDTO> getCoreInstanceDTO() {

        NormalProtocol normalProtocol = NormalProtocolService.newNormalProtocol();


        final String author = ChainConfig.GENESIS_WALLET_ADDRESS;


        int tokenIndex = 1;

        Token nacToken = TokenService.newToken("Nirvana Coin", "NAC", "Nirvana Coin", Amount.of("75000000", Unit.NAC), Amount.of("9075000", Unit.NAC), Amount.of("1655280", Unit.NAC), ChainConfig.GENESIS_WALLET_ADDRESS, Unit.NAC.exp, TokenTypeEnum.KERNEL, TokenProtocolEnum.NORMAL, normalProtocol);

        Token nomcToken = TokenService.newToken("Nomc Token", "NOMC", "Nirvana operators maintenance chain", Amount.of("35000", Unit.NAC), Amount.of("35000", Unit.NAC), Amount.of("0", Unit.NAC), ChainConfig.GENESIS_WALLET_ADDRESS, Unit.NAC.exp, TokenTypeEnum.KERNEL, TokenProtocolEnum.NORMAL, normalProtocol);

        Token usdnToken = TokenService.newToken("USDN Token", "USDN", "Nirvana USD", Amount.of("100000000000000", Unit.NAC), Amount.of("100000000000000", Unit.NAC), Amount.of("0", Unit.NAC), ChainConfig.GENESIS_WALLET_ADDRESS, Unit.NAC.exp, TokenTypeEnum.KERNEL, TokenProtocolEnum.NORMAL, normalProtocol);

        nacToken.setId(tokenIndex++);
        nomcToken.setId(tokenIndex++);
        usdnToken.setId(tokenIndex++);


        int coreIndex = 1;
        Core appChainCore = CoreService.newCore("App Chain", "1.0", author);
        Core fullNodeCore = CoreService.newCore("Full Node", "1.0", author);
        Core superNodeCore = CoreService.newCore("Super Node", "1.0", author);
        Core swapCore = CoreService.newCore("Swap", "1.0", author);

        appChainCore.setId(coreIndex++);
        fullNodeCore.setId(coreIndex++);
        superNodeCore.setId(coreIndex++);
        swapCore.setId(coreIndex++);


        InstanceDTO appInstance = InstanceDTOService.newInstanceDTO("App Chain (PoWF)", "1.0", "AC", "Application Chain", InstanceType.Core, nacToken.getHash(), Lists.newArrayList(nacToken.getHash()), appChainCore);

        InstanceDTO nacInstance = InstanceDTOService.newInstanceDTO("NAC (DPoS)", "1.0", "NAC", "Nirvana Coin", InstanceType.Token, nacToken.getHash(), Lists.newArrayList(nacToken.getHash()), nacToken);

        InstanceDTO nomcInstance = InstanceDTOService.newInstanceDTO("NOMC (DPoS)", "1.0", "NOMC", "Nirvana operators maintenance chain", InstanceType.Token, nomcToken.getHash(), Lists.newArrayList(nomcToken.getHash()), nomcToken);

        InstanceDTO usdnInstance = InstanceDTOService.newInstanceDTO("USDN (DPoS)", "1.0", "USDN", "USD Nirvana", InstanceType.Token, usdnToken.getHash(), Lists.newArrayList(usdnToken.getHash()), usdnToken);

        InstanceDTO fullNodeInstance = InstanceDTOService.newInstanceDTO("Full Node (DPoS)", "1.0", "FNC", "Full node credential", InstanceType.Core, "", Lists.newArrayList(nacToken.getHash(), nomcToken.getHash()), fullNodeCore);

        InstanceDTO superNodeInstance = InstanceDTOService.newInstanceDTO("VOTE (DPoS)", "1.0", "VOTE", "Vote Super Node", InstanceType.Core, "", Lists.newArrayList(nacToken.getHash()), superNodeCore);

        InstanceDTO swapInstance = InstanceDTOService.newInstanceDTO("SWAP (DPoS)", "1.0", "SWAP", "Swap", InstanceType.Core, "", Lists.newArrayList(), swapCore);


        List<InstanceDTO> instances = Arrays.asList(appInstance, nacInstance, nomcInstance, usdnInstance, fullNodeInstance, superNodeInstance, swapInstance);

        return instances;
    }


    public static AbstractInstanceNpp getInstanceApp(Instance instance) {
        switch (instance.getInstanceType()) {
            case Core:

                Core core = JsonUtils.jsonToPojo(instance.getData(), Core.class);
                return core;
            case Token:

                Token token = JsonUtils.jsonToPojo(instance.getData(), Token.class);
                return token;
            case DApp:

                DApp dApp = JsonUtils.jsonToPojo(instance.getData(), DApp.class);
                return dApp;
            case DWeb:

                DWeb dWeb = JsonUtils.jsonToPojo(instance.getData(), DWeb.class);
                return dWeb;
            case DContract:

                DContract dContract = JsonUtils.jsonToPojo(instance.getData(), DContract.class);
                return dContract;
            default:
                return null;
        }
    }


    public static String getInstanceAppHash(Instance instance) {
        AbstractInstanceNpp abstractInstanceNpp = getInstanceApp(instance);
        if (abstractInstanceNpp == null) {
            throw new ChainException("getInstanceApp(instance) is null. %s", instance);
        }

        return abstractInstanceNpp.getHash();
    }


    public static Token getToken(long instanceId) {

        Instance instance = InstanceSingleton.get().getInstance(instanceId);
        if (instance == null) {
            throw new ChainException("Instance object is null, instanceId=%d", instanceId);
        }

        Token token;
        if (instance.getInstanceType() == InstanceType.Token) {
            token = Token.toToken(instance.getData());
        } else {

            if (CoreInstanceEnum.APPCHAIN.id == instanceId) {
                token = TokenSingleton.get().get(CoreTokenEnum.NAC.id);
            } else {
                token = TokenSingleton.get().get(CoreTokenEnum.NULL.id);
            }
        }

        return token;
    }


    public static String getTokenSymbol(String tokenAddress) {
        long instanceId = InstanceSingleton.get().getInstanceIdByAppAddressMap().get(tokenAddress);
        Token token = getToken(instanceId);
        return token.getSymbol();
    }


    public static Core getCore(long instanceId) {
        Instance instance = InstanceSingleton.get().getInstance(instanceId);
        Core core = Core.toCore(instance.getData());
        return core;
    }


    public static DContract getDContract(long instanceId) {
        Instance instance = InstanceSingleton.get().getInstance(instanceId);
        DContract dContract = DContract.toDContract(instance.getData());
        return dContract;
    }


    public static DWeb getDWeb(long instanceId) {
        Instance instance = InstanceSingleton.get().getInstance(instanceId);
        DWeb toDWeb = DWeb.toDWeb(instance.getData());
        return toDWeb;
    }


    public static DApp getDApp(long instanceId) {
        Instance instance = InstanceSingleton.get().getInstance(instanceId);
        DApp dApp = DApp.toDApp(instance.getData());
        return dApp;
    }


    public static AbstractInstanceNpp deployInstance(Instance instance, long deployBlockHeight) throws RocksDBException, IOException, ExecutionException {
        IndexConfigDAO indexConfigDAO = new IndexConfigDAO();
        InstanceDetailDAO instanceDetailDAO = new InstanceDetailDAO();


        long instanceIndex = indexConfigDAO.nextInstanceAppHeight();

        instance.setId(instanceIndex);


        removeTempInstanceNpp(instanceIndex);


        AbstractInstanceNpp instanceApp = deployApp(instance);


        AccountDeployService.addDeployInstance(instance.getAuthor(), instance.getId());


        InstanceService.removeExistInstance(instanceIndex);


        InstanceSingleton.get().addToMap(instance);


        indexConfigDAO.saveInstanceAppHeight(instanceIndex);


        InstanceDetail instanceDetail = InstanceDetailService.newInstanceDetail(instanceIndex, deployBlockHeight, 0);

        instanceDetailDAO.put(instanceDetail);


        log.debug("deployInstance() -> instance={}, instanceDetail={}", instance, instanceDetail);

        return instanceApp;
    }


    private static void removeTempInstanceNpp(long instanceId) {

        Instance tempInstance = InstanceSingleton.get().getInstance(instanceId);
        if (tempInstance != null) {

            switch (tempInstance.getInstanceType()) {
                case Core:
                    break;
                case Token:

                    Token token = TokenSingleton.get().getByInstanceId(tempInstance.getId());
                    TokenSingleton.get().remove(token.getHash());
                    break;
                case DApp:
                    break;
                case DWeb:
                    break;
                case DContract:
                    break;
            }

        }

    }


    private static AbstractInstanceNpp deployApp(Instance instance) throws RocksDBException, IOException, ExecutionException {

        InstanceDAO instanceDAO = new InstanceDAO();
        IndexConfigDAO indexConfigDAO = new IndexConfigDAO();

        CoreDAO coreDAO = new CoreDAO();
        TokenDAO tokenDAO = new TokenDAO();
        DAppDAO dAppDAO = new DAppDAO();
        DWebDAO dWebDAO = new DWebDAO();
        DContractDAO dContractDAO = new DContractDAO();


        long instanceAppId = indexConfigDAO.nextInstanceAppHeight(instance.getInstanceType());


        AbstractInstanceNpp aInstance = null;
        switch (instance.getInstanceType()) {
            case Core:

                Core core = JsonUtils.jsonToPojo(instance.getData(), Core.class);
                core.setId(instanceAppId);
                core.setInstanceId(instance.getId());


                CoreService.verifyCore(core);


                coreDAO.add(core);

                instance.setData(core.toJson());
                aInstance = core;
                break;
            case Token:

                Token token = JsonUtils.jsonToPojo(instance.getData(), Token.class);
                token.setId(instanceAppId);
                token.setInstanceId(instance.getId());
                token.setAuthor(instance.getAuthor());


                TokenService.verifyDeployToken(token);


                tokenDAO.add(token);


                instance.setData(token.toJson());
                aInstance = token;


                TokenSingleton.get().addToMap(token);


                switch (token.getTokenProtocol()) {
                    case NORMAL:


                        break;
                    case NFT:
                        NftCollectionDAO nftCollectionDAO = new NftCollectionDAO(token.getInstanceId());

                        NFTProtocol nftProtocol = (NFTProtocol) token.getProtocol();

                        NftCollection nftCollection = NftCollectionService.newNftCollection(token, nftProtocol);

                        nftCollectionDAO.add(nftCollection);
                        break;
                    default:
                        break;
                }

                break;
            case DApp:

                DApp dApp = JsonUtils.jsonToPojo(instance.getData(), DApp.class);
                dApp.setId(instanceAppId);
                dApp.setInstanceId(instance.getId());

                dAppDAO.add(dApp);

                instance.setData(dApp.toJson());
                aInstance = dApp;


                break;
            case DWeb:

                DWeb dWeb = JsonUtils.jsonToPojo(instance.getData(), DWeb.class);
                dWeb.setId(instanceAppId);
                dWeb.setInstanceId(instance.getId());

                dWebDAO.add(dWeb);

                instance.setData(dWeb.toJson());
                aInstance = dWeb;

                break;
            case DContract:

                DContract dContract = JsonUtils.jsonToPojo(instance.getData(), DContract.class);
                dContract.setId(instanceAppId);
                dContract.setInstanceId(instance.getId());

                dContractDAO.add(dContract);

                instance.setData(dContract.toJson());
                aInstance = dContract;

                break;
        }


        instanceDAO.put(instance);


        indexConfigDAO.saveInstanceAppHeight(instance.getInstanceType(), aInstance.getId());


        CoreInstanceEnum.updateId(instance.getAppName(), instance.getId());

        return aInstance;
    }


}
