package org.nachain.core.subscribe.client.process;

import org.nachain.core.chain.Feedback;
import org.nachain.core.chain.transaction.Tx;

public interface ITxProcess {


    Feedback process(Tx tx);
}
