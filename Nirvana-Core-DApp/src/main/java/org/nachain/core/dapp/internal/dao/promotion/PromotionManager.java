package org.nachain.core.dapp.internal.dao.promotion;

import lombok.extern.slf4j.Slf4j;
import org.rocksdb.RocksDBException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PromotionManager {


    private final Map<String, Promotion> promotionMap = new ConcurrentHashMap<String, Promotion>();

    private PromotionDAO promotionDAO;

    public PromotionManager() {
        try {
            promotionDAO = new PromotionDAO();
        } catch (Exception e) {
            log.error("new PromotionManager() error", e);
        }

        loadDB();
    }


    private void loadDB() {

        try {
            List<Promotion> promotionList = promotionDAO.findAll();

            for (Promotion promotion : promotionList) {
                addToMap(promotion);
            }
            log.info("Initialize Promotion:" + promotionList.size());
        } catch (Exception e) {
            log.error("Initialize PromotionManager error", e);
        }
    }


    public void addToMap(Promotion promotion) throws RocksDBException {

        String clientName = promotion.getClientName();

        if (!promotionMap.containsKey(clientName)) {
            promotionMap.put(clientName, promotion);
        }
    }


    public void remove(String clientName) {
        if (promotionMap.containsKey(clientName)) {
            promotionMap.remove(clientName);
        }
    }


    public Promotion get(String clientName) throws RocksDBException {
        Promotion promotion = promotionMap.get(clientName);
        if (promotion == null) {

            loadDB();

            promotion = promotionMap.get(clientName);
        }

        return promotion;
    }


    public Map<String, Promotion> findAll() {
        return promotionMap;
    }


    public int count() {
        return promotionMap.size();
    }
}
