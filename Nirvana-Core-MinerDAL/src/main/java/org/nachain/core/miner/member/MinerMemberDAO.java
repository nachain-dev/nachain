package org.nachain.core.miner.member;

import com.google.common.cache.CacheLoader;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.persistence.rocksdb.RocksDAO;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class MinerMemberDAO extends RocksDAO {


    public MinerMemberDAO() throws RocksDBException, IOException {
        super("MinerMember", CoreInstanceEnum.APPCHAIN.id);
    }

    @Override
    public CacheLoader cacheLoader() {
        return new CacheLoader<String, Optional<MinerMember>>() {
            public Optional<MinerMember> load(String walletAddress) throws RocksDBException {
                return Optional.ofNullable(get(walletAddress));
            }
        };
    }


    public MinerMember find(String walletAddress) throws ExecutionException {
        return (MinerMember) cache.get(walletAddress).orElse(null);
    }


    public boolean add(MinerMember minerMember) throws RocksDBException {

        if (get(minerMember.getWalletAddress()) != null) {
            return false;
        }
        put(minerMember.getWalletAddress(), minerMember);

        return true;
    }


    public boolean put(MinerMember minerMember) throws RocksDBException {
        put(minerMember.getWalletAddress(), minerMember);
        return true;
    }


    public MinerMember get(String walletAddress) throws RocksDBException {
        String result = db.get(walletAddress);
        if (result == null) {
            return null;
        }
        return JsonUtils.jsonToPojo(result, MinerMember.class);
    }


    public boolean delete(String walletAddress) throws RocksDBException {
        return super.delete(walletAddress);
    }


    public boolean exist(String key) {
        try {
            return db.get(key) != null;
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public List findAll() {
        return db.findAll(MinerMember.class);
    }
}
