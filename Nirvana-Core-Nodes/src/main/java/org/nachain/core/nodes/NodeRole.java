package org.nachain.core.nodes;

import static java.util.Arrays.stream;

/**
 * The node role
 */
public enum NodeRole {

    NO_ROLE(0, "Unknown_Node", "Unknown node type"),

    POWF_DNS(1, "DNS_Node", "Decentration domain name system,It's a full node"),
    POWF_DATAFLOW(2, "Flow_Node", "Data flow dispatch node,Not full node"),

    // Provides block, storage, traffic, acceleration, and execution services
    DPOS_FULLNODE(10, " Full_Node", "Synchronize all data"),
    // Provides block, storage, traffic, acceleration, and execution services
    DPOS_SUPER(11, "Super_Node", "Super compute primary node,It's a full node"),
    // Provides storage, traffic, acceleration, and execution services
    DPOS_TEMPORARY(12, "Temporary_Node", "Temporary worker node,Not full node");

    public final int exp;
    public final String symbol;
    public final String explain;

    NodeRole(int exp, String symbol, String explain) {
        this.exp = exp;
        this.symbol = symbol;
        this.explain = explain;
    }

    /**
     * Decode the unit from symbol.
     *
     * @param symbol the symbol text
     * @return a Unit object if valid; otherwise false
     */
    public static NodeRole of(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }

    /**
     * Decode the NodeRole from id.
     *
     * @param exp the id value
     * @return a Unit object if valid; otherwise false
     */
    public static NodeRole of(int exp) {
        return stream(values()).filter(v -> v.exp == exp).findAny().orElse(null);
    }

}


