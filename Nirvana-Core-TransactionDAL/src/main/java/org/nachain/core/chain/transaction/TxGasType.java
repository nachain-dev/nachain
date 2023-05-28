package org.nachain.core.chain.transaction;

import org.nachain.core.token.CoreTokenEnum;

import static java.util.Arrays.stream;


public enum TxGasType {


    NAC("NAC", CoreTokenEnum.NAC.id),


    USDN("USDN", CoreTokenEnum.USDN.id);

    public String name;
    public long value;

    TxGasType(String name, long value) {
        this.name = name;
        this.value = value;
    }


    public static TxGasType of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static TxGasType of(int value) {
        return stream(values()).filter(v -> v.value == value).findAny().orElse(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
