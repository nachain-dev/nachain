package org.nachain.core.dapp.internal.dns.events;

import lombok.Data;


@Data
public class DomainRentingDTO {

    String subdomain;


    String domain;


    String ipAddress;


    public String toFullDomain() {
        return getSubdomain() + "." + getDomain();
    }
}
