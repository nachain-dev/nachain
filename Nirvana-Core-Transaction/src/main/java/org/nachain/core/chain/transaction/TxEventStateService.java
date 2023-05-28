package org.nachain.core.chain.transaction;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.das.TxDas;

import java.math.BigInteger;
import java.util.List;


@Slf4j
public class TxEventStateService {


    public static TxEventState newTxEventState(TxStateTypeEnum txStateTypeEnum, String tx, String address, BigInteger before, BigInteger after) {
        TxEventState txEventState = new TxEventState();
        txEventState.setStateType(txStateTypeEnum.id);
        txEventState.setTx(tx);
        txEventState.setAddress(address);
        txEventState.setBefore(before);
        txEventState.setAfter(after);
        txEventState.setStateDifference(before.subtract(after).abs());
        txEventState.setHash(txEventState.encodeHashString());

        return txEventState;
    }


    public static TxEventState addTxEventState(Tx tx, String address, BigInteger before, BigInteger after) {
        List<TxEventState> txEventStateList = tx.getEventStates();
        if (txEventStateList == null) {
            txEventStateList = Lists.newArrayList();
        }

        TxEventState txEventState = newTxEventState(TxStateTypeEnum.Tx, tx.getHash(), address, before, after);

        txEventStateList.add(txEventState);

        return txEventState;
    }


    public static TxEventState addTxDasEventState(TxDas txDas, String address, BigInteger before, BigInteger after) {
        List<TxEventState> txEventStateList = txDas.getEventStates();
        if (txEventStateList == null) {
            txEventStateList = Lists.newArrayList();
        }

        TxEventState txEventState = newTxEventState(TxStateTypeEnum.TxDas, txDas.getHash(), address, before, after);

        txEventStateList.add(txEventState);

        return txEventState;
    }


    public static TxEventState addTxBaseEventState(TxBase txBase, String address, BigInteger before, BigInteger after) {
        List<TxEventState> txEventStateList = txBase.getEventStates();
        if (txEventStateList == null) {
            txEventStateList = Lists.newArrayList();
        }

        TxEventState txEventState = newTxEventState(TxStateTypeEnum.Instance, txBase.getHash(), address, before, after);

        txEventStateList.add(txEventState);

        return txEventState;
    }

}
