package org.nachain.core.oracle;

import java.util.List;


public interface IPredictedResult {


    boolean addPrediction(String pHash);


    String toMinedSignString() throws Exception;


    String toString();


    List<String> initPredictions();
}
