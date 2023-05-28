package org.nachain.core.token;

public class TokenIconDTOService {


    public static TokenIconDTO newTokenIconDTO(long tokenId, String logoUrl, String logoBase64) {
        TokenIconDTO tokenIconDTO = new TokenIconDTO();
        tokenIconDTO.setTokenId(tokenId);
        tokenIconDTO.setLogoUrl(logoUrl);
        tokenIconDTO.setLogoBase64(logoBase64);

        return tokenIconDTO;
    }
}
