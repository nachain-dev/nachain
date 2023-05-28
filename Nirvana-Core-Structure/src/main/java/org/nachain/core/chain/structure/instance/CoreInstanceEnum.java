package org.nachain.core.chain.structure.instance;

import static java.util.Arrays.stream;

public enum CoreInstanceEnum {


    NULL(-1, "", "NULL_INSTANCE", "Null instance", InstanceType.Core),


    APPCHAIN(0, "App Chain (PoWF)", "AC", "Application Chain", InstanceType.Core),


    NAC(1, "NAC (DPoS)", "NAC", "Nirvana Coin", InstanceType.Token),


    NOMC(2, "NOMC (DPoS)", "NOMC", "Nirvana operators maintenance chain", InstanceType.Token),


    USDN(3, "USDN (DPoS)", "USDN", "USD Nirvana", InstanceType.Token),


    FNC(4, "FNC (DPoS)", "FNC", "Full node credential", InstanceType.DApp),


    SUPERNODE(5, "VOTE (DPoS)", "VOTE", "Vote Super Node", InstanceType.DApp),


    SWAP(6, "SWAP (DPoS)", "SWAP", "Swap", InstanceType.DApp),


    FreeLock(CoreInstanceEnum.INSTANCE_UNDEFINED, "FreeLock", "FreeLock", "Free Lock", InstanceType.Core),


    Bridge(CoreInstanceEnum.INSTANCE_UNDEFINED, "Mapping Bridge", "BB", "Mapping bridge binding", InstanceType.DApp),


    NUSD(CoreInstanceEnum.INSTANCE_UNDEFINED, "NUSD", "NUSD", "NA USD", InstanceType.Token),


    DNS(CoreInstanceEnum.INSTANCE_UNDEFINED, "DNS", "DNS", "Decentralized Domain Name System", InstanceType.Core);

    public long id;
    public final String name;
    public final String symbol;
    public final String info;
    public final InstanceType instanceType;

    CoreInstanceEnum(int id, String name, String symbol, String info, InstanceType instanceType) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.info = info;
        this.instanceType = instanceType;

        if (id == CoreInstanceEnum.INSTANCE_UNDEFINED) {
            Instance instance = InstanceService.find(name);
            if (instance != null) {
                this.id = instance.getId();
            } else {

            }
        }
    }


    public static final int INSTANCE_UNDEFINED = -2;


    public static CoreInstanceEnum ofSymbol(String symbol) {
        return stream(values()).filter(v -> v.symbol.equals(symbol)).findAny().orElse(null);
    }


    public static CoreInstanceEnum of(String name) {
        return stream(values()).filter(v -> v.name.equals(name)).findAny().orElse(null);
    }


    public static CoreInstanceEnum of(long id) {
        return stream(values()).filter(v -> v.id == id).findAny().orElse(null);
    }


    public static void updateId(String name, long id) {
        CoreInstanceEnum instanceEnum = of(name);

        if (instanceEnum != null && instanceEnum.id == CoreInstanceEnum.INSTANCE_UNDEFINED) {
            instanceEnum.id = id;
        }
    }


}
