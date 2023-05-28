package org.nachain.core.token.nft;

import com.google.common.collect.Lists;
import org.nachain.core.persistence.rocksdb.page.Page;
import org.nachain.core.persistence.rocksdb.page.PageCallback;
import org.nachain.core.persistence.rocksdb.page.PageService;
import org.nachain.core.persistence.rocksdb.page.SortEnum;
import org.nachain.core.token.TokenSingleton;
import org.nachain.core.token.protocol.NFTProtocol;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class NftService {


    public static NftItem newNftItem(long instance, long token, long tokenId, String owner, String fromTx, String mintTx) {
        NftItem nft = new NftItem();
        nft.setInstance(instance);
        nft.setToken(token);
        nft.setNftItemId(tokenId);
        nft.setOwner(owner);
        nft.setFromTx(fromTx);
        nft.setMintTx(mintTx);
        nft.setSale(false);
        nft.setSalePrice(BigInteger.ZERO);

        return nft;
    }


    public static NftItemDetail newNftItemDetail(long nftItemId, String name, String description, NftContentTypeEnum contentType, String preview, String original, List<NftItemAttr> properties) {
        NftItemDetail nftDetail = new NftItemDetail();

        nftDetail.setNftItemId(nftItemId);

        nftDetail.setName(name);

        nftDetail.setDescription(description);

        nftDetail.setContentType(contentType);

        nftDetail.setPreview(preview);

        nftDetail.setOriginal(original);

        nftDetail.setProperties(properties);

        return nftDetail;
    }


    public static boolean verifyOwner(long instance, List<Long> tokenIds, String fromAddress) {

        NftItemDAO nftItemDAO = new NftItemDAO(instance);
        try {

            for (long tokenId : tokenIds) {
                NftItem nftItem = nftItemDAO.get(tokenId);

                if (!fromAddress.equals(nftItem.getOwner())) {
                    return false;
                }
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }

        return true;
    }


    public static String getNftDetailUrl(NftItem item) {

        long tokenId = item.getToken();

        NFTProtocol nftProtocol = (NFTProtocol) TokenSingleton.get().get(tokenId).getProtocol();

        String url = String.format("%s%d%s", nftProtocol.getBaseURI(), item.getNftItemId(), nftProtocol.getBaseExt());
        return url;
    }


    public static String getNftCoverUrl(NftItem item) {

        long tokenId = item.getToken();

        NFTProtocol nftProtocol = (NFTProtocol) TokenSingleton.get().get(tokenId).getProtocol();

        String url = String.format("%s%s", nftProtocol.getBaseURI(), "cover.json");
        return url;
    }


    public static Page findByPage(long instanceId, int pageNum, int pageSize, SortEnum sortEnum) {
        PageService<NftItem> pageService = new PageService<NftItem>(NftItem.class, instanceId);
        Page page = pageService.findPage(sortEnum, pageNum, pageSize, new PageCallback() {
            @Override
            public Page gettingData(Page page) {
                try {
                    NftItemDAO nftItemDAO = new NftItemDAO(instanceId);

                    long startId = page.getStartId();
                    long endId = page.getEndId();


                    List<String> keyList = Lists.newArrayList();
                    List<NftItem> nftItemList = nftItemDAO.gets(startId, endId);
                    for (NftItem nftItem : nftItemList) {
                        if (nftItem != null) {
                            keyList.add(String.valueOf(nftItem.getNftItemId()));
                        } else {
                            keyList.add(null);
                        }
                    }

                    keyList.removeAll(Collections.singleton(null));
                    nftItemList.removeAll(Collections.singleton(null));

                    page.setKeyList(keyList);
                    page.setDataList(nftItemList);
                } catch (RocksDBException e) {
                    throw new RuntimeException(e);
                }
                return page;
            }
        });

        return page;
    }

}
