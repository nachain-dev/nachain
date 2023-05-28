package org.nachain.core.miner.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IArbitrament {

    String toHashString() throws Exception;


    String toSignString() throws Exception;


    String toString();


    String toJson() throws JsonProcessingException;


    byte[] encodeHash() throws Exception;


    String encodeHashString() throws Exception;
}
