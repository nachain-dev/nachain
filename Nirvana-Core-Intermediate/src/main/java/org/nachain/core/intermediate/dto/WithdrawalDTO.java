package org.nachain.core.intermediate.dto;

import lombok.Data;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


@Data
public class WithdrawalDTO {


    BigInteger amount;


    public static WithdrawalDTO to(String json) {
        return JsonUtils.jsonToPojo(json, WithdrawalDTO.class);
    }


    public String toJson() {
        return JsonUtils.objectToJson(this);
    }
}
