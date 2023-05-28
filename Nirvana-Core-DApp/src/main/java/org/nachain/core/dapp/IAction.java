package org.nachain.core.dapp;

import org.nachain.core.chain.Feedback;
import org.nachain.core.chain.transaction.Tx;
import org.nachain.core.chain.transaction.context.TxContext;

public interface IAction {


    Feedback handler(Tx tx, TxContext txContext);
}
