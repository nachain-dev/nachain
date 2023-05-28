package org.nachain.core.chain.transaction;

public interface ITx {


    String toHashString() throws Exception;


    String toSenderSignString() throws Exception;


    String toMinedSignString() throws Exception;


    String toString();


    String toJson();


    byte[] encodeHash();


    String encodeHashString();
}
