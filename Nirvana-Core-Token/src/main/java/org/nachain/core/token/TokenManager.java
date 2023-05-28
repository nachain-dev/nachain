package org.nachain.core.token;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.base.Amount;
import org.nachain.core.base.Unit;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.token.protocol.NormalProtocolService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class TokenManager {


    private final Map<String, Token> tokenMap = new ConcurrentHashMap();


    private final Map<Long, String> tokenIdMap = new ConcurrentHashMap<>();


    private final Map<String, Long> tokenSymbolMap = new ConcurrentHashMap();


    private final Map<Long, Long> tokenInstanceIdMap = new ConcurrentHashMap();

    private TokenDAO tokenDAO;

    public TokenManager() {
        try {

            tokenDAO = new TokenDAO();

            Token noneToken = TokenService.newToken("None Token", "-", "No tokens are defined", Amount.of("0", Unit.NAC), Amount.of("0", Unit.NAC), Amount.of("0", Unit.NAC), "", Unit.NAC.exp, TokenTypeEnum.KERNEL, TokenProtocolEnum.NORMAL, NormalProtocolService.newNormalProtocol());
            noneToken.setId(-1);

            addToMap(noneToken);
        } catch (Exception e) {
            log.error("new TokenManager() error", e);
        }


        loadDB();
    }


    private void loadDB() {

        try {
            List<Token> tokenList = tokenDAO.findAll();

            for (Token token : tokenList) {
                addToMap(token);
            }
            log.info("Initialize Token:" + tokenList.size());
        } catch (Exception e) {
            log.error("Initialize TokenManager error", e);
        }
    }


    public Map<String, Token> getTokenMap() {
        return tokenMap;
    }

    public Map<Long, String> getTokenIdMap() {
        return tokenIdMap;
    }

    public Map<String, Long> getTokenSymbolMap() {
        return tokenSymbolMap;
    }


    public boolean existSymbol(String symbol) {
        for (String key : tokenSymbolMap.keySet()) {
            if (key.equalsIgnoreCase(symbol)) {
                return true;
            }
        }

        return false;
    }


    public List<Token> getTokenList() {

        Collection<Token> tokens = tokenMap.values();

        List<Token> tokensList = Lists.newArrayList(tokens);

        TokenService.sortByTokenId(tokensList);
        return tokensList;
    }


    public void addToMap(Token token) {
        long tokenId = token.getId();
        long instanceId = token.getInstanceId();
        String symbol = token.getSymbol();
        String tokenHash = token.getHash();

        if (Strings.isNullOrEmpty(symbol)) {
            throw new ChainException("Symbol is null or empty. token=%s", token);
        }


        if (!tokenMap.containsKey(tokenHash)) {
            tokenMap.put(tokenHash, token);
            tokenIdMap.put(tokenId, tokenHash);
            tokenSymbolMap.put(symbol, tokenId);
            tokenInstanceIdMap.put(instanceId, tokenId);
        }
    }


    public void remove(String hash) {
        if (tokenMap.containsKey(hash)) {
            Token token = tokenMap.get(hash);

            tokenMap.remove(hash);
            tokenIdMap.remove(token.getId());
            tokenSymbolMap.remove(token.getSymbol());
            tokenInstanceIdMap.remove(token.getInstanceId());
        }
    }


    public Token get(String hash) {

        if (Strings.isNullOrEmpty(hash)) {
            return null;
        }

        Token token = tokenMap.get(hash);
        if (token == null) {

            loadDB();

            token = tokenMap.get(hash);
        }

        return token;
    }


    public Token get(long tokenId) {
        String hash = tokenIdMap.get(tokenId);
        return get(hash);
    }


    public Token getBySymbol(String symbol) {
        long tokenId = tokenSymbolMap.get(symbol);
        return get(tokenId);
    }


    public Token getByInstanceId(long instanceId) {
        if (!tokenInstanceIdMap.containsKey(instanceId)) {
            return null;
        }
        long tokenId = tokenInstanceIdMap.get(instanceId);
        return get(tokenId);
    }


    public int count() {
        return tokenMap.size();
    }


}

