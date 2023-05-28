package org.nachain.core.dapp.internal.dns.events;

import lombok.Data;

import java.math.BigInteger;


@Data
public class DomainRentDTO {


    String domain;


    BigInteger rentNac;

}
