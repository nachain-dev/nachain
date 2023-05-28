package org.nachain.core.chain.block;

import com.fasterxml.jackson.core.JsonProcessingException;


public interface IBlock {


    String toHashString() throws Exception;


    String toMinedSignString() throws Exception;


    String toString();


    String toJson() throws JsonProcessingException;


    byte[] encodeHash() throws Exception;


    String encodeHashString() throws Exception;
}
