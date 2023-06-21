package org.nachain.core.nodes;

public interface IChain {

    /**
     *
     * @param nodeRole
     */
    void register(NodeRole nodeRole);


    /**
     *
     * @param nodeRole
     */
    void offline(NodeRole nodeRole);

}
