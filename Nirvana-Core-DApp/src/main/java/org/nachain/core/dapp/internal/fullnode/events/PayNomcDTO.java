package org.nachain.core.dapp.internal.fullnode.events;

import lombok.Data;
import org.nachain.core.nodes.NodeRole;

@Data
public class PayNomcDTO {


    private String ownerAddress;


    private String beneficiaryAddress;


    private NodeRole nodeRole;

    public PayNomcDTO() {
    }

    public PayNomcDTO(String ownerAddress, String beneficiaryAddress, NodeRole nodeRole) {
        this.ownerAddress = ownerAddress;
        this.beneficiaryAddress = beneficiaryAddress;
        this.nodeRole = nodeRole;
    }

}
