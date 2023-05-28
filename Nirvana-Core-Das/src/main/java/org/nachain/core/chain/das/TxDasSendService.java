package org.nachain.core.chain.das;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.das.unverified.UnverifiedTxDasDAO;
import org.nachain.core.chain.das.unverified.UnverifiedTxDasService;
import org.nachain.core.chain.exception.ChainException;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.intermediate.AccountTxDasHeightService;
import org.nachain.core.util.JsonUtils;

import java.math.BigInteger;


@Slf4j
public class TxDasSendService {


    public static TxDas sendTx(TxDas sendTx) throws Exception {
        TxDas tx;

        log.debug("Transfer objects:" + sendTx);


        TxContext txContext = JsonUtils.jsonToPojo(sendTx.getContext(), TxContext.class);
        long instance = sendTx.getInstance();
        long toInstance = txContext.getCrossToInstance();
        String to = sendTx.getTo();
        BigInteger tokenValue = sendTx.getValue();
        BigInteger gas = sendTx.getGas();
        long txHeight = sendTx.getTxHeight();


        BigInteger usedToken = tokenValue.add(gas);


        UnverifiedTxDasDAO fromUnverifiedTxDAO = new UnverifiedTxDasDAO(instance);


        if (txHeight == 0) {
            throw new ChainException("TxHeight is 0");
        } else {

            long accountTxHeight = AccountTxDasHeightService.getTxHeight(sendTx.getFrom(), sendTx.getInstance(), sendTx.getToken());

            if (txHeight <= accountTxHeight) {
                throw new ChainException("TxHeight must be equal to " + accountTxHeight);
            }

            TxDas checkUnTx = fromUnverifiedTxDAO.find(sendTx.getHash());
            if (checkUnTx != null) {
                throw new ChainException("Don't submit duplicate transactions");
            }
        }


        boolean isEnough = DasProfileService.checkBalanceEnough(sendTx).isSucceed();


        if (isEnough) {
            tx = sendTx;

            UnverifiedTxDasService.saveToUnverifiedAndBroadcast(tx);
        } else {

            throw new ChainException("The balance is not enough, There are no input transactions available, so no transfer is possible (Instance:" + instance + ",ToInstance:" + toInstance + ",To:" + to + ",tokenValue=" + tokenValue + ",gas=" + gas + ",usedToken=" + usedToken + ",TokenValue:" + tokenValue + ")");
        }

        return tx;
    }
}
