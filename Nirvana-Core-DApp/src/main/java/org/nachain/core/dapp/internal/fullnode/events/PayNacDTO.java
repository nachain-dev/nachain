package org.nachain.core.dapp.internal.fullnode.events;

import lombok.Data;

@Data
public class PayNacDTO {

    private long id;

    public PayNacDTO() {

    }

    public PayNacDTO(long id) {
        this.id = id;
    }
}
