package org.nachain.core.chain.das.unverified;

import org.nachain.core.chain.das.TxDas;
import org.nachain.core.chain.das.TxDasDAO;
import org.nachain.core.chain.structure.instance.ChainTypeEnum;
import org.nachain.core.chain.transaction.TxGasService;
import org.nachain.core.chain.transaction.TxStatus;
import org.nachain.core.crypto.Key;
import org.nachain.core.networks.BroadcastWorker;
import org.nachain.core.signverify.SignVerify;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.math.BigInteger;


public class UnverifiedTxDasService {


    public static void unverifiedToMined(TxDas unTxDas, Key minerKey) throws Exception {

        if (unTxDas.getStatus() == TxStatus.PENDING.value) {
            TxDasDAO txDAO = new TxDasDAO(unTxDas.getInstance());
            UnverifiedTxDasDAO unverifiedTxDasDAO = new UnverifiedTxDasDAO(unTxDas.getInstance());


            BigInteger minGasDAS = TxGasService.calcGasAmount(ChainTypeEnum.DAS, true);
            BigInteger bleedValue = unTxDas.getGas().subtract(minGasDAS);


            unTxDas.setBleedValue(bleedValue);
            unTxDas.setStatus(TxStatus.COMPLETED.value);
            unTxDas.setMinedSign(SignVerify.signHexString(unTxDas, minerKey));
            txDAO.add(unTxDas);


            unverifiedTxDasDAO.delete(unTxDas.getHash());


            BroadcastWorker.mined(unTxDas);
        }
    }


    public static void saveToUnverifiedAndBroadcast(TxDas unTxDas) throws RocksDBException, IOException {

        if (unTxDas.getStatus() == TxStatus.PENDING.value) {

            UnverifiedTxDasDAO unTxDasDAO = new UnverifiedTxDasDAO(unTxDas.getInstance());
            unTxDasDAO.add(unTxDas);


            BroadcastWorker.unverifiedTxDas(unTxDas);
        }
    }

}
