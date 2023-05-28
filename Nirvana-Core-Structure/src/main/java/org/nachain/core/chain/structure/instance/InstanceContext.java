package org.nachain.core.chain.structure.instance;

import org.nachain.core.chain.structure.instance.datachain.DataChain;
import org.nachain.core.chain.structure.instance.logicchain.LogicChain;
import org.nachain.core.nodes.supernode.SuperNode;

import java.util.List;


public class InstanceContext {


    private String instantiationAddress;


    private InstanceType instanceType;


    private int superNodes;


    private List<SuperNode> superNodeList;


    private LogicChain logicChain;


    private DataChain dataChain;


}
