package org.nachain.core.persistence.rocksdb;

import lombok.Data;


@Data
public class Counter {


    private long dataAmount;


    public void add() {
        synchronized (this) {
            dataAmount++;
        }
    }


    public void remove() {
        synchronized (this) {
            dataAmount--;
        }
    }
}
