package org.nachain.core.chain.instance.npp;

import com.fasterxml.jackson.core.JsonProcessingException;


public interface IInstanceNpp {


    String toHashString();


    String toString();


    String toJson() throws JsonProcessingException;


    byte[] encodeHash();


    String encodeHashString();
}
