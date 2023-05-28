package org.nachain.core.dapp.internal.dns.domain;

import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.List;

public class DomainRentDAO extends RocksDAO {


    public DomainRentDAO() {
        super("DomainRent", CoreInstanceEnum.DNS.id);
    }


    public boolean add(DomainRent domainRent) throws RocksDBException {
        String key = domainRent.getDomain();

        if (get(key) != null) {
            return false;
        }
        put(key, domainRent);
        return true;
    }


    public boolean put(DomainRent domainRent) throws RocksDBException {
        put(domainRent.getDomain(), domainRent);
        return true;
    }


    public DomainRent get(String domain) throws RocksDBException {
        String result = db.get(domain);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, DomainRent.class);
    }


    public List findAll() {
        return db.findAll(DomainRent.class);
    }

}
