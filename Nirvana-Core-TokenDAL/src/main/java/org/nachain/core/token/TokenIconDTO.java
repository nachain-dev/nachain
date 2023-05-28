package org.nachain.core.token;


public class TokenIconDTO {


    private long tokenId;


    private String logoUrl;


    private String logoBase64;

    public long getTokenId() {
        return tokenId;
    }

    public TokenIconDTO setTokenId(long tokenId) {
        this.tokenId = tokenId;
        return this;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public TokenIconDTO setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public String getLogoBase64() {
        return logoBase64;
    }

    public TokenIconDTO setLogoBase64(String logoBase64) {
        this.logoBase64 = logoBase64;
        return this;
    }

    @Override
    public String toString() {
        return "TokenIconDTO{" +
                "tokenId=" + tokenId +
                ", logoUrl='" + logoUrl + '\'' +
                ", logoBase64='" + logoBase64 + '\'' +
                '}';
    }
}
