package org.nachain.core.dapp.internal.dao.lock.events;

import lombok.Data;


@Data
public class FreeLockDTO {


    String address;


    double lockNacPrice;


    long lockDay;
}
