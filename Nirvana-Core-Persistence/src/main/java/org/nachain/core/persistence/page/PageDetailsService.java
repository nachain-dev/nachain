package org.nachain.core.persistence.page;

import com.google.common.collect.Lists;
import org.rocksdb.RocksDBException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PageDetailsService {


    public static PageDetails newPageDetails(Class pageClazz, String pageKey, long pageSize) {
        PageDetails pageDetails = new PageDetails();
        pageDetails.setPageClass(pageClazz);
        pageDetails.setPageKey(pageKey);
        pageDetails.setPageSize(pageSize);
        pageDetails.setPageTotal(1);
        pageDetails.setItemTotal(1);

        return pageDetails;
    }


    public static PageDetails get(long instance, Class pageClazz, String pageKey) throws ExecutionException {
        String pageClass = pageClazz.getSimpleName();
        PageDetailsDAO pageDetailsDAO = new PageDetailsDAO(instance);
        PageDetails pageDetails = null;
        try {
            pageDetails = pageDetailsDAO.get(pageClass, pageKey);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }

        return pageDetails;
    }


    public static PageDetails get(long instance, Class pageClazz) throws ExecutionException {
        String pageClass = pageClazz.getSimpleName();
        PageDetailsDAO pageDetailsDAO = new PageDetailsDAO(instance);
        PageDetails pageDetails = null;
        try {

            String key = pageDetailsDAO.toKey(pageClass, "");
            pageDetails = pageDetailsDAO.get(key);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }

        return pageDetails;
    }


    public static void rebuildPageDetails(long instance, Class pageClazz, String pageKey) throws ExecutionException, RocksDBException {

        PageDAO pageDAO = new PageDAO(instance);
        PageDetailsDAO pageDetailsDAO = new PageDetailsDAO(instance);

        PageDetails pageDetails = PageDetailsService.get(instance, pageClazz, pageKey);
        if (pageDetails != null) {


            List<String> itemList = Lists.newArrayList();
            for (int i = 1; i <= pageDetails.getPageTotal(); i++) {
                Page page = PageService.get(instance, pageClazz, i);
                if (page != null) {
                    itemList.addAll(page.getItems());
                }

                pageDAO.delete(page.toKey());
            }


            pageDetailsDAO.delete(pageDetails.toKey());


            for (String item : itemList) {
                PageService.addItem(instance, pageClazz, pageKey, item);
            }
        }
    }

}
