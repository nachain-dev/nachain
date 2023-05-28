package org.nachain.core.chain.block;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.base.Amount;
import org.nachain.core.base.Unit;
import org.nachain.core.chain.config.PricingSystem;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceDetailService;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.unverified.UnverifiedTxDAO;
import org.nachain.core.chain.transaction.unverified.UnverifiedTxService;
import org.nachain.core.config.ChainConfig;
import org.nachain.core.config.miner.Mining;
import org.nachain.core.crypto.Key;
import org.nachain.core.networks.BroadcastWorker;
import org.nachain.core.persistence.rocksdb.page.Page;
import org.nachain.core.persistence.rocksdb.page.PageCallback;
import org.nachain.core.persistence.rocksdb.page.PageService;
import org.nachain.core.persistence.rocksdb.page.SortEnum;
import org.nachain.core.signverify.SignVerify;
import org.nachain.core.util.CommUtils;
import org.nachain.core.util.JsonUtils;
import org.nachain.core.util.SystemUtils;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class BlockService {


    public static String MACHINE_CLIENT_INFO;

    static {

        MACHINE_CLIENT_INFO = ChainConfig.APP_VERSION + "@" + SystemUtils.OsName.LINUX + " " + SystemUtils.getOsArch() + " C" + SystemUtils.getNumberOfProcessors() + "AM" + SystemUtils.getAvailableMemorySize() + "TM" + SystemUtils.getTotalMemorySize();
    }


    public static String getMachineClientInfo() {
        return MACHINE_CLIENT_INFO;
    }


    public static Block toBlock(String json) {
        return JsonUtils.jsonToPojo(json, Block.class);
    }


    public static Block getBlock(long instanceId, long blockHeight) {
        try {
            BlockDAO blockDAO = new BlockDAO(instanceId);
            return blockDAO.find(blockHeight);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public static Block newBlock(long instance, long height, String minerAddress, final BigInteger gasUsedTotal, final BigInteger bleedValueTotal, final BigInteger collectMined, String parentHash, BlockBody blockBody, IBlockRewardCallBack blockRewardCallBack) throws Exception {

        Key minerKey = Mining.getKey();


        BigInteger gasTotal = gasUsedTotal.subtract(bleedValueTotal);


        BigInteger gasDestroy = new BigDecimal(gasTotal).multiply(PricingSystem.Destroy.RATIO).toBigInteger();


        BigInteger uninstallAward = new BigDecimal(gasTotal).multiply(PricingSystem.UninstallAward.RATIO).toBigInteger();


        BigInteger gasAward = gasTotal.subtract(gasDestroy).subtract(uninstallAward);


        BigInteger blockReward = BlockService.getBlockReward(instance, height);


        List<String> minedTxHashList = blockRewardCallBack.blockReward(instance, height, blockReward, gasAward, minerAddress, minerKey);
        if (!minedTxHashList.isEmpty()) {
            for (String minedTxHash : minedTxHashList) {
                blockBody.addTransaction(minedTxHash);
            }
        }


        blockBody.setMinedSign(SignVerify.signHexString(blockBody, minerKey));


        List<String> transactions = blockBody.getTransactions();


        BlockExtraData extraData = new BlockExtraData();
        extraData.setClient(getMachineClientInfo());
        extraData.setDataCenter(ChainConfig.DATA_CENTER);
        extraData.setDappInvoking("");

        extraData.setGasDestroy(gasDestroy);

        extraData.setUninstallAward(uninstallAward);

        extraData.setGasAward(gasAward);

        extraData.setBleedValue(bleedValueTotal);

        extraData.setAcBlockHeight(InstanceDetailService.getAcBlockHeight());

        extraData.setNacBlockHeight(InstanceDetailService.getNacBlockHeight());


        String transactionsRoot = BlockService.genTransactionsRoot(transactions);


        long size = JsonUtils.objectToJson(transactions).getBytes().length - ChainConfig.EMPTY_BLOCK_SIZE;


        Block block = new Block();

        block.setInstance(instance);

        block.setHeight(height);

        block.setTimestamp(CommUtils.currentTimeMillis());

        block.setMiner(minerAddress);

        block.setBlockReward(blockReward);

        block.setCollectMined(collectMined);

        block.setSize(size);

        block.setParentHash(parentHash);

        block.setVersion(ChainConfig.BLOCK_VERSION);

        block.setTransactionsRoot(transactionsRoot);

        block.setTxVolume(transactions.size());

        block.setGasUsed(gasUsedTotal);

        block.setGasLimit(BigInteger.ZERO);

        block.setGasMinimum(BigInteger.ZERO);

        block.setGasMaximum(BigInteger.ZERO);

        block.setExtraData(extraData);

        block.setHash(block.encodeHashString());

        block.setMinedSign(SignVerify.signHexString(block, minerKey));

        return block;
    }


    public static String genTransactionsRoot(List<String> transactions) {
        MerkleTrees merkleTrees = new MerkleTrees(transactions);
        merkleTrees.merkleTree();
        return merkleTrees.getRoot();
    }


    public static BigInteger getBlockReward(long instance, long blockHeight) {

        if (instance == CoreInstanceEnum.NAC.id) {

            if (blockHeight >= 1 && blockHeight <= 20736000) {
                return Amount.of(BigInteger.valueOf(1), Unit.NAC).toBigInteger();
            } else if (blockHeight >= 20736001 && blockHeight <= 41472000) {
                return Amount.of(BigDecimal.valueOf(0.5), Unit.NAC).toBigInteger();
            } else if (blockHeight >= 41472001 && blockHeight <= 62208000) {
                return Amount.of(BigDecimal.valueOf(0.25), Unit.NAC).toBigInteger();
            } else if (blockHeight >= 62208001 && blockHeight <= 82944000) {
                return Amount.of(BigDecimal.valueOf(0.125), Unit.NAC).toBigInteger();
            } else if (blockHeight >= 82944001 && blockHeight <= 103680000) {
                return Amount.of(BigDecimal.valueOf(0.0625), Unit.NAC).toBigInteger();
            } else if (blockHeight >= 103680001 && blockHeight <= 395696320) {
                return Amount.of(BigDecimal.valueOf(0.0625), Unit.NAC).toBigInteger();
            }

            return Amount.ZERO.toBigInteger();
        } else if (instance == CoreInstanceEnum.APPCHAIN.id) {

            if (blockHeight >= 1 && blockHeight <= 2073600) {
                return Amount.of(BigInteger.valueOf(1), Unit.NAC).toBigInteger();
            } else if (blockHeight >= 2073601 && blockHeight <= 4147200) {
                return Amount.of(BigDecimal.valueOf(0.5), Unit.NAC).toBigInteger();
            } else if (blockHeight >= 4147201 && blockHeight <= 6220800) {
                return Amount.of(BigDecimal.valueOf(0.25), Unit.NAC).toBigInteger();
            } else if (blockHeight >= 6220801 && blockHeight <= 8294400) {
                return Amount.of(BigDecimal.valueOf(0.125), Unit.NAC).toBigInteger();
            } else if (blockHeight >= 8294401 && blockHeight <= 10368000) {
                return Amount.of(BigDecimal.valueOf(0.0625), Unit.NAC).toBigInteger();
            } else if (blockHeight >= 10368001 && blockHeight <= 39569632) {
                return Amount.of(BigDecimal.valueOf(0.0625), Unit.NAC).toBigInteger();
            }

            return Amount.ZERO.toBigInteger();
        } else {
            return Amount.ZERO.toBigInteger();
        }

    }


    public static void solidify(Block block, BlockBody blockBody) throws Exception {
        long instance = block.getInstance();
        BlockDAO blockDAO = new BlockDAO(instance);
        BlockBodyDAO blockBodyDAO = new BlockBodyDAO(instance);
        UnverifiedTxDAO unverifiedTxDAO = new UnverifiedTxDAO(instance);


        if (blockDAO.find(block.getHeight()) != null) {
            throw new ChainException("Block [" + block.getHeight() + "] already exists and cannot be created");
        }


        Key minerKey = Mining.getKey();


        List<String> txList = blockBody.getTransactions();

        log.info("Block successfully,block height:" + block.getHeight() + ",Contains transaction:" + txList.size());

        for (String txAddress : txList) {
            Tx tx = unverifiedTxDAO.find(txAddress);
            if (tx != null) {

                UnverifiedTxService.unverifiedToMined(tx, block.getHeight(), minerKey);
            } else {
                throw new ChainException("UnverifiedTx [" + txAddress + "] Not available, the height of the block is:" + block.getHeight());
            }
        }


        blockBodyDAO.add(blockBody);


        blockDAO.add(block);


        BroadcastWorker.mined(block);
    }


    public static boolean miningRight(long instance, long blockHeight) {


        return true;
    }


    public static Page findByPage(long instanceId, int pageNum, int pageSize, SortEnum sortEnum) {
        PageService<Block> pageService = new PageService<Block>(Block.class, instanceId);
        Page page = pageService.findPage(sortEnum, pageNum, pageSize, new PageCallback() {
            @Override
            public Page gettingData(Page page) {
                try {
                    BlockDAO blockDAO = new BlockDAO(instanceId);

                    long startId = page.getStartId();
                    long endId = page.getEndId();


                    List<String> keyList = Lists.newArrayList();
                    List<Block> blockList = blockDAO.gets(startId, endId);
                    for (Block block : blockList) {
                        if (block != null) {
                            keyList.add(String.valueOf(block.getHeight()));
                        } else {
                            keyList.add(null);
                        }
                    }

                    keyList.removeAll(Collections.singleton(null));
                    blockList.removeAll(Collections.singleton(null));

                    page.setKeyList(keyList);
                    page.setDataList(blockList);
                } catch (RocksDBException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return page;
            }
        });

        return page;
    }


    public static long getNacBlockHeight(Tx tx) {
        return getNacBlockHeight(tx.getInstance(), tx.getBlockHeight());
    }


    public static long getNacBlockHeight(long instanceId, long blockHeight) {
        Block block = BlockService.getBlock(instanceId, blockHeight);
        if (block != null) {
            return block.getExtraData().getNacBlockHeight();
        } else {
            return 0;
        }
    }

}
