package org.nachain.core.persistence.page;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.RocksDBException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class PageService {


    public final static int DEFAULT_PAGE_SIZE = 20;


    public static Page newPage(Class pageClass, String pageKey, long pageNum) {
        Page page = new Page();
        page.setPageClass(pageClass);
        page.setPageKey(pageKey);
        page.setPageNum(pageNum);
        page.setItems(Lists.newArrayList());

        return page;
    }


    public static Page addItem(long instance, Class pageClazz, String pageKey, String item) throws RocksDBException {
        Page page;

        PageDAO pageDAO = new PageDAO(instance);
        PageDetailsDAO pageDetailsDAO = new PageDetailsDAO(instance);

        String pageClass = pageClazz.getSimpleName();

        PageDetails pageDetails = pageDetailsDAO.get(pageClass, pageKey);
        if (pageDetails == null) {

            pageDetails = PageDetailsService.newPageDetails(pageClazz, pageKey, DEFAULT_PAGE_SIZE);
            pageDetailsDAO.add(pageDetails);


            page = PageService.newPage(pageClazz, pageKey, 1);
            page.addItem(item);
            pageDAO.add(page);
        } else {

            long pageNum = pageDetails.getPageTotal();

            long pageSize = pageDetails.getPageSize();

            String key = pageDAO.toKey(pageClass, pageKey, pageNum);

            page = pageDAO.get(key);

            if (page == null || page.getItems().size() >= pageSize) {
                page = PageService.newPage(pageClazz, pageKey, pageNum + 1);

                pageDetails.addPageTotal();
            }

            page.addItem(item);
            pageDAO.edit(page);


            pageDetails.addItemTotal();
            pageDetailsDAO.edit(pageDetails);
        }

        return page;
    }


    public static Page addItem(long instance, Class pageClazz, String item) throws RocksDBException {
        return addItem(instance, pageClazz, "", item);
    }


    public static List<String> findAll(long instance, Class pageClazz) throws ExecutionException {
        return findAll(instance, pageClazz, "");
    }


    public static List<String> findAll(long instance, Class pageClazz, String pageKey) throws ExecutionException {
        List<String> itemList = Lists.newArrayList();

        PageDetails pageDetails;
        if (pageKey.isEmpty()) {
            pageDetails = PageDetailsService.get(instance, pageClazz);
        } else {
            pageDetails = PageDetailsService.get(instance, pageClazz, pageKey);
        }
        if (pageDetails != null) {
            long pageTotal = pageDetails.getPageTotal();
            for (long i = 1; i <= pageTotal; i++) {

                Page page;
                if (pageKey.isEmpty()) {
                    page = PageService.get(instance, pageClazz, i);
                } else {
                    page = PageService.get(instance, pageClazz, pageKey, i);
                }
                if (page != null) {
                    List<String> descList = page.getItems();
                    itemList.addAll(descList);
                }
            }
        }

        return itemList;
    }


    public static List<String> findAllDesc(long instance, Class pageClazz) throws ExecutionException {
        return findAllDesc(instance, pageClazz, "");
    }


    public static List<String> findAllDesc(long instance, Class pageClazz, String pageKey) throws ExecutionException {
        List<String> itemList = Lists.newArrayList();

        PageDetails pageDetails;
        if (pageKey.isEmpty()) {
            pageDetails = PageDetailsService.get(instance, pageClazz);
        } else {
            pageDetails = PageDetailsService.get(instance, pageClazz, pageKey);
        }
        if (pageDetails != null) {
            long pageTotal = pageDetails.getPageTotal();
            for (long i = 1; i <= pageTotal; i++) {

                Page descPage;
                if (pageKey.isEmpty()) {
                    descPage = PageService.getDesc(instance, pageClazz, i);
                } else {
                    descPage = PageService.getDesc(instance, pageClazz, pageKey, i);
                }
                if (descPage != null) {
                    List<String> descList = descPage.getItems();
                    itemList.addAll(descList);
                }
            }
        }

        return itemList;
    }


    public static Page get(long instance, Class pageClazz, String pageKey, long pageNum) throws ExecutionException {
        PageDAO pageDAO = new PageDAO(instance);
        try {
            String key = pageDAO.toKey(pageClazz.getSimpleName(), pageKey, pageNum);
            return pageDAO.get(key);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static Page get(long instance, Class pageClazz, long pageNum) throws ExecutionException {
        PageDAO pageDAO = new PageDAO(instance);
        try {
            String key = pageDAO.toKey(pageClazz.getSimpleName(), "", pageNum);
            return pageDAO.get(key);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }


    public static Page getDesc(long instance, Class pageClazz, long pageNum) throws ExecutionException {
        return getDesc(instance, pageClazz, "", pageNum);
    }


    public static Page getDesc(long instance, Class pageClazz, String pageKey, long pageNum) throws ExecutionException {

        PageDetails pageDetails;
        if (pageKey.isEmpty()) {
            pageDetails = PageDetailsService.get(instance, pageClazz);
        } else {
            pageDetails = PageDetailsService.get(instance, pageClazz, pageKey);
        }


        long descPageNum = pageDetails.getPageTotal() - pageNum + 1;
        long pageTotal = pageDetails.getPageTotal();

        Page page;
        if (pageKey.isEmpty()) {
            page = get(instance, pageClazz, descPageNum);
        } else {
            page = get(instance, pageClazz, pageKey, descPageNum);
        }
        if (page == null) {
            return null;
        }

        int pageItemSize = page.getItems().size();


        Page lastPage;
        if (pageKey.isEmpty()) {
            lastPage = get(instance, pageClazz, pageTotal);
        } else {
            lastPage = get(instance, pageClazz, pageKey, pageTotal);
        }
        if (lastPage == null) {
            return null;
        }

        int lastPageItemSize = lastPage.getItems().size();


        if (lastPageItemSize != DEFAULT_PAGE_SIZE) {

            List<String> itemList = page.getItems();

            int compSize = DEFAULT_PAGE_SIZE - lastPageItemSize;

            if (pageNum == 1) {

                List<String> newCompList = compensationDescData(instance, pageClazz, pageKey, descPageNum - 1, itemList.size());

                Collections.reverse(itemList);

                itemList.addAll(newCompList);

                page.setItems(itemList);
            } else if (compSize > 0) {

                int fromIndex = compSize;
                int toIndex = pageItemSize;
                if (fromIndex >= 0 && toIndex >= 0) {

                    Collections.reverse(itemList);

                    itemList = itemList.subList(fromIndex, toIndex);
                }


                List<String> newCompList = compensationDescData(instance, pageClazz, pageKey, descPageNum - 1, itemList.size());


                if (!newCompList.isEmpty()) {
                    newCompList = newCompList.subList(0, compSize);
                }


                itemList.addAll(newCompList);


                page.setItems(itemList);
            }
        } else {

            Collections.reverse(page.getItems());
        }

        return page;
    }


    private static List<String> compensationDescData(long instance, Class pageClazz, long compPageNum, int itemSize) throws ExecutionException {
        return compensationDescData(instance, pageClazz, "", compPageNum, itemSize);
    }


    private static List<String> compensationDescData(long instance, Class pageClazz, String pageKey, long compPageNum, int itemSize) throws ExecutionException {
        List<String> newCompList = Lists.newArrayList();

        Page compPage;
        if (pageKey.isEmpty()) {
            compPage = get(instance, pageClazz, compPageNum);
        } else {
            compPage = get(instance, pageClazz, pageKey, compPageNum);
        }
        if (compPage == null) {
            return newCompList;
        }


        List<String> compList = compPage.getItems();

        Collections.reverse(compList);


        long compSize = DEFAULT_PAGE_SIZE - itemSize;


        for (int i = 0; i < compSize; i++) {
            newCompList.add(compList.get(i));
        }

        return newCompList;
    }

}
