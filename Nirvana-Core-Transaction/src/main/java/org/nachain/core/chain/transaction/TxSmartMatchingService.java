package org.nachain.core.chain.transaction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TxSmartMatchingService {


    public static TxSmartMatching txSmartMatching(List<Tx> allowSendTxList, BigInteger matchTotal) {
        TxSmartMatching tsm = new TxSmartMatching();


        ArrayList<Tx> inputSendTxList = new ArrayList<Tx>();

        BigInteger checkValue = BigInteger.ZERO;
        for (Tx t : allowSendTxList) {


            checkValue = checkValue.add(t.getValue());


            inputSendTxList.add(t);


            if (checkValue.compareTo(matchTotal) == 0) {
                break;
            } else if (checkValue.compareTo(matchTotal) == 1) {

                BigInteger changeValue = checkValue.subtract(matchTotal);

                BigInteger changeTxUsed = t.getValue().subtract(changeValue);
                tsm.setChangeTx(t);
                tsm.setChangeValue(changeValue);
                tsm.setChangeTxUsed(changeTxUsed);
                break;
            }
        }

        tsm.setInputTxValue(checkValue);

        tsm.setInputTxList(inputSendTxList);


        tsm.setEnough(tsm.getInputTxValue().compareTo(matchTotal) != -1);

        return tsm;
    }
}
