package org.nachain.core.networks.chain;


public class NacPriceDTO {


    long usdPrice;

    public long getUsdPrice() {
        return usdPrice;
    }

    public NacPriceDTO setUsdPrice(long usdPrice) {
        this.usdPrice = usdPrice;
        return this;
    }

    @Override
    public String toString() {
        return "NacPriceDTO{" +
                "usdPrice=" + usdPrice +
                '}';
    }
}
