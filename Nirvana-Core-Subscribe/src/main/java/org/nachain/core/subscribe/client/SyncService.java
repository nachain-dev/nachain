package org.nachain.core.subscribe.client;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.structure.instance.InstanceDetailService;
import org.nachain.core.networks.chain.BlockDTO;
import org.nachain.core.networks.chain.NacPriceDTO;
import org.nachain.core.networks.p2p.isc.SenderService;
import org.rocksdb.RocksDBException;

@Slf4j
public class SyncService {


    private static SyncServer syncServer;


    public static SyncServer startServer() {

        if (syncServer == null) {
            syncServer = new SyncServer();
        }

        if (syncServer.isRun() && !syncServer.isEnd()) {
            throw new RuntimeException("SyncClient is already running, do not start again.");
        }

        syncServer.startServer();

        return syncServer;
    }


    public static SyncServer getSyncClient() {
        return syncServer;
    }


    public static void getInstanceDetail(String nodeName, long instanceId) {

        SenderService.sendJsonNetData(syncServer.getChanel(nodeName), "/getInstanceDetail", "", instanceId);
    }


    public static void asyncNacPrice(String nodeName, long usdPrice) {
        NacPriceDTO nacPriceDTO = new NacPriceDTO();

        nacPriceDTO.setUsdPrice(usdPrice);

        log.info("Count NAC, nodeName={}, usdPrice={}", nodeName, usdPrice);


        if (syncServer != null) {
            SenderService.sendJsonNetData(syncServer.getChanel(nodeName), "/getNacPrice", "", nacPriceDTO);
        }
    }


    public static boolean asyncBlock(String nodeName, long instanceId) {
        long blockHeight;
        try {
            blockHeight = InstanceDetailService.getBlockHeight(instanceId) + 1;
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }


        SenderService.sendJsonNetData(syncServer.getChanel(nodeName), "/getBlock", "", new BlockDTO().setInstance(instanceId).setBlockHeight(blockHeight));


        return true;
    }


}
