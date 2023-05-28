package org.nachain.core.chain.structure.instance;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.structure.instance.dto.InstanceDTO;
import org.nachain.core.token.Token;
import org.nachain.core.token.TokenService;
import org.nachain.core.util.JsonUtils;
import org.nachain.core.wallet.WalletUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class InstanceService {

    private static InstanceDAO instanceDAO;

    static {
        instanceDAO = new InstanceDAO();
    }


    public static Instance newInstance(String txHash, String appName, String appVersion, String symbol, String info, String author, InstanceType instanceType, String minedTokenHash, List<String> relatedTokensHash, Object data) throws Exception {
        Instance instance = new Instance();
        instance.setId(0);
        instance.setTx(txHash);
        instance.setAppName(appName);
        instance.setAppVersion(appVersion);
        instance.setAppAddress(toAppAddress(txHash));
        instance.setSymbol(symbol);
        instance.setInfo(info);
        instance.setAuthor(author);
        instance.setInstanceType(instanceType);
        instance.setMinedTokenHash(minedTokenHash);
        instance.setRelatedTokensHash(relatedTokensHash);
        instance.setData(JsonUtils.objectToJson(data));
        instance.setHash(instance.encodeHashString());

        return instance;
    }


    public static Instance newInstance(InstanceDTO instanceDTO, String txHash, String author) throws Exception {
        Instance instance = new Instance();
        instance.setId(0);
        instance.setTx(txHash);
        instance.setAppName(instanceDTO.getAppName());
        instance.setAppVersion(instanceDTO.getAppVersion());
        instance.setAppAddress(toAppAddress(txHash));
        instance.setSymbol(instanceDTO.getSymbol());
        instance.setInfo(instanceDTO.getInfo());
        instance.setAuthor(author);
        instance.setInstanceType(instanceDTO.getInstanceType());
        instance.setMinedTokenHash(instanceDTO.getMinedTokenHash());
        instance.setRelatedTokensHash(instanceDTO.getRelatedTokensHash());
        instance.setData(instanceDTO.getData());
        instance.setHash(instance.encodeHashString());

        return instance;
    }


    public static void removeExistInstance(long instanceIndex) {

        Instance existInstance = InstanceSingleton.get().getInstance(instanceIndex);
        if (existInstance != null) {

            InstanceSingleton.get().remove(existInstance.getHash());
        }
    }


    public static Instance find(String instanceName) {
        List<Instance> instances = instanceDAO.findAll();
        for (Instance instance : instances) {
            if (instance.getAppName().equals(instanceName)) {
                return instance;
            }
        }

        return null;
    }


    public static void sortByInstanceId(List<Instance> instanceList) {
        Collections.sort(instanceList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Instance t1 = (Instance) o1;
                Instance t2 = (Instance) o2;
                return Long.valueOf(t1.getId()).compareTo(Long.valueOf(t2.getId()));
            }
        });
    }


    public static String toAppAddress(String txHash) {
        return WalletUtils.generateDAppAddress(txHash.getBytes());
    }


    public static boolean isNftToken(long instanceId) {
        Instance instance = InstanceSingleton.get().getInstance(instanceId);


        if (instance.getInstanceType() == InstanceType.Token) {

            Token token = JsonUtils.jsonToPojo(instance.getData(), Token.class);
            return TokenService.isNft(token);
        }

        return false;
    }


}
