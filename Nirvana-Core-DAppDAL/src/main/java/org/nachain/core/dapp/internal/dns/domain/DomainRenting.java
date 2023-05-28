package org.nachain.core.dapp.internal.dns.domain;

import java.math.BigInteger;


public class DomainRenting {


    String address;


    String subdomain;


    String domain;


    String ipAddress;


    String rentingTx;


    BigInteger rentingNac;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRentingTx() {
        return rentingTx;
    }

    public void setRentingTx(String rentingTx) {
        this.rentingTx = rentingTx;
    }

    public BigInteger getRentingNac() {
        return rentingNac;
    }

    public void setRentingNac(BigInteger rentingNac) {
        this.rentingNac = rentingNac;
    }

    @Override
    public String toString() {
        return "DomainRenting{" +
                "address='" + address + '\'' +
                ", subdomain='" + subdomain + '\'' +
                ", domain='" + domain + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", rentingTx='" + rentingTx + '\'' +
                ", rentingNac=" + rentingNac +
                '}';
    }


    public String toFullDomain() {
        return getSubdomain() + "." + getDomain();
    }
}
