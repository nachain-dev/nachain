package org.nachain.core.dapp.internal.dns.domain;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;

public class DomainRentingDAO extends RocksDAO {


    public DomainRentingDAO() {
        super("DomainRenting", CoreInstanceEnum.DNS.id);
    }


    public boolean add(DomainRenting domainRenting) throws RocksDBException {
        String key = domainRenting.toFullDomain();

        if (get(key) != null) {
            return false;
        }
        put(key, domainRenting);
        return true;
    }


    public boolean put(DomainRenting domainRenting) throws RocksDBException {
        put(domainRenting.toFullDomain(), domainRenting);
        return true;
    }


    public DomainRenting get(String fullDomain) throws RocksDBException {
        String result = db.get(fullDomain);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, DomainRenting.class);
    }


    public List findAll() {
        return db.findAll(DomainRenting.class);
    }

}
