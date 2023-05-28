package org.nachain.core.chain.sign;

public interface IMinedSignObject {


    String toMinedSignString() throws Exception;


    String getMinedSign();
}
