package org.nachain.core.chain.structure.instance.dto;

import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.util.JsonUtils;

import java.util.List;

public class InstanceDTOService {


    public static InstanceDTO newInstanceDTO(String appName, String appVersion, String symbol, String info, InstanceType instanceType, String minedTokenHash, List<String> relatedTokensHash, Object data) {
        InstanceDTO instanceDTO = new InstanceDTO();
        instanceDTO.setAppName(appName);
        instanceDTO.setAppVersion(appVersion);
        instanceDTO.setSymbol(symbol);
        instanceDTO.setInfo(info);
        instanceDTO.setInstanceType(instanceType);
        instanceDTO.setMinedTokenHash(minedTokenHash);
        instanceDTO.setRelatedTokensHash(relatedTokensHash);
        instanceDTO.setData(JsonUtils.objectToJson(data));

        return instanceDTO;
    }

}
