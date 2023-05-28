package org.nachain.core.dapp.internal.fullnode.events;

import lombok.Data;


@Data
public class AdminDTO {

    long id;


    String adminAddress;

    public AdminDTO() {
    }

    public AdminDTO(long id, String adminAddress) {
        this.id = id;
        this.adminAddress = adminAddress;
    }

}
