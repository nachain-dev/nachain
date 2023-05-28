package org.nachain.core.chain.structure.instance;


public class InstanceSingleton {

    private static InstanceManager instanceManager;

    static {
        instanceManager = new InstanceManager();
    }

    public static InstanceManager get() {
        return instanceManager;
    }

}
