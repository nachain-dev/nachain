package org.nachain.core.chain.transaction;

import lombok.extern.slf4j.Slf4j;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.rocksdb.RocksDBException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@Slf4j
public class TxUtils {


    public static List<String> toTxHashList(List<Tx> txList) throws RocksDBException {
        List<String> txHashList = new ArrayList<>();
        for (Tx tx : txList) {
            txHashList.add(tx.getHash());
        }
        return txHashList;
    }


    public static String[] toTxHashArray(List<Tx> txList) throws RocksDBException {
        String[] txHashArray = new String[txList.size()];
        int i = 0;
        for (Tx tx : txList) {
            txHashArray[i++] = tx.getHash();
        }
        return txHashArray;
    }


    public static boolean isAddressHasKeyword(String address) {
        TxType txType = Stream.of(TxType.values()).filter(v -> v.name.equals(address)).findFirst().orElse(null);
        boolean exist = txType != null;

        TxEventType eventType = Stream.of(TxEventType.values()).filter(v -> v.name.equals(address)).findFirst().orElse(null);
        boolean exist2 = eventType != null;

        TxReservedWord txReservedWord = Stream.of(TxReservedWord.values()).filter(v -> v.name.equals(address)).findFirst().orElse(null);
        boolean exist3 = txReservedWord != null;

        return exist2 || exist || exist3;
    }


}
