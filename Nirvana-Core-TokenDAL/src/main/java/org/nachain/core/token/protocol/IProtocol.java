package org.nachain.core.token.protocol;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NormalProtocol.class, name = "NormalProtocol"),
        @JsonSubTypes.Type(value = NFTProtocol.class, name = "NFTProtocol")
})
public interface IProtocol {


    void init();

}
