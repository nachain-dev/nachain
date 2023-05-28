package org.nachain.core.oracle;


public interface IPrediction {

    byte[] encodeHash() throws Exception;


    String encodeHashString() throws Exception;
}
