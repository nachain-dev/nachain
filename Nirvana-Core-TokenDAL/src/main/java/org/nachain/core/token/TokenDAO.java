package org.nachain.core.token;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;

public class TokenDAO extends RocksDAO {


    public TokenDAO() {
        super("Token");
    }


    public boolean add(Token token) throws RocksDBException {
        String result = db.get(token.getHash());

        if (result != null) {
            return false;
        }
        put(token.getHash(), token);

        return true;
    }


    public boolean edit(Token token) throws RocksDBException {
        put(token.getHash(), token);
        return true;
    }


    public Token get(String hash) throws RocksDBException {
        String result = db.get(hash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Token.class);
    }


    public Token findBySymbol(String symbol) {
        List<String> list = db.findAllString();
        for (String value : list) {
            Token token = JsonUtils.jsonToPojo(value, Token.class);
            if (token.getSymbol().equals(symbol)) {
                return token;
            }
        }

        return null;
    }


    public List<Token> findAll() {
        List tokenList = db.findAll(Token.class);
        return tokenList;
    }


}
