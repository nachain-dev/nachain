package org.nachain.core.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nachain.core.chain.instance.npp.AbstractInstanceNpp;
import org.nachain.core.token.protocol.IProtocol;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;

public class Token extends AbstractInstanceNpp {


    private String symbol;


    private String info;


    private String logoUrl;


    private String logoBase64;


    private BigInteger amount;


    private BigInteger initialAmount;


    private BigInteger initialTestNetAmount;


    private String initialAddress;


    private int decimals;


    private TokenTypeEnum tokenType;


    private TokenProtocolEnum tokenProtocol;


    private IProtocol protocol;

    public String getSymbol() {
        return symbol;
    }

    public Token setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public Token setInfo(String info) {
        this.info = info;
        return this;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public Token setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public String getLogoBase64() {
        return logoBase64;
    }

    public Token setLogoBase64(String logoBase64) {
        this.logoBase64 = logoBase64;
        return this;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public Token setAmount(BigInteger amount) {
        this.amount = amount;
        return this;
    }

    public BigInteger getInitialAmount() {
        return initialAmount;
    }

    public Token setInitialAmount(BigInteger initialAmount) {
        this.initialAmount = initialAmount;
        return this;
    }

    public BigInteger getInitialTestNetAmount() {
        return initialTestNetAmount;
    }

    public Token setInitialTestNetAmount(BigInteger initialTestNetAmount) {
        this.initialTestNetAmount = initialTestNetAmount;
        return this;
    }

    public String getInitialAddress() {
        return initialAddress;
    }

    public Token setInitialAddress(String initialAddress) {
        this.initialAddress = initialAddress;
        return this;
    }

    public int getDecimals() {
        return decimals;
    }

    public Token setDecimals(int decimals) {
        this.decimals = decimals;
        return this;
    }

    public TokenTypeEnum getTokenType() {
        return tokenType;
    }

    public Token setTokenType(TokenTypeEnum tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public TokenProtocolEnum getTokenProtocol() {
        return tokenProtocol;
    }

    public Token setTokenProtocol(TokenProtocolEnum tokenProtocol) {
        this.tokenProtocol = tokenProtocol;
        return this;
    }

    public IProtocol getProtocol() {
        return protocol;
    }

    public Token setProtocol(IProtocol protocol) {
        this.protocol = protocol;
        return this;
    }

    @Override
    public String toString() {
        return "Token{" +
                "symbol='" + symbol + '\'' +
                ", info='" + info + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", logoBase64='" + logoBase64 + '\'' +
                ", amount=" + amount +
                ", initialAmount=" + initialAmount +
                ", initialTestNetAmount=" + initialTestNetAmount +
                ", initialAddress='" + initialAddress + '\'' +
                ", decimals=" + decimals +
                ", tokenType=" + tokenType +
                ", tokenProtocol=" + tokenProtocol +
                ", protocol=" + protocol +
                ", id=" + id +
                ", instanceId=" + instanceId +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", instanceType=" + instanceType +
                ", hash='" + hash + '\'' +
                '}';
    }


    @Override
    public String toHashString() {
        return "Token{" +
                "symbol='" + symbol + '\'' +
                ", info='" + info + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", logoBase64='" + logoBase64 + '\'' +
                ", amount=" + amount +
                ", initialAmount=" + initialAmount +
                ", initialTestNetAmount=" + initialTestNetAmount +
                ", initialAddress='" + initialAddress + '\'' +
                ", decimals=" + decimals +
                ", tokenType=" + tokenType +
                ", tokenProtocol=" + tokenProtocol +
                ", protocol=" + protocol +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", instanceType=" + instanceType +
                '}';
    }


    public static Token toToken(String json) {
        return JsonUtils.jsonToPojo(json, Token.class);
    }


    public String toJson() throws JsonProcessingException {
        return JsonUtils.objectToJson(this);
    }

}
