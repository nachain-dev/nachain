package org.nachain.core.mailbox;

import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;


public class MailBoxDAO extends RocksDAO {


    public MailBoxDAO(long instance) throws RocksDBException, IOException {
        super("MailBox", instance);
    }


    public boolean add(Mail m) throws RocksDBException {
        put(m.getHash(), m);
        return true;
    }


    public boolean edit(Mail m) throws RocksDBException {
        put(m.getHash(), m);
        return true;
    }


    public boolean delete(String mHash) throws RocksDBException {
        return super.delete(mHash);
    }


    public Mail find(String mHash) throws RocksDBException {
        String result = db.get(mHash);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, Mail.class);
    }


}
