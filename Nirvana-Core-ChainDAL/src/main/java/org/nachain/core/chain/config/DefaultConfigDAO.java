package org.nachain.core.chain.config;

public class DefaultConfigDAO extends AbstractConfigDAO {


    public DefaultConfigDAO(long instance) {
        super(instance);
    }

    @Override
    public String[] getRegKey() {
        return new String[0];
    }
}
