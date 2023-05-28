package org.nachain.core.chain.transaction.recycle;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceDetailService;
import org.nachain.core.config.Constants;
import org.nachain.core.server.AbstractServer;
import org.rocksdb.RocksDBException;

import javax.management.timer.Timer;
import java.io.IOException;
import java.util.List;


@Slf4j
public class RecycleTxCleanerServer extends AbstractServer {


    private final RecycleTxDAO recycleTxDAO;

    public RecycleTxCleanerServer() {
        try {
            recycleTxDAO = new RecycleTxDAO();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getServerName() {
        return this.getClass().getSimpleName();
    }


    @Override
    public void starting() {

    }


    @Override
    public void executes() {
        try {

            long blockHeight = InstanceDetailService.getBlockHeight(CoreInstanceEnum.NAC.id);


            List<RecycleTx> recycleTxList = recycleTxDAO.findAll();
            for (RecycleTx recycleTx : recycleTxList) {

                if (blockHeight - recycleTx.getBlockHeight() >= Constants.DPoS_BLOCKS_PER_DAY) {
                    recycleTxDAO.delete(recycleTx.getHash());
                    log.info("Delete recycleTx old data:" + recycleTx.getHash());
                }
            }


            executeSleepMillis(Timer.ONE_HOUR);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }
}
