package org.nachain.core.dapp.internal.dns.domain;

import java.math.BigInteger;


public class DomainRent {


    String address;


    String domain;


    BigInteger rentNac;


    String rentTx;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public BigInteger getRentNac() {
        return rentNac;
    }

    public void setRentNac(BigInteger rentNac) {
        this.rentNac = rentNac;
    }

    public String getRentTx() {
        return rentTx;
    }

    public void setRentTx(String rentTx) {
        this.rentTx = rentTx;
    }

    @Override
    public String toString() {
        return "DomainRent{" +
                "address='" + address + '\'' +
                ", domain='" + domain + '\'' +
                ", rentNac=" + rentNac +
                ", rentTx='" + rentTx + '\'' +
                '}';
    }
}
