package org.nachain.core.dapp.internal.swap;

import lombok.extern.slf4j.Slf4j;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class SwapPairManager {


    private final Map<String, SwapPair> swapPairMap = new ConcurrentHashMap<>();

    public Map<String, SwapPair> getSwapPairMap() {
        return swapPairMap;
    }

    private SwapPairDAO swapPairDAO;

    public SwapPairManager() {
        try {
            swapPairDAO = new SwapPairDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        loadDB();
    }


    private void loadDB() {

        try {
            List<SwapPair> swapPairList = swapPairDAO.findAll();

            for (SwapPair swapPair : swapPairList) {
                addToMap(swapPair);
            }
            log.info("Initialize SwapPair:" + swapPairList.size());
        } catch (Exception e) {
            log.error("Initialize SwapPairManager error", e);
        }
    }


    public void addToMap(SwapPair swapPair) {
        String keyName = toKey(swapPair);

        if (!swapPairMap.containsKey(keyName)) {
            swapPairMap.put(keyName, swapPair);
        }
    }


    public SwapPair get(String pairName, SwapType swapType) {
        String keyName = toKey(pairName, swapType);
        SwapPair swapPair = swapPairMap.get(keyName);
        if (swapPair == null) {
            synchronized (swapPairMap) {
                swapPair = swapPairMap.get(keyName);
                if (swapPair == null) {

                    loadDB();

                    swapPair = swapPairMap.get(keyName);
                }
            }
        }

        return swapPair;
    }


    public String toKey(SwapPair swapPair) {
        return toKey(swapPair.getPairName(), swapPair.getSwapType());
    }

    public String toKey(String pairName, SwapType swapType) {
        return pairName + "_" + swapType.name;
    }

}
