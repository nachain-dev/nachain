package org.nachain.core.dapp.internal.fullnode.events;

import lombok.Data;


@Data
public class EnabledDTO {

    long id;

    public EnabledDTO() {
    }

    public EnabledDTO(long id) {
        this.id = id;
    }
}
