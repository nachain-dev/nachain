package org.nachain.core.token.protocol;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.nachain.core.token.TokenProtocolEnum;


@JsonTypeName("NormalProtocol")
public class NormalProtocol extends AbstractProtocol {

    @Override
    public void init() {
        this.setProtocolName(TokenProtocolEnum.NORMAL.symbol);
        this.setProtocolVersion("1.0");
        this.setAllowDecimal(true);
    }

    @Override
    public String toString() {
        return "NormalProtocol{" +
                "protocolName='" + protocolName + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                ", isAllowDecimal=" + isAllowDecimal +
                '}';
    }


}
