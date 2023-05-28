package org.nachain.libs.log;


public interface ICustom {


    void add(String loggerName, String level, String event, String explainInfo);


    void add(LogInfo logInfo);

}