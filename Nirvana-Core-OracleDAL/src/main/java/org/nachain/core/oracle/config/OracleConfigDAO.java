package org.nachain.core.oracle.config;

import org.nachain.core.chain.config.AbstractConfigDAO;
import org.nachain.core.oracle.events.NacPrice;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.concurrent.ExecutionException;

public class OracleConfigDAO extends AbstractConfigDAO {

    private final String CHAIN_NAC_PRICE = "Chain.Oracle.NacPrice";
    private final String CHAIN_ORACLE_MIN_NAC_PRICE = "Chain.Oracle.MinNacPrice";


    public OracleConfigDAO(long instance) {
        super(instance);
    }

    @Override
    public String[] getRegKey() {
        return new String[]{CHAIN_NAC_PRICE, CHAIN_ORACLE_MIN_NAC_PRICE};
    }


    public boolean saveOracleNacPrice(NacPrice nacPrice) throws RocksDBException {
        put(CHAIN_NAC_PRICE, nacPrice.toJson());
        return true;
    }


    public NacPrice getOracleNacPrice() throws ExecutionException {
        String result = findValue(CHAIN_NAC_PRICE);
        if (result != null) {
            return JsonUtils.jsonToPojo(result, NacPrice.class);
        }
        return null;
    }


    public boolean saveOracleMinimumNacPrice(NacPrice nacPrice) throws RocksDBException {
        put(CHAIN_ORACLE_MIN_NAC_PRICE, nacPrice.toJson());
        return true;
    }


    public NacPrice getOracleMinimumNacPrice() throws ExecutionException {
        String result = findValue(CHAIN_ORACLE_MIN_NAC_PRICE);
        if (result != null) {
            return JsonUtils.jsonToPojo(result, NacPrice.class);
        }
        return null;
    }

}
