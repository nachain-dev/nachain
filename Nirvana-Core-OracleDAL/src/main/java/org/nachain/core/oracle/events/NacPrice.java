package org.nachain.core.oracle.events;

import org.nachain.core.util.JsonUtils;

import java.math.BigDecimal;


public class NacPrice implements IEventData {


    private BigDecimal price;


    private long timestamp;


    private long blockHeight;

    public BigDecimal getPrice() {
        return price;
    }

    public NacPrice setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public NacPrice setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public NacPrice setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    @Override
    public String toString() {
        return "NacPrice{" +
                "price=" + price +
                ", timestamp=" + timestamp +
                ", blockHeight=" + blockHeight +
                '}';
    }

    @Override
    public String toJson() {
        return JsonUtils.objectToJson(this);
    }


}
