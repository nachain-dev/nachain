package org.nachain.core.chain.das.processor;

import org.nachain.core.chain.Feedback;
import org.nachain.core.chain.das.TxDas;
import org.nachain.core.crypto.Key;

public interface ITxDasProcessor {


    Feedback doBefore(TxDas txDas);


    Feedback doFinally(Feedback report, TxDas txDas, Key senderKey);


    Feedback DAS_ACTION(TxDas txDas);


    Feedback DAS_PREPAID(TxDas txDas);


}
