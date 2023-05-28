package org.nachain.core.intermediate;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.TxDAO;
import org.nachain.core.chain.transaction.TxIndex;
import org.nachain.core.chain.transaction.TxIndexDAO;
import org.nachain.core.intermediate.config.INTERMEDIATE_CONFIG;
import org.nachain.core.persistence.rocksdb.page.Page;
import org.nachain.core.persistence.rocksdb.page.PageCallback;
import org.nachain.core.persistence.rocksdb.page.PageService;
import org.nachain.core.persistence.rocksdb.page.SortEnum;
import org.nachain.core.token.CoreTokenEnum;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class AccountTxBatchService {


    public static AccountTxBatch newAccountTxBatch(String address, long tokenId) {
        AccountTxBatch accountTxBatch = new AccountTxBatch();
        accountTxBatch.setAddress(address);
        accountTxBatch.setTokenId(tokenId);
        accountTxBatch.setTxBatchAllTotal(0);
        accountTxBatch.setTxBatchInTotal(0);
        accountTxBatch.setTxBatchOutTotal(0);

        return accountTxBatch;
    }


    public static void saveAccountTx(long instanceId, String fromAddress, String toAddress, BigInteger txIndexId) throws RocksDBException, IOException, ExecutionException {
        saveAccountTx(instanceId, CoreTokenEnum.NULL.id, fromAddress, toAddress, txIndexId);
    }


    public static void saveAccountTx(long instanceId, long tokenId, String fromAddress, String toAddress, BigInteger txIndexId) throws RocksDBException, IOException, ExecutionException {

        saveAccountTx(instanceId, tokenId, fromAddress, TxBatchType.OUT, txIndexId);


        saveAccountTx(instanceId, tokenId, toAddress, TxBatchType.IN, txIndexId);


        saveAccountTx(instanceId, tokenId, fromAddress, TxBatchType.ALL, txIndexId);
        if (!fromAddress.equals(toAddress)) {
            saveAccountTx(instanceId, tokenId, toAddress, TxBatchType.ALL, txIndexId);
        }
    }


    private static void saveAccountTx(long instanceId, long tokenId, String address, TxBatchType txBatchType, BigInteger txIndexId) throws RocksDBException, IOException, ExecutionException {
        AccountTxBatchDAO accountTxBatchDAO = new AccountTxBatchDAO(instanceId);
        AccountTxsDAO accountTxsDAO = new AccountTxsDAO(instanceId);


        AccountTxBatch accountTxBatch = accountTxBatchDAO.find(address, tokenId);
        if (accountTxBatch == null) {

            accountTxBatch = AccountTxBatchService.newAccountTxBatch(address, tokenId);
        }

        long batchID = accountTxBatch.getTxBatchTotal(txBatchType);
        if (batchID == 0) {
            batchID = 1;
        }


        AccountTxs accountTxs = accountTxsDAO.find(address, tokenId, txBatchType, batchID);
        if (accountTxs == null) {
            accountTxs = AccountTxsService.newAccountTxs(address, tokenId, txBatchType, batchID);

            accountTxBatch.setTxBatchTotal(txBatchType, batchID);
        } else {

            if (accountTxs.getTxIDs().size() >= INTERMEDIATE_CONFIG.TXS_BATCH_SIZE) {
                batchID = batchID + 1;

                accountTxs = AccountTxsService.newAccountTxs(address, tokenId, txBatchType, batchID);

                accountTxBatch.setTxBatchTotal(txBatchType, batchID);
            }
        }


        List<BigInteger> txIDs = accountTxs.getTxIDs();
        txIDs.add(txIndexId);
        accountTxsDAO.put(accountTxs);


        accountTxBatchDAO.put(accountTxBatch);
    }


    public static Page findByPage(long instanceId, long tokenId, String address, TxBatchType txBatchType, int pageNum, SortEnum sortEnum) {
        PageService<Tx> pageService = new PageService<Tx>(Tx.class, instanceId);

        long pageSize = INTERMEDIATE_CONFIG.TXS_BATCH_SIZE;
        Page page = pageService.findPage(sortEnum, pageNum, pageSize, new PageCallback() {
            @Override
            public Page gettingData(Page page) {
                try {
                    AccountTxBatchDAO accountTxBatchDAO = new AccountTxBatchDAO(instanceId);
                    AccountTxsDAO accountTxsDAO = new AccountTxsDAO(instanceId);
                    TxDAO txDAO = new TxDAO(instanceId);
                    TxIndexDAO txIndexDAO = new TxIndexDAO(instanceId);


                    List<BigInteger> pageTxIds = Lists.newArrayList();


                    AccountTxBatch accountTxBatch = accountTxBatchDAO.find(address, tokenId);

                    if (accountTxBatch != null) {

                        long batchTotal = accountTxBatch.getTxBatchTotal(txBatchType);


                        long batchId;
                        if (sortEnum == SortEnum.ASC) {

                            batchId = pageNum;
                        } else {
                            batchId = batchTotal - pageNum + 1;
                        }


                        long descSkipAmount = 0;
                        if (sortEnum == SortEnum.DESC && pageNum >= 2) {
                            AccountTxs descFirstAccountTxs = accountTxsDAO.find(address, tokenId, txBatchType, batchTotal);
                            descSkipAmount = pageSize - descFirstAccountTxs.getTxIDs().size();
                        }


                        while (pageTxIds.size() < pageSize) {

                            AccountTxs accountTxs = accountTxsDAO.find(address, tokenId, txBatchType, batchId);
                            if (accountTxs == null) {
                                break;
                            } else {
                                if (accountTxs.getTxIDs().size() == 0) {
                                    break;
                                }


                                List<BigInteger> txIds = new ArrayList<>();
                                txIds.addAll(accountTxs.getTxIDs());

                                Collections.reverse(txIds);


                                int batchStartIndex = 0;

                                for (int i = batchStartIndex; i < pageSize; i++) {

                                    if (sortEnum == SortEnum.DESC && pageNum >= 2) {
                                        if (i + 1 <= descSkipAmount) {
                                            continue;
                                        }
                                    }

                                    if (txIds.size() >= i + 1) {
                                        pageTxIds.add(txIds.get(i));
                                    }
                                    if (pageTxIds.size() >= pageSize) {
                                        break;
                                    }
                                }


                                if (sortEnum == SortEnum.ASC) {
                                    batchId++;
                                } else {
                                    batchId--;
                                }
                            }
                        }
                    }


                    List<String> keyList = Lists.newArrayList();

                    List<Tx> dataList = Lists.newArrayList();


                    List<TxIndex> txIndexList = txIndexDAO.gets(pageTxIds);


                    for (TxIndex txIndex : txIndexList) {
                        if (txIndex != null) {
                            keyList.add(txIndex.getTxHash());
                            Tx tx = txDAO.find(txIndex.getTxHash());
                            dataList.add(tx);
                        }
                    }
                    page.setKeyList(keyList);
                    page.setDataList(dataList);

                } catch (RocksDBException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                return page;
            }
        });

        return page;
    }


}
